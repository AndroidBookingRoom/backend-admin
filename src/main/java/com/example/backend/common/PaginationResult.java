package com.example.backend.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Nhan Nguyen on 5/20/2021
 *
 * @author Nhan Nguyen
 * @date 5/20/2021
 */

@Setter
@Getter
public class PaginationResult<T> {
    private int totalRecords;
    private int currentPage;
    private List<T> data;
    private int maxResult;
    private int totalPages;
    private int first;

    public PaginationResult() {
    }

}