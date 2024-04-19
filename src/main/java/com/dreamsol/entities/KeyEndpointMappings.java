package com.dreamsol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class KeyEndpointMappings
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String key;
    private String endpoint;

    public KeyEndpointMappings(String key,String endpoint)
    {
        this.endpoint = key;
        this.endpoint = endpoint;
    }
}
