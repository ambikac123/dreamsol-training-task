package com.dreamsol.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo
{
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean firstPage;
    private boolean lastPage;
}
