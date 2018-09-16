package com.sunducation.waterflow.muti;

import com.sunducation.waterflow.dao.mapper.ImportDataMapper;
import com.sunducation.waterflow.dto.DataDTO;
import com.sunducation.waterflow.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @version 1.0
 * @description: 另外线程插入
 * @projectName: com.kongxiang.muti
 * @className: DesignModel
 * @author:tannc
 * @createTime:2018/9/15 9:36
 */
public class InsertDataTask implements Runnable {

  private final ImportDataMapper importDataMapper;

  private final BlockingQueue<DataDTO> queue;

  private final CyclicBarrier barrier;
  private final String taskName ;

  public InsertDataTask(BlockingQueue<DataDTO> queue, CyclicBarrier barrier,String taskName) {
    this.queue = queue;
    this.barrier = barrier;
    this.taskName =taskName;
    importDataMapper = SpringContextUtils.getBean("importDataMapper");
  }

  @Override
  public void run() {
    List<DataDTO> list = new ArrayList<DataDTO>(256);
    final ReentrantLock lock = new ReentrantLock();
    while (!queue.isEmpty()) {
      lock.lock();
      list.clear();
      // 出栈
      queue.drainTo(list,10000);
      //分批处理
      if(null!=list&&list.size()>0){
//        System.err.println(taskName + " 插入size +++> " + list.size());
        int pointsDataLimit = 10000;//限制条数
        Integer size = list.size();
        //判断是否有必要分批
        if(pointsDataLimit<size){
          int part = size/pointsDataLimit;//分批数
          // System.out.println("共有 ： "+size+"条，！"+" 分为 ："+part+"批");
          //
          for (int i = 0; i < part; i++) {
            //1000条
            List<DataDTO> listPage = list.subList(0, pointsDataLimit);
            importDataMapper.batchInsert(listPage);
            //剔除
            list.subList(0, pointsDataLimit).clear();
          }
          if(!list.isEmpty()){
            //表示最后剩下的数据
            importDataMapper.batchInsert(list);
          }
        }else{
          importDataMapper.batchInsert(list);
        }
      } // end if
      if (queue.isEmpty()){
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      lock.unlock();
    } // while
    try {
      barrier.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }
  }
}
