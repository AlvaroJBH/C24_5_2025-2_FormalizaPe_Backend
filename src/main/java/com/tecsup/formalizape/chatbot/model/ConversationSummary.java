package com.tecsup.formalizape.chatbot.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "conversation_summaries")
public class ConversationSummary extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String summaryText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id")
    private Message lastMessage;
}
