package com.sunducation.waterflow;

import java.io.Serializable;

/**
 *   返回信息
 */
public class ResultMsg implements Serializable {
    private int code =0 ;
    private String msg ="ok";
    private Object data ;

  public ResultMsg(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public ResultMsg(int code, String msg, Object data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public ResultMsg() {
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
