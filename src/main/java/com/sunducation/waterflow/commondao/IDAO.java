package com.sunducation.waterflow.commondao;

import java.util.List;

/**
 * dao接口泛型，用于生成dao接口
 * @param <T>
 * @param <Texample>
 */
public interface IDAO<T, Texample>{

    public Long countByExample(Texample example);

    public Long deleteByExample(Texample example);

    public Long deleteByPrimaryKey(Long id);

    public List<T> selectByExample(Texample example);

    public Long updateByExample(T record, Texample example);

    public Long updateByPrimaryKey(T record);

    public Long insert(T record);

    public Long insertSelective(T record);

    public T selectByPrimaryKey(Long id);

    public Long updateByExampleSelective(T record, Texample example);

    public Long updateByPrimaryKeySelective(T record);

}
