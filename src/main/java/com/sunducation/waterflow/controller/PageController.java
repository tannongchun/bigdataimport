package com.sunducation.waterflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  @version  1.0
 *
 */
@Controller
public class PageController {

  @GetMapping("/index")
  public  String index(){
    // 映射地址 ->resources\templates\index.html
    return "index";
  }

}
