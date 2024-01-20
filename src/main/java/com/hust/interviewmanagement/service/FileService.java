package com.hust.interviewmanagement.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService<T> {
    List<T> importExcel(MultipartFile file, Class<T> clazz);
    void export(HttpServletResponse resp, List<T> t) throws IOException;
    void saveFile(MultipartFile file, Long id) throws IOException;
    byte[] downloadFile(Long id, String fileName) throws IOException;
}
