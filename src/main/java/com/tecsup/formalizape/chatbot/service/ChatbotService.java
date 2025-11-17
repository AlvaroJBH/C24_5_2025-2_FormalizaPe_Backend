package com.tecsup.formalizape.chatbot.service;

import com.tecsup.formalizape.chatbot.dto.MessageResponseDTO;
import com.tecsup.formalizape.chatbot.model.Conversation;
import com.tecsup.formalizape.chatbot.model.ConversationSummary;
import com.tecsup.formalizape.chatbot.model.Message;
import com.tecsup.formalizape.chatbot.repository.ConversationRepository;
import com.tecsup.formalizape.chatbot.repository.ConversationSummaryRepository;
import com.tecsup.formalizape.chatbot.repository.MessageRepository;
import com.tecsup.formalizape.formalization.model.Business;
import com.tecsup.formalizape.formalization.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final ConversationRepository conversationRepo;
    private final MessageRepository messageRepo;
    private final ConversationSummaryRepository summaryRepo;
    private final BusinessRepository businessRepo;
    private final OpenAIService openAIService;

    private static final int MAX_RECENT_MESSAGES = 10; // últimos N mensajes
    private static final int SUMMARY_THRESHOLD = 20;    // generar resumen cada N mensajes

    @Transactional
    public MessageResponseDTO sendUserMessage(Long conversationId, String userContent) {
        Conversation conversation = conversationRepo.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // 1️⃣ Guardar mensaje del usuario
        Message userMessage = Message.builder()
                .conversation(conversation)
                .role("user")
                .content(userContent)
                .build();
        messageRepo.save(userMessage);

        // 2️⃣ Traer últimos mensajes recientes
        List<Message> recentMessages = messageRepo.findTopByConversationOrderByCreatedAtDesc(
                conversation, PageRequest.of(0, MAX_RECENT_MESSAGES));

        // 3️⃣ Traer último resumen si existe
        ConversationSummary lastSummary = summaryRepo.findTopByConversationOrderByCreatedAtDesc(conversation).orElse(null);

        // 4️⃣ Construir payload y llamar a OpenAI
        List<com.theokanning.openai.completion.chat.ChatMessage> payload =
                openAIService.buildPayload(lastSummary, recentMessages);

        String assistantResponse = openAIService.sendChat(payload);

        // 5️⃣ Guardar respuesta del asistente
        Message assistantMessage = Message.builder()
                .conversation(conversation)
                .role("assistant")
                .content(assistantResponse)
                .build();
        messageRepo.save(assistantMessage);

        // 6️⃣ Generar sugerencias
        List<String> suggestions = openAIService.generateSuggestions(assistantResponse);

        // 7️⃣ Generar resumen si corresponde
        maybeUpdateSummary(conversation);

        // 8️⃣ Mapear al DTO y agregar sugerencias
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setRole(assistantMessage.getRole());
        dto.setContent(assistantMessage.getContent());
        dto.setCreatedAt(assistantMessage.getCreatedAt());
        dto.setSuggestions(suggestions);

        return dto;
    }

    /**
     * Genera o actualiza resumen si hay suficientes mensajes nuevos
     */
    @Transactional
    public void maybeUpdateSummary(Conversation conversation) {
        List<Message> allMessages = messageRepo.findTopByConversationOrderByCreatedAtDesc(
                conversation, PageRequest.of(0, SUMMARY_THRESHOLD));

        // Contamos mensajes desde último resumen
        ConversationSummary lastSummary = summaryRepo.findTopByConversationOrderByCreatedAtDesc(conversation).orElse(null);
        int messagesSinceLastSummary = lastSummary == null ? allMessages.size()
                : (int) allMessages.stream()
                .filter(m -> m.getCreatedAt().isAfter(lastSummary.getLastMessage().getCreatedAt()))
                .count();

        if (messagesSinceLastSummary >= SUMMARY_THRESHOLD) {
            String summaryText = openAIService.generateSummary(allMessages);
            ConversationSummary newSummary = ConversationSummary.builder()
                    .conversation(conversation)
                    .summaryText(summaryText)
                    .lastMessage(allMessages.get(0)) // más reciente
                    .build();
            summaryRepo.save(newSummary);
        }
    }

    @Transactional
    public Conversation createConversation(Long businessId, String title) {
        Conversation conversation = new Conversation();
        conversation.setTitle(title);

        // Buscar el business por ID y asociarlo
        Business business = businessRepo.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));
        conversation.setBusiness(business);

        return conversationRepo.save(conversation);
    }

    @Transactional(readOnly = true)
    public List<Message> getAllMessages(Long conversationId) {
        Conversation conversation = conversationRepo.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        return messageRepo.findByConversationOrderByCreatedAtAsc(conversation);
    }

    @Transactional(readOnly = true)
    public Conversation getConversation(Long conversationId) {
        return conversationRepo.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    @Transactional(readOnly = true)
    public List<Conversation> getConversationsByBusiness(Long businessId) {
        return conversationRepo.findByBusinessIdOrderByCreatedAtDesc(businessId);
    }

}
