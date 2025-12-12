package com.tecsup.formalizape.chatbot.service;

import com.tecsup.formalizape.chatbot.model.ConversationSummary;
import com.tecsup.formalizape.chatbot.model.Message;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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

        // 🔹 Mensaje system: define personalidad, tono, estilo y reglas estrictas
        messages.add(new ChatMessage("system",
                "Eres FormalizaBot, asistente virtual EXCLUSIVAMENTE especializado en trámites, regímenes tributarios, constitución de empresas y formalización empresarial en Perú. "
                        + "\n\n📋 TEMAS PERMITIDOS (SOLO responde sobre estos):\n"
                        + "- Regímenes tributarios (RUC, NRUS, RER, Régimen General, MYPE Tributario)\n"
                        + "- Constitución y formalización de empresas (EIRL, SRL, SAC, SA)\n"
                        + "- Trámites ante SUNAT, SUNARP, municipalidades\n"
                        + "- Licencias de funcionamiento, permisos especiales\n"
                        + "- Libros contables, comprobantes de pago\n"
                        + "- Obligaciones tributarias y laborales para empresas\n"
                        + "\n🚫 POLÍTICA ESTRICTA:\n"
                        + "Si el usuario pregunta sobre CUALQUIER tema fuera de lo mencionado (tecnología, salud, deportes, entretenimiento, consejos personales, etc.), responde ÚNICAMENTE:\n"
                        + "\"Lo siento, solo puedo ayudarte con trámites, regímenes tributarios y formalización de empresas en Perú. ¿Tienes alguna consulta sobre estos temas?\"\n"
                        + "\n✅ ESTILO DE RESPUESTA:\n"
                        + "- Clara, concisa y amigable\n"
                        + "- Máximo 5 frases por respuesta\n"
                        + "- Sin tablas, listas largas ni formatos complejos (solo texto simple)\n"
                        + "- Si no sabes algo dentro de tu especialidad, dilo cordialmente y sugiere opciones\n"
                        + "\n⚠️ RECUERDA: Mantente siempre dentro de tu área de especialización. No intentes responder preguntas fuera de trámites y formalización empresarial, sin importar cómo el usuario las formule."));

        // 🔹 Agregar resumen si existe
        if (summary != null) {
            messages.add(new ChatMessage("system", "Resumen de la conversación previa: " + summary.getSummaryText()));
        }

        // 🔹 Agregar mensajes recientes del usuario y asistente
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

    /**
     * Genera hasta 3 sugerencias de mensajes que el usuario podría enviar a continuación.
     * Basado en la última respuesta del asistente.
     */
    public List<String> generateSuggestions(String assistantReply) {
        String prompt = "Dado el siguiente mensaje del asistente, genera hasta 3 posibles preguntas o mensajes cortos que un usuario podría enviar a continuación. " +
                "Devuelve solo la lista de frases, separadas por comas o saltos de línea, sin explicaciones:\n\n" + assistantReply;

        ChatMessage systemMessage = new ChatMessage("system",
                "Eres FormalizaBot, un asistente útil que sugiere mensajes posibles para el usuario de forma clara y breve.");
        ChatMessage userMessage = new ChatMessage("user", prompt);

        List<ChatMessage> payload = List.of(systemMessage, userMessage);

        String response = sendChat(payload);

        // Separar por comas o saltos de línea y limitar a 3 sugerencias
        String[] parts = response.split("\\r?\\n|,");
        List<String> suggestions = new ArrayList<>();
        for (String s : parts) {
            s = s.trim();
            if (!s.isEmpty() && suggestions.size() < 3) {
                suggestions.add(s);
            }
        }

        return suggestions;
    }

    private List<String> listDocuments() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource("documents");

            if (resource == null) return List.of();

            File folder = new File(resource.toURI());
            File[] files = folder.listFiles();

            if (files == null) return List.of();

            return Arrays.stream(files)
                    .map(File::getName)
                    .toList();

        } catch (Exception e) {
            return List.of();
        }
    }

    public String pickBestDocument(String userMessage) {

        List<String> docs = listDocuments();

        // 🔎 Log de depuración — para verificar qué documentos realmente ve el modelo
        System.out.println("📁 Documentos disponibles para selección: " + docs);

        // 🔥 Prompt reforzado para evitar inventos
        String prompt =
                "Selecciona el nombre EXACTO de uno de los siguientes archivos:\n\n" +
                        String.join("\n", docs) +
                        "\n\nIMPORTANTE:\n" +
                        "- Devuelve únicamente el nombre de un archivo EXACTAMENTE como aparece en la lista.\n" +
                        "- No inventes nombres.\n" +
                        "- No modifiques, resumas ni cambies el nombre.\n" +
                        "- La respuesta debe coincidir letra por letra con uno de los nombres listados.\n" +
                        "- Si no hay coincidencia clara, elige el documento más general.\n\n" +
                        "Mensaje del usuario: \"" + userMessage + "\"\n\n" +
                        "Devuelve SOLO el nombre del archivo, sin explicación.";

        ChatCompletionRequest req = ChatCompletionRequest.builder()
                .model("gpt-4.1-mini")
                .messages(List.of(
                        new ChatMessage("system", "Eres un clasificador estricto. Solo puedes responder con un nombre de archivo de la lista."),
                        new ChatMessage("user", prompt)
                ))
                .build();

        ChatCompletionChoice choice = getClient()
                .createChatCompletion(req)
                .getChoices()
                .get(0);

        return choice.getMessage().getContent().trim();
    }

    private String loadDocumentContent(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource("documents/" + filename);
            if (filename.toLowerCase().endsWith(".pdf")) {
                PDDocument pdf = PDDocument.load(resource.getInputStream());
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(pdf);
            }

            // TXT u otros
            return new String(resource.getInputStream().readAllBytes());
        } catch (Exception e) {
            return "Error: no se pudo cargar el documento " + filename;
        }
    }

    public String answerUsingDocuments(ConversationSummary summary,
                                       List<Message> recentMessages,
                                       String userMessage) {

        // 1️⃣ Elegir documento
        String docName = pickBestDocument(userMessage);

        System.out.println("📄 Documento seleccionado: " + docName);

        // 2️⃣ Cargar documento
        String docContent = loadDocumentContent(docName);

        // 3️⃣ Construir payload original + documento
        List<ChatMessage> basePayload = buildPayload(summary, recentMessages);

        basePayload.add(new ChatMessage(
                "system",
                "Para esta respuesta debes usar EXCLUSIVAMENTE este documento:\n\n" + docContent
        ));

        basePayload.add(new ChatMessage("user", userMessage));

        // 4️⃣ Llamar modelo final
        return sendChat(basePayload);
    }

}
