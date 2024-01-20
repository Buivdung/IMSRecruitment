package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Level;
import com.hust.interviewmanagement.repository.LevelRepository;
import com.hust.interviewmanagement.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {
    private final LevelRepository levelRepository;

    @Override
    public List<Level> findAllLevel() {
        return levelRepository.findAll();
    }
}
