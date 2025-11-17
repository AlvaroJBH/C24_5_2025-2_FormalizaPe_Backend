package com.tecsup.formalizape.chatbot.repository;

import com.tecsup.formalizape.chatbot.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByBusinessIdOrderByCreatedAtDesc(Long businessId);
}