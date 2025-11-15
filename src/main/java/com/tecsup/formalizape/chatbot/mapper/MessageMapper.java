package com.tecsup.formalizape.chatbot.mapper;

import com.tecsup.formalizape.chatbot.dto.MessageResponseDTO;
import com.tecsup.formalizape.chatbot.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponseDTO toResponseDTO(Message message);
}
