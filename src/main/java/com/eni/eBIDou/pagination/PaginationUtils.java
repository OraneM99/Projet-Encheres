package com.eni.eBIDou.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;


public class PaginationUtils {
    
    public static <T> Page<T> getPageFromList(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        if (start > list.size() || list.isEmpty()) {
            return new PageImpl<T>(new ArrayList<T>(), pageable, list.size());
        }

        List<T> pageContent = list.subList(start, end);

        return new PageImpl<T>(pageContent, pageable, list.size());
    }
}