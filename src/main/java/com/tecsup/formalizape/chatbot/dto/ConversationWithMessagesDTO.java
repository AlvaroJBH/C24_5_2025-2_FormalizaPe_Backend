package com.tecsup.formalizape.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ConversationWithMessagesDTO {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MessageResponseDTO> messages;
}
