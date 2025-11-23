package com.pnc.project.dto.response;

import java.util.List;

public record Pagination<T>(
        List<T> data,
        int page,
        int size,
        int pages,
        long total) {
}