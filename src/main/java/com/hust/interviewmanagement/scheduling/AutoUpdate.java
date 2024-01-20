package com.hust.interviewmanagement.scheduling;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class AutoUpdate {



    @Scheduled(cron = "01 00 00 * * *")
    public void updateStatusInterviewerSchedule() {
//        LocalDate localDate = LocalDate.now();
//        List<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findAllBySchedule(localDate);
//        interviewSchedules.forEach(x -> {
//            x.setStatus(EStatus.IN_PROGRESS);
//            x.getResultInterviews().getCandidate().setStatus(EStatus.IN_PROGRESS);
//        });
//        interviewScheduleRepository.saveAll(interviewSchedules);
    }

    @Scheduled(cron = "01 00 08 * * *")
    public void sendMailRemind() throws MessagingException {
//        LocalDate localDate = LocalDate.now();
//        List<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findAllBySchedule(localDate);
//        Set<InterviewerSchedule> interviewers = new HashSet<>();
//        interviewSchedules.forEach(x -> interviewers.addAll(x.getInterviewer()));
//        emailService.sendMailInterviewScheduleToInterviewer(interviewers);
    }
}
