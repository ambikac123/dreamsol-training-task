package com.dreamsol.repositories;

import com.dreamsol.entities.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndpointRepository extends JpaRepository<Endpoint,String>
{

}
