package com.hust.interviewmanagement.utils;

import com.hust.interviewmanagement.web.request.JobSearch;
import com.hust.interviewmanagement.web.request.SearchRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchUtil {
    public SearchRequest getSearchRequest(SearchRequest searchRequest) {
        if (searchRequest.getPageNumber() == null) {
            searchRequest.setPageNumber(1);
        }
        if (searchRequest.getParam() == null || searchRequest.getParam().isEmpty()) {
            searchRequest.setParam("");
        }
        if (searchRequest.getInterviewer() == null || searchRequest.getInterviewer().isEmpty()) {
            searchRequest.setInterviewer("");
        }
        if (searchRequest.getDepartment() == null || searchRequest.getDepartment().isEmpty()) {
            searchRequest.setDepartment("");
        }
        return searchRequest;
    }

    public JobSearch getJobRequest(JobSearch jobSearch) {
        if (jobSearch.getPageNumber() == null) {
            jobSearch.setPageNumber(1);
        }
        if (jobSearch.getParam() == null || jobSearch.getParam().isEmpty()) {
            jobSearch.setParam("");
        }
        if (jobSearch.getLevel() == null || jobSearch.getLevel().isEmpty()) {
            jobSearch.setLevel("");
        }
        if (jobSearch.getAddress() == null || jobSearch.getAddress().isEmpty()) {
            jobSearch.setAddress("");
        }
        return jobSearch;
    }

    public SearchRequest setPageMax(int pageMax, SearchRequest searchRequest) {
        List<Integer> integers = new ArrayList<>();
        if (pageMax > 0) {
            for (int i = 0; i < pageMax; i++) {
                integers.add(i);
            }
            searchRequest.setPageMaxNumber(integers);
        }
        return searchRequest;
    }

    public JobSearch setPageMax(int pageMax, JobSearch jobSearch) {
        if (pageMax > 0) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < pageMax; i++) {
                integers.add(i);
            }
            jobSearch.setPageMaxNumber(integers);
        }
        return jobSearch;
    }
}
