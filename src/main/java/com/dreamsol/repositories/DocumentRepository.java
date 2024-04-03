package com.dreamsol.repositories;

import com.dreamsol.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long>
{
    Document findByDocumentDuplicateName(String fileName);
}
