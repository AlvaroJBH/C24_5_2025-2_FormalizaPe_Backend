package com.tecsup.formalizape.chatbot.service;

import com.tecsup.formalizape.chatbot.model.ConversationSummary;
import com.tecsup.formalizape.chatbot.model.Message;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private OpenAiService client;

    private OpenAiService getClient() {
        if (client == null) {
            client = new OpenAiService(apiKey);
        }
        return client;
    }

    /**
     * Construye payload para enviar al modelo:
     * [system] + [resumen] + [últimos mensajes]
     */
    public List<ChatMessage> buildPayload(ConversationSummary summary, List<Message> recentMessages) {
        List<ChatMessage> messages = new ArrayList<>();

        // Mensaje system inicial
        messages.add(new ChatMessage("system", "Eres un asistente útil."));

        // Agregar resumen si existe
        if (summary != null) {
            messages.add(new ChatMessage("system", "Resumen de conversación: " + summary.getSummaryText()));
        }

        // Agregar mensajes recientes en orden ascendente
        recentMessages.stream()
                .sorted((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                .forEach(m -> messages.add(new ChatMessage(m.getRole(), m.getContent())));

        return messages;
    }

    /**
     * Llama al modelo de ChatGPT y devuelve la respuesta de texto
     */
    public String sendChat(List<ChatMessage> messages) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4.1-mini")
                .messages(messages)
                .build();

        List<ChatCompletionChoice> choices = getClient().createChatCompletion(request).getChoices();
        if (choices.isEmpty()) {
            return "";
        }
        return choices.get(0).getMessage().getContent();
    }

    /**
     * Genera un resumen a partir de un conjunto de mensajes
     * Devuelve el texto resumido
     */
    public String generateSummary(List<Message> messages) {
        // Construir prompt para resumir
        StringBuilder prompt = new StringBuilder("Resume la siguiente conversación de manera concisa:\n");
        messages.forEach(m -> prompt.append(m.getRole()).append(": ").append(m.getContent()).append("\n"));

        ChatMessage systemMessage = new ChatMessage("system", "Eres un asistente que resume conversaciones.");
        ChatMessage userMessage = new ChatMessage("user", prompt.toString());

        List<ChatMessage> payload = List.of(systemMessage, userMessage);

        return sendChat(payload);
    }
}
