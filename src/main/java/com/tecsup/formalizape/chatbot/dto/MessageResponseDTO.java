package com.tecsup.formalizape.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageResponseDTO {
    private String role; // "user" o "assistant"
    private String content;
    private LocalDateTime createdAt;
    private List<String> suggestions;
}
