package com.dreamsol.services;

import com.dreamsol.dto.DocumentSingleDataResponseDto;
import com.dreamsol.entities.Document;
import com.dreamsol.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService
{
    ResponseEntity<?> uploadDocument(MultipartFile file, User user,String filePath);

    /* ----------------------- Document Helper Methods -----------------*/
    Document saveDocument(MultipartFile file,String filePath);
    DocumentSingleDataResponseDto documentToDocumentSingleDataResponseDto(Document document);
}
/*
    ResponseEntity<?> updateDocument(MultipartFile file,Long documentId);
    ResponseEntity<?> deleteDocument(Long documentId);
    ResponseEntity<Resource> downloadDocument(Long documentId);
*/