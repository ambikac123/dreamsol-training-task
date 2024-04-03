package com.dreamsol.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DocumentSingleDataResponseDto
{
    private String documentName;

    private String documentType;

    private long documentSize;

    private boolean status;

    private LocalDateTime timeStamp;

}
