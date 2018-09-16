package com.sunducation.waterflow.commondao.impl;

import com.sunducation.waterflow.commondao.IDAO;
import com.sunducation.waterflow.commondao.mapper.IMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * dao接口的虚拟实现类，用于提供一些通用的方法。
 * @param <T>
 * @param <Texample>
 */
public abstract class DAOImpl<T, Texample> implements IDAO<T, Texample> {

    @Autowired
    public IMapper<T, Texample> mapper;

    @Override
    public Long countByExample(Texample example){
        return mapper.countByExample(example);
    }

    @Override
    public Long deleteByExample(Texample example){
        return mapper.deleteByExample(example);
    }

    @Override
    public Long deleteByPrimaryKey(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public Long insert(T record){
        return mapper.insert(record);
    }

    @Override
    public Long insertSelective(T record){
        return mapper.insertSelective(record);
    }

    @Override
    public List<T> selectByExample(Texample example){
        return mapper.selectByExample(example);
    }

    @Override
    public T selectByPrimaryKey(Long id){
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public Long updateByExampleSelective(T record, Texample example){
        return mapper.updateByExampleSelective(record, example);
    }

    @Override
    public Long updateByExample(T record, Texample example){
        return mapper.updateByExample(record, example);
    }

    @Override
    public Long updateByPrimaryKeySelective(T record){
        return mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Long updateByPrimaryKey(T record){
        return mapper.updateByPrimaryKey(record);
    }
}
