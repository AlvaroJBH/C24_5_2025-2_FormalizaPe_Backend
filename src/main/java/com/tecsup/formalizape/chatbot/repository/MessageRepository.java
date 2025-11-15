package com.tecsup.formalizape.chatbot.repository;

import com.tecsup.formalizape.chatbot.model.Conversation;
import com.tecsup.formalizape.chatbot.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findTopByConversationOrderByCreatedAtDesc(Conversation conversation, Pageable pageable);
    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);

}
