package com.dreamsol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;

    private String roleType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_endpoints", joinColumns = @JoinColumn(name = "roleId"), inverseJoinColumns = @JoinColumn(name = "endPointKey"))
    private List<EndpointMappings> endPoints;

    private boolean status;

    private LocalDateTime timeStamp;

}
