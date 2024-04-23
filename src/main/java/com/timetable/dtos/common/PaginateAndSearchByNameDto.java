package com.timetable.dtos.common;

import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaginateAndSearchByNameDto {
    @Nullable
    private String name;
    @Nullable
    private Integer page;
    @Nullable
    private Integer size;

    public PaginateAndSearchByNameDto(String name, Integer page, Integer size) {
        this.name = name;
        this.page = (page != null && page > 0) ? page : 1;
        this.size = (size != null && size > 0) ? size : 10;
    }
}
