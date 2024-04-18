package com.dreamsol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Permission
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long permissionId;
    private String permissionType;
    private List<String> endPoints;
    private boolean status;
    private LocalDateTime timeStamp;
}
