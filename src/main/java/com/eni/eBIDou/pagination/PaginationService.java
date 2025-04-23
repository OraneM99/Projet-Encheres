package com.eni.eBIDou.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PaginationService {
    
    public Pageable createPageable(int page, int size, String sortProperty) {
        // Conversion de 1-based (UI) à 0-based (Spring Data)
        return PageRequest.of(page - 1, size, Sort.by(sortProperty).descending());
    }

    // Surcharge pour créer un Pageable avec un tri ascendant
    public Pageable createPageableAsc(int page, int size, String sortProperty) {
        return PageRequest.of(page - 1, size, Sort.by(sortProperty).ascending());
    }

    // Convertit un objet Page de Spring Data en PaginatedResult
    public <T> PaginatedResult<T> createPaginatedResult(Page<T> page) {
        return new PaginatedResult<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}