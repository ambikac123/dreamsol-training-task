package com.dreamsol.services.impl;

import com.dreamsol.dto.EndpointResponseDto;
import com.dreamsol.entities.Endpoint;
import com.dreamsol.helpers.EndpointMappingsHelper;
import com.dreamsol.repositories.EndpointRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EndpointServiceImpl implements EndpointService
{
    @Autowired private EndpointRepository endpointRepository;
    @Autowired private EndpointMappingsHelper endpointMappingsHelper;
    @Override
    public ResponseEntity<?> getAllEndpoints()
    {
        List<EndpointResponseDto> endpointResponseDtoList = endpointRepository.findAll()
                .stream()
                .map((endpointMappings -> new EndpointResponseDto(endpointMappings.getEndPointKey(), endpointMappings.getEndPointLink())))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(endpointResponseDtoList);
    }

    @Override
    public ResponseEntity<?> updateEndpoints()
    {
        int count = 0;
        Map<String,String> endpointMap1 = endpointMappingsHelper.getEndpointMap();
        for(Map.Entry<String,String> endpoint : endpointMap1.entrySet())
        {
            Endpoint endpointMappings = new Endpoint(endpoint.getKey(),endpoint.getValue());
            endpointRepository.save(endpointMappings);
            count++;
        }
        Map<String,String> endpointMap2 = endpointMappingsHelper.getRoleEndpointMap();
        for(Map.Entry<String,String> endpoint : endpointMap2.entrySet())
        {
            Endpoint endpointMappings = new Endpoint(endpoint.getKey(),endpoint.getValue());
            endpointRepository.save(endpointMappings);
            count++;
        }
        Map<String,String> endpointMap3 = endpointMappingsHelper.getPermissionEndpointMap();
        for(Map.Entry<String,String> endpoint : endpointMap3.entrySet())
        {
            Endpoint endpointMappings = new Endpoint(endpoint.getKey(),endpoint.getValue());
            endpointRepository.save(endpointMappings);
            count++;
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(count+" endpoints updated!",true));
    }

    @Override
    public ResponseEntity<?> getRoleRelatedEndpoints() {
        return ResponseEntity.status(HttpStatus.OK).body(endpointMappingsHelper.getRoleEndpointMap().keySet());
    }

    @Override
    public ResponseEntity<?> getPermissionRelatedEndpoints() {
        return ResponseEntity.status(HttpStatus.OK).body(endpointMappingsHelper.getPermissionEndpointMap().keySet());
    }
}
