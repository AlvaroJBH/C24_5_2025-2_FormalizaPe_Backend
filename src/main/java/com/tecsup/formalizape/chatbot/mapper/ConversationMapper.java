package com.tecsup.formalizape.chatbot.mapper;

import com.tecsup.formalizape.chatbot.dto.ConversationResponseDTO;
import com.tecsup.formalizape.chatbot.dto.ConversationWithMessagesDTO;
import com.tecsup.formalizape.chatbot.dto.MessageResponseDTO;
import com.tecsup.formalizape.chatbot.model.Conversation;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Comparator;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    // Entity -> DTO básico
    ConversationResponseDTO toResponseDTO(Conversation conversation);

    // Entity -> DTO con mensajes
    @Mapping(source = "messages", target = "messages")
    ConversationWithMessagesDTO toWithMessagesDTO(Conversation conversation);

    // Ordenar mensajes después de mapear
    @AfterMapping
    default void sortMessages(@MappingTarget ConversationWithMessagesDTO dto) {
        if (dto.getMessages() != null) {
            dto.getMessages().sort(Comparator.comparing(MessageResponseDTO::getCreatedAt));
        }
    }
}
