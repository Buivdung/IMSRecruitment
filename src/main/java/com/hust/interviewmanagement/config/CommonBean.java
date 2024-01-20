package com.hust.interviewmanagement.config;

import com.hust.interviewmanagement.entities.Candidate;
import com.hust.interviewmanagement.entities.InterviewSchedule;
import com.hust.interviewmanagement.entities.Offer;
import com.hust.interviewmanagement.entities.ResultInterview;
import com.hust.interviewmanagement.web.request.*;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Objects;
import java.util.concurrent.Executor;

import static org.modelmapper.Conditions.isNotNull;

@Configuration
public class CommonBean {

    private static final ExpressionMap<InterviewRequest, InterviewSchedule> MAPPER_INTERVIEW = mapper ->
    {
        mapper.skip(InterviewSchedule::setResultInterviews);
        mapper.skip(InterviewSchedule::setInterviewer);
    };
    private static final ExpressionMap<ResultRequest, ResultInterview> MAPPER_RESULT = mapper ->
    {
        mapper.skip(ResultInterview::setCandidate);
        mapper.skip(ResultInterview::setOffer);
        mapper.skip(ResultInterview::setInterviewSchedule);
    };
    private static final ExpressionMap<CandidateRequest, Candidate> MAPPER_CANDIDATE = mapper -> {
        mapper.skip(Candidate::setCv);
        mapper.skip(Candidate::setCvId);
    };
    private static final ExpressionMap<OfferRequest, Offer> MAPPER_OFFER = mapper -> {
        mapper.skip(Offer::setResultInterview);
        mapper.skip(Offer::setDepartment);
        mapper.skip(Offer::setManager);
        mapper.skip(Offer::setRecruiter);
    };
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(CandidateRequest.class, Candidate.class).addMappings(MAPPER_CANDIDATE);
        modelMapper.createTypeMap(ResultRequest.class, ResultInterview.class).addMappings(MAPPER_RESULT);
        modelMapper.createTypeMap(OfferRequest.class, Offer.class).addMappings(MAPPER_OFFER);
        modelMapper.createTypeMap(InterviewRequest.class, InterviewSchedule.class).addMappings(MAPPER_INTERVIEW);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("UserThread -");
        executor.initialize();
        return executor;
    }

}
