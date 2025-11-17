package com.tecsup.formalizape.chatbot.controller;

import com.tecsup.formalizape.chatbot.dto.*;
import com.tecsup.formalizape.chatbot.mapper.ConversationMapper;
import com.tecsup.formalizape.chatbot.mapper.MessageMapper;
import com.tecsup.formalizape.chatbot.model.Conversation;
import com.tecsup.formalizape.chatbot.model.Message;
import com.tecsup.formalizape.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;

    // 1️⃣ Iniciar nueva conversación
    @PostMapping("/conversations")
    public ConversationResponseDTO createConversation(@RequestBody CreateConversationRequestDTO request) {
        Conversation conversation = chatbotService.createConversation(request.getBusinessId(), request.getTitle());
        return conversationMapper.toResponseDTO(conversation);
    }

    // 2️⃣ Enviar mensaje dentro de una conversación
    @PostMapping("/conversations/{conversationId}/messages")
    public MessageResponseDTO sendMessage(@PathVariable Long conversationId,
                                          @RequestBody SendMessageRequestDTO request) {
        return chatbotService.sendUserMessage(conversationId, request.getContent());
    }

    // 3️⃣ Obtener historial de mensajes
    @GetMapping("/conversations/{conversationId}/messages")
    public List<MessageResponseDTO> getMessages(@PathVariable Long conversationId) {
        List<Message> messages = chatbotService.getAllMessages(conversationId);
        return messages.stream()
                .map(messageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 4️⃣ Obtener conversación completa con mensajes (opcional)
    @GetMapping("/conversations/{conversationId}")
    public ConversationWithMessagesDTO getConversation(@PathVariable Long conversationId) {
        Conversation conversation = chatbotService.getConversation(conversationId);
        return conversationMapper.toWithMessagesDTO(conversation);
    }

    // 5️⃣ Listar conversaciones de un Business
    @GetMapping("/business/{businessId}/conversations")
    public List<ConversationResponseDTO> getConversationsByBusiness(@PathVariable Long businessId) {
        List<Conversation> conversations = chatbotService.getConversationsByBusiness(businessId);
        return conversations.stream()
                .map(conversationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
