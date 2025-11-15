package com.tecsup.formalizape.chatbot.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "messages")
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(nullable = false)
    private String role; // user / assistant / system

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}
