package com.tecsup.formalizape.formalization.mapper;

import com.tecsup.formalizape.formalization.dto.BusinessDTO;
import com.tecsup.formalizape.formalization.model.Business;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    // RequestDTO -> Entity
    @Mapping(target = "owner", ignore = true) // resolver owner en el service
    @Mapping(target = "businessProcedures", ignore = true)
    Business toEntity(BusinessDTO.RequestDTO dto);

    // Entity -> ResponseDTO
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.username", target = "ownerUsername")
    BusinessDTO.ResponseDTO toResponse(Business business);
}
