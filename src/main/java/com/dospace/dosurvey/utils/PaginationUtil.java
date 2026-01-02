package com.dospace.dosurvey.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtil {

    private static final int DEFAULT_PAGE = 0;
    private static final int MIN_SIZE = 10;

    public static Pageable createPageable(int page, int size, Sort sort) {
        int validatedPage = Math.max(page, DEFAULT_PAGE);
        int validatedSize = Math.max(size, MIN_SIZE);
        return PageRequest.of(validatedPage, validatedSize, sort);
    }

    public static int validatePage(int page) {
        return Math.max(page, DEFAULT_PAGE);
    }

    public static int validateSize(int size) {
        return Math.max(size, MIN_SIZE);
    }
}
