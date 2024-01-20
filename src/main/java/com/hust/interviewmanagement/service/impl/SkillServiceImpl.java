package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Skill;
import com.hust.interviewmanagement.repository.SkillRepository;
import com.hust.interviewmanagement.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    @Override
    public List<Skill> findAllSkill() {
        return skillRepository.findAll();
    }
}
