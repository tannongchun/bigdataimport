package com.sunducation.waterflow.commondao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * mybatis mapper 泛型接口，所提供的方法跟mybatis自动生成的xml可以对应。
 * 使用：先用mybatis generator生成entity和sql xml，然后根据entity写一个interface
 * 扩展本interface
 * @param <T>: enti@param <Texample>: entity example
 */
public interface IMapper<T, Texample>{

    Long countByExample(Texample example);

    Long deleteByExample(Texample example);

    Long deleteByPrimaryKey(Long id);

    List<T> selectByExample(Texample example);

    Long updateByExample(@Param("record") T record, @Param("example") Texample example);

    Long updateByPrimaryKey(T record);

    Long insert(T record);

    Long insertSelective(T record);

    T selectByPrimaryKey(Long id);

    Long updateByExampleSelective(@Param("record") T record, @Param("example") Texample example);

    Long updateByPrimaryKeySelective(T record);
}
