package com.dreamsol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Permission
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long permissionId;

    private String permissionType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "permission_endpoints", joinColumns = @JoinColumn(name = "permissionId"), inverseJoinColumns = @JoinColumn(name = "endPointKey"))
    private List<Endpoint> endPoints;

    private boolean status;

    private LocalDateTime timeStamp;
}
