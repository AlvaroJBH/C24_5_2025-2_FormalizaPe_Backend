package com.tecsup.formalizape.chatbot.repository;

import com.tecsup.formalizape.chatbot.model.Conversation;
import com.tecsup.formalizape.chatbot.model.ConversationSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationSummaryRepository extends JpaRepository<ConversationSummary, Long> {

    Optional<ConversationSummary> findTopByConversationOrderByCreatedAtDesc(Conversation conversation);
}
