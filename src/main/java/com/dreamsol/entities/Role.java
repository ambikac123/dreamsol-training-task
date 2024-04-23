package com.dreamsol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;

    private String roleType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_endpoints", joinColumns = @JoinColumn(name = "roleId"), inverseJoinColumns = @JoinColumn(name = "endPointKey"))
    private List<Endpoint> endPoints;

    private boolean status;

    private LocalDateTime timeStamp;

}
