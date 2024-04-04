package com.dreamsol.services.impl;

import com.dreamsol.dto.DocumentSingleDataResponseDto;
import com.dreamsol.entities.Document;
import com.dreamsol.entities.User;
import com.dreamsol.repositories.DocumentRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class DocumentServiceImpl implements DocumentService
{
    private DocumentRepository documentRepository;
    @Override
    public ResponseEntity<?> uploadDocument(MultipartFile file,User user,String filePath)
    {
        try {
            Document document = saveDocument(file,filePath);
            document.setUser(user);
            document.setTimeStamp(LocalDateTime.now());
            documentRepository.save(document);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("document successfully uploaded!",true));
        }catch(Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    /*---------------------------Document Helper Methods-----------------------------------------*/

    public Document saveDocument(MultipartFile file,String filePath)
    {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
            String randomID = UUID.randomUUID().toString();
            String duplicateFileName = randomID + fileExtension;
            String duplicateFilePath = filePath.concat(duplicateFileName);
            Files.copy(file.getInputStream(), Paths.get(duplicateFilePath));

            Document document = new Document();
            document.setDocumentOriginalName(originalFilename);
            document.setDocumentDuplicateName(duplicateFileName);
            document.setDocumentType(file.getContentType());
            document.setDocumentSize(file.getSize());
            document.setStatus(true);
            return document;
        }catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
    public DocumentSingleDataResponseDto documentToDocumentSingleDataResponseDto(Document document)
    {
        DocumentSingleDataResponseDto documentSingleDataResponseDto = new DocumentSingleDataResponseDto();
        BeanUtils.copyProperties(document,documentSingleDataResponseDto);
        String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/download-file/")
                .path(document.getDocumentDuplicateName())
                .toUriString();
        documentSingleDataResponseDto.setDocumentName(downloadLink);
        return documentSingleDataResponseDto;
    }
}
