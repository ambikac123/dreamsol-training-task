package com.dreamsol.response;

import com.dreamsol.helpers.PageInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AllDataResponse
{
    private List<?> contents;
    private PageInfo pageInfo;
}
