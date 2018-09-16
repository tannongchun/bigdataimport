package com.sunducation.waterflow.dto;

import java.io.Serializable;

/**
 * @version 1.0
 * @description:
 * @projectName: com.sunducation.waterflow.dto
 * @className: water-level-flow-relation
 * @author:谭农春
 * @createTime:2018/9/14 20:46
 */
public class DataDTO implements Serializable
{
  private Integer id ;
  private Integer no ;
  private String name;
  private String grade ;
  private Integer age ;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getNo() {
    return no;
  }

  public void setNo(Integer no) {
    this.no = no;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
