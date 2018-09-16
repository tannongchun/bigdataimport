package com.sunducation.waterflow.dao.mapper;

import com.sunducation.waterflow.commondao.mapper.IMapper;
import java.util.List;

import com.sunducation.waterflow.dto.DataDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ImportDataMapper {

  // 插入
  public  void batchInsert(@Param("list")List<DataDTO> list);
}