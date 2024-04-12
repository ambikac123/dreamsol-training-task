package com.dreamsol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Document
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long documentId;

    private String documentOriginalName;

    private String documentDuplicateName;

    private String documentType;

    private long documentSize;

    private boolean status;

    private LocalDateTime timeStamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
