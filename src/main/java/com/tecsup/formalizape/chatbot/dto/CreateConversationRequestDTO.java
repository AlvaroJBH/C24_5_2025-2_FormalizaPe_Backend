package com.tecsup.formalizape.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateConversationRequestDTO {
    private Long businessId;
    private String title; // opcional
}
