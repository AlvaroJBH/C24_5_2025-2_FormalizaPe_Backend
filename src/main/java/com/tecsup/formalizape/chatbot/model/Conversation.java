package com.tecsup.formalizape.chatbot.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import com.tecsup.formalizape.formalization.model.Business;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "conversations")
public class Conversation extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConversationSummary> summaries;
}
