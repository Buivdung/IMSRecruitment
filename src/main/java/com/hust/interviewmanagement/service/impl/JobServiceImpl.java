package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Job;
import com.hust.interviewmanagement.entities.Level;
import com.hust.interviewmanagement.entities.Skill;
import com.hust.interviewmanagement.entities.SkillJob;
import com.hust.interviewmanagement.enums.ELabelCommon;
import com.hust.interviewmanagement.enums.EMessageJob;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.repository.JobRepository;
import com.hust.interviewmanagement.repository.LevelRepository;
import com.hust.interviewmanagement.repository.SkillJobRepository;
import com.hust.interviewmanagement.repository.SkillRepository;
import com.hust.interviewmanagement.service.EmailService;
import com.hust.interviewmanagement.service.FileService;
import com.hust.interviewmanagement.service.JobService;
import com.hust.interviewmanagement.web.request.JobImport;
import com.hust.interviewmanagement.web.request.JobRequest;
import com.hust.interviewmanagement.web.request.JobSearch;
import com.hust.interviewmanagement.web.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final FileService<JobImport> fileService;
    private final ModelMapper modelMapper;
    private final LevelRepository levelRepository;
    private final SkillJobRepository skillJobRepository;

    @Override
    @Transactional
    public Job saveJob(JobRequest jobRequest) {
        Job job = mapJob(new Job(), jobRequest);
        job.setInterviewed(0L);
        if (jobRepository.existsJobByTitleAndLevelAndStatus(job.getTitle(), job.getLevel(), EStatus.OPEN)) {
            return null;
        }
        return jobRepository.save(job);
    }

    @Override
    public Page<Job> findAllJob(SearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPageNumber() - 1,
                SearchRequest.PAGE_SIZE);
        if (searchRequest.getStatus() != null) {
            return jobRepository.findAllJob(
                    searchRequest.getParam(),
                    searchRequest.getStatus(),
                    pageable);
        }
        return jobRepository.findAllJob(searchRequest.getParam(), pageable);
    }

    @Override
    public Page<Job> findAllJob(JobSearch jobSearch) {
        Pageable pageable = PageRequest.of(jobSearch.getPageNumber() - 1,
                JobSearch.PAGE_SIZE);
        return jobRepository.findAllJob(
                jobSearch.getParam(),
                jobSearch.getLevel(),
                jobSearch.getAddress(),
                pageable);
    }

    @Override
    public void deleteJobById(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Job findJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(EMessageJob.DONT_JOB.getValue()));
    }

    @Override
    public List<Job> findJobByStatusOpen() {
        return jobRepository.findAllByStatus(EStatus.OPEN);
    }

    @Override
    public List<Job> findJobByStatusOpenAndApply() {
        return jobRepository.findAllByStatusAndApply(EStatus.OPEN);
    }

    @Override
    @Transactional
    public Job updateJob(Job job, JobRequest jobRequest) {
        if (jobRepository.existsJobByTitleAndLevel_IdAndStatusAndIdNot(
                jobRequest.getTitle(),
                jobRequest.getLevel(),
                EStatus.OPEN,
                jobRequest.getId())) {
            return null;
        }
        skillJobRepository.deleteSkillJob(jobRequest.getId());
        Job jobMap = mapJob(job, jobRequest);
        return jobRepository.save(jobMap);
    }

    @Override
    public List<Job> saveAllJobs(MultipartFile fileImport) {
        return null;
    }

    @Override
    public Page<Job> findNewJob() {
        Pageable pageable = PageRequest.of(0, 9);
        return jobRepository.findAll(pageable);
    }

    private Job mapJob(Job job, JobRequest jobRequest) {
        modelMapper.map(jobRequest, job);
        Level level = levelRepository.findById(jobRequest.getLevel()).orElseThrow();
        List<Skill> skills = skillRepository.findAllById(jobRequest.getSkills());
        List<SkillJob> skillJobs = getSkillJobs(skills, job);
        job.setLevel(level);
        job.setStatus(EStatus.OPEN);
        job.setSkillJobs(skillJobs);
        job.setApply(0L);
        return job;
    }

    private List<Job> getJobs(MultipartFile file) {
        List<Job> jobs = new ArrayList<>();
        List<JobImport> jobImports = fileService.importExcel(file, JobImport.class);
        for (JobImport j : jobImports) {
            Job job = modelMapper.map(j, Job.class);
            Level level = levelRepository.findAllByName(j.getLevel()).orElseThrow();
            List<Skill> skills = getSkills(j.getSkills());
            List<SkillJob> skillJobs = getSkillJobs(skills, job);
            job.setStatus(EStatus.OPEN);
            job.setLevel(level);
            job.setSkillJobs(skillJobs);
            job.setApply(0L);
            jobs.add(job);
        }
        return jobs;
    }


    private List<Skill> getSkills(String skillName) {
        String[] arr = skillName.split(",");
        List<String> skillNames = Arrays.stream(arr).map(String::trim).toList();
        return skillRepository.findAllByNameIn(skillNames);
    }


    private List<SkillJob> getSkillJobs(List<Skill> skills, Job job) {

        List<SkillJob> skillJobs = new ArrayList<>();
        for (Skill s : skills) {
            skillJobs.add(SkillJob
                    .builder()
                    .skill(s)
                    .job(job)
                    .build()
            );
        }
        return skillJobs;
    }
}
