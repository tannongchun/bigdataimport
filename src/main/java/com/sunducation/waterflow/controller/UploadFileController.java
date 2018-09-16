package com.sunducation.waterflow.controller;


import com.sunducation.waterflow.ResultMsg;
import com.sunducation.waterflow.service.ImportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @version 1.0
 * @description:
 * @projectName: 上传文件
 * @className: 上传文件
 * @author:谭农春
 * @createTime:2018/9/6 19:32
 */
@RestController
@RequestMapping("/api/v1/uploads")
public class UploadFileController {

   @Autowired
   private ImportDataService importDataService ;
  //处理文件上传
  @RequestMapping(value="/excel", method = RequestMethod.POST)
  public ResultMsg uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
    if (!file.isEmpty()) {
       //
      String filename = file.getOriginalFilename();
      String prefix=filename.substring(filename.lastIndexOf(".")+1);
      // Excel 2007版9
      if( null != prefix && prefix.equalsIgnoreCase("xlsx")){
        // 返回记录数
        Long count= null;
        try {
          File f = null;
          f=File.createTempFile("tmp", null);
          file.transferTo(f);
          f.deleteOnExit();
          // 文件路径
          count = importDataService.parseAndinsertDb(f.getAbsolutePath());
        } catch (IOException e) {
          e.printStackTrace();
        }
        return  new ResultMsg(0,"文件上传成功，共花费 " + count +" 秒");
      }
      else{
        return  new ResultMsg(400,"请选择文件");
      }


    } else {
      return  new ResultMsg(400,"文件不存在");
    }
  }

}
