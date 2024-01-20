package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.repository.*;
import com.hust.interviewmanagement.service.EmailService;
import com.hust.interviewmanagement.service.InterviewService;
import com.hust.interviewmanagement.web.request.InterviewRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final InterviewerScheduleRepository interviewerScheduleRepository;
    private final ResultInterviewRepository resultInterviewRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final OfferRepository offerRepository;

    @Override
    @Transactional
    public InterviewSchedule saveInterviewSchedule(InterviewRequest interviewRequest) throws MessagingException {
        InterviewSchedule interviewSchedule = interviewScheduleRepository
                .save(getInterviewSchedule(new InterviewSchedule(), interviewRequest));
        Job job = jobRepository.findById(interviewRequest.getJobId()).orElseThrow();
        job.setInterviewed(job.getInterviewed() + interviewSchedule.getResultInterviews().size());
        List<String> emails = interviewSchedule.getResultInterviews().stream()
                .map(x -> x.getCandidate().getEmail()).toList();
        emailService.sendMailNotificationInterviewSchedule(List.of("dungbv201098@gmail.com"),"THƯ MỜI PHỎNG VẤN",interviewSchedule);
        return interviewSchedule;
    }

    @Override
    public Page<InterviewSchedule> findAllInterviewSchedule(SearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPageNumber() - 1, SearchRequest.PAGE_SIZE);
        if (searchRequest.getStatus() == null) {
            return interviewScheduleRepository.findAll(
                    searchRequest.getParam(),
                    searchRequest.getInterviewer(),
                    pageable
            );
        }
        return interviewScheduleRepository.findAll(
                searchRequest.getParam(),
                searchRequest.getInterviewer(),
                searchRequest.getStatus(),
                pageable
        );
    }

    @Override
    public InterviewSchedule findInterviewScheduleById(Long id) {
        return interviewScheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("K có"));
    }

    @Override
    @Transactional
    public InterviewSchedule updateInterviewSchedule(InterviewRequest interviewRequest) throws MessagingException {
        InterviewSchedule interviewSchedule = findInterviewScheduleById(interviewRequest.getId());
        interviewerScheduleRepository.deleteBySchedule_Id(interviewSchedule.getId());
        InterviewSchedule interviewSchedule1 = interviewScheduleRepository.save(checkUpdate(interviewSchedule, interviewRequest));
        emailService.sendMailChangeInterviewSchedule(List.of("dungbv201098@gmail.com"),
                "THƯ THAY ĐỔI LỊCH PHỎNG VẤN",
                interviewSchedule);
        return  interviewSchedule1;
    }

    @Override
    @Transactional
    public void deleteInterviewSchedule(Long id) throws MessagingException {
        InterviewSchedule interviewSchedule = findInterviewScheduleById(id);
        Job job = jobRepository.findById(interviewSchedule.getResultInterviews().get(0).getCandidate().getJob().getId())
                .orElseThrow();
        job.setInterviewed(job.getInterviewed() - interviewSchedule.getResultInterviews().size());
        List<Candidate> candidates = interviewSchedule
                .getResultInterviews()
                .stream()
                .map(ResultInterview::getCandidate)
                .toList();
        List<String> emailCandidate = candidates.stream().map(Candidate::getEmail).toList();
        List<String> emailInterviewer = interviewSchedule
                .getInterviewer()
                .stream()
                .map(InterviewerSchedule::getInterviewer)
                .map(Users::getAccount)
                .map(Account::getEmail)
                .toList();
        List<Long> offerIds = interviewSchedule
                .getResultInterviews()
                .stream()
                .map(ResultInterview::getOffer)
                .filter(Objects::nonNull)
                .map(Offer::getId)
                .toList();
        offerRepository.deleteByOfferId(offerIds);
        resultInterviewRepository.deleteByInterviewSchedule_Id(id);
        interviewerScheduleRepository.deleteBySchedule_Id(id);
        interviewScheduleRepository.deleteByInterviewScheduleID(id);
        candidates.forEach(x -> x.setStatus(EStatus.OPEN));
        candidateRepository.saveAll(candidates);
        emailService.sendMailCancelInterviewSchedule(
                List.of("dungbv201098@gmail.com"),
                "THƯ HỦY LỊCH PHỎNG VẤN",
                interviewSchedule);
//        emailService.sendMailNotificationInterviewSchedule(
//                emailInterviewer,
//                "Cancel interviewSchedule",
//                interviewSchedule);
    }

    private InterviewSchedule getInterviewSchedule(InterviewSchedule interviewSchedule,
                                                   InterviewRequest interviewRequest) {
        modelMapper.map(interviewRequest, interviewSchedule);
        List<ResultInterview> resultInterviews = getResult(interviewSchedule, interviewRequest);
        List<InterviewerSchedule> interviewerSchedules = getInterviewers(interviewSchedule, interviewRequest);
        interviewSchedule.setCandidateNumber((long) resultInterviews.size());
        interviewSchedule.setInterviewer(interviewerSchedules);
        interviewSchedule.setResultInterviews(resultInterviews);
        return interviewSchedule;
    }

    private InterviewSchedule checkUpdate(InterviewSchedule interviewSchedule,
                                          InterviewRequest interviewRequest) {
        modelMapper.map(interviewRequest, interviewSchedule);
        List<InterviewerSchedule> interviewerSchedules = getInterviewers(interviewSchedule, interviewRequest);
        interviewSchedule.setInterviewer(interviewerSchedules);
        return interviewSchedule;
    }

    private List<ResultInterview> getResult(InterviewSchedule interviewSchedule,
                                            InterviewRequest interviewRequest) {
        List<ResultInterview> resultInterviews = new ArrayList<>();
        List<Candidate> candidates = candidateRepository.findAllByJob_IdOrOrderByIdAsc(
                interviewRequest.getJobId(), interviewRequest.getCandidateNumber());
        for (Candidate c : candidates) {
            c.setStatus(EStatus.WAITING_FOR_INTERVIEW);
            ResultInterview resultInterview = ResultInterview
                    .builder()
                    .candidate(c)
                    .interviewSchedule(interviewSchedule)
                    .result(EStatus.NA)
                    .note("N/A")
                    .build();
            resultInterviews.add(resultInterview);
        }
        return resultInterviews;
    }

    private List<InterviewerSchedule> getInterviewers(InterviewSchedule interviewSchedule,
                                                      InterviewRequest interviewRequest) {
        List<InterviewerSchedule> interviewerSchedules = new ArrayList<>();
        List<Users> interviewers = userRepository.findAllByIdIn(interviewRequest.getInterviewId());
        for (Users i : interviewers) {
            InterviewerSchedule interviewerSchedule = InterviewerSchedule
                    .builder()
                    .interviewer(i)
                    .schedule(interviewSchedule)
                    .build();
            interviewerSchedules.add(interviewerSchedule);
        }
        return interviewerSchedules;
    }
}
