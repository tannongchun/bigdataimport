package com.sunducation.waterflow.service.impl;


import com.sunducation.waterflow.dto.DataDTO;
import com.sunducation.waterflow.muti.InsertDataTask;
import com.sunducation.waterflow.service.ImportDataService;
import com.sunducation.waterflow.utils.ParseXMLUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;


/**
 * @version 1.0
 * @description:
 * @projectName: com.sunducation.rbac.service.impl
 * @className: rbac
 * @author:谭农春
 * @createTime:2018/8/2 14:26
 */
@Service
public class ImportDataServiceImpl  implements ImportDataService {

  @Override
  public Long parseAndinsertDb(String fiepath) throws Exception {
    long startMini = System.currentTimeMillis();
    final BlockingQueue<DataDTO> results = new LinkedBlockingQueue<DataDTO>();
    // 多线程插入
    final CyclicBarrier barrier = new CyclicBarrier(6);
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    executorService.execute(new Runnable() {
      @Override
      public void run() {        // 解析
        ParseXMLUtil xmlUtil = new ParseXMLUtil(results);
        try {
          xmlUtil.processFirstSheet(fiepath);
          barrier.await();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
//    ParseXMLUtil xmlUtil = new ParseXMLUtil(results);
//    xmlUtil.processFirstSheet(fiepath);
   // Thread.sleep(1000);
    // 提交任务
    executorService.execute(new InsertDataTask(results,barrier,"task1"));
    executorService.execute(new InsertDataTask(results,barrier,"task2"));
    executorService.execute(new InsertDataTask(results,barrier,"task3"));
    executorService.execute(new InsertDataTask(results,barrier,"task4"));
    barrier.await();
    executorService.shutdown();
    System.out.println("执行完成");
    System.out.println(results.size());
    long endMini = System.currentTimeMillis();
    System.out.println(" take time is " + (endMini - startMini)/1000.0);
    return (endMini - startMini)/1000;
  }
}
