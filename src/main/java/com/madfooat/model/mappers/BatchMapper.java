package com.madfooat.model.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.madfooat.model.Batch;
import com.madfooat.web.dto.BatchDTO;

@Mapper
public interface BatchMapper {

	BatchMapper INSTANCE = Mappers.getMapper( BatchMapper.class ); 
	 
    BatchDTO batchToBatchDto(Batch batch); 
    
    List<BatchDTO> batchToBatchDto(List<Batch> batch); 
}
