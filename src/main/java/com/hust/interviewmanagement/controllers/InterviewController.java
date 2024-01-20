package com.hust.interviewmanagement.controllers;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.enums.ELabelCommon;
import com.hust.interviewmanagement.enums.EMessageJob;
import com.hust.interviewmanagement.service.*;
import com.hust.interviewmanagement.utils.SearchUtil;
import com.hust.interviewmanagement.web.request.InterviewRequest;
import com.hust.interviewmanagement.web.request.ResultRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/interview")
@RequiredArgsConstructor
public class InterviewController {
    private final JobService jobService;
    private final UserService userService;
    private final InterviewService interviewService;
    private final SearchUtil searchUtil;
    private final CandidateService candidateService;
    private final ResultService resultService;

    @GetMapping({"/", ""})
    public String getCandidates(@ModelAttribute SearchRequest searchRequest,
                                Model model) {
        Page<InterviewSchedule> interviewSchedules = interviewService
                .findAllInterviewSchedule(searchUtil.getSearchRequest(searchRequest));
        SearchRequest searchResponse = searchUtil
                .setPageMax(interviewSchedules.getTotalPages(), searchRequest);
        model.addAttribute("interviewSchedules", interviewSchedules);
        model.addAttribute("searchResponse", searchResponse);
        return "ui/interview/list";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute InterviewRequest interviewRequest
            , Model model) {
        model.addAttribute("interviewRequest", interviewRequest);
        model.addAttribute("jobs", jobService.findJobByStatusOpen());
        model.addAttribute("users", userService.findUserByRoleInterviewAndRecruiter());
        return "ui/interview/add";
    }

    @PostMapping("/create")
    public String createInterview(@ModelAttribute @Validated InterviewRequest interviewRequest, BindingResult bindingResult
            , Model model) throws MessagingException {
        if(bindingResult.hasErrors()) {
            model.addAttribute("interviewRequest", interviewRequest);
            model.addAttribute("alert", "Create false");
            model.addAttribute("jobs", jobService.findJobByStatusOpen());
            model.addAttribute("users", userService.findUserByRoleInterviewAndRecruiter());
            return "ui/interview/add";
        }
        interviewService.saveInterviewSchedule(interviewRequest);
        return "redirect:/admin/interview/create";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @ModelAttribute InterviewRequest interviewRequest
            , Model model) {
        InterviewSchedule interviewSchedule = interviewService.findInterviewScheduleById(id);
        List<Long> ids = interviewSchedule.getInterviewer()
                .stream().map(x -> x.getInterviewer().getId()).toList();
        model.addAttribute("ids", ids);
        model.addAttribute("interviewSchedule", interviewSchedule);
        model.addAttribute("users", userService.findUserByRoleInterviewAndRecruiter());
        return "ui/interview/edit";
    }

    @PostMapping("/edit/{id}")
    public String editInterview(@PathVariable Long id,
                                @ModelAttribute InterviewRequest interviewRequest,
                                BindingResult bindingResult,
                                Model model) throws MessagingException {
        if(bindingResult.hasErrors()){
            return edit(id,interviewRequest,model);
        }
        InterviewSchedule interviewSchedule = interviewService.updateInterviewSchedule(interviewRequest);
        return "redirect:/admin/interview/edit/" + id;
    }

    @GetMapping("/{id}/candidates")
    public String candidate(@PathVariable Long id,
                            Model model) {
        InterviewSchedule interviewSchedule = interviewService.findInterviewScheduleById(id);
        model.addAttribute("resultInterviews", interviewSchedule.getResultInterviews());
        return "ui/interview/list-candidate";
    }

    @GetMapping("/{interviewId}/result/{resultId}")
    public String editCandidate(@PathVariable Long interviewId,
                                @PathVariable Long resultId,
                                Model model) {
        InterviewSchedule interviewSchedule = interviewService.findInterviewScheduleById(interviewId);
        ResultInterview resultInterview = interviewSchedule.getResultInterviews().stream()
                .filter(x -> x.getId().equals(resultId))
                .findFirst()
                .orElseThrow();
        model.addAttribute("resultInterviews", resultInterview);
        model.addAttribute("interviewSchedule", interviewSchedule);
        return "ui/interview/result";
    }

    @PostMapping("/{interviewId}/result/{resultId}")
    public String result(@PathVariable Long interviewId,
                         @PathVariable Long resultId,
                         @ModelAttribute ResultRequest resultRequest,
                         Model model) {
        resultService.updateResult(resultRequest);
        return "redirect:/admin/interview/" + interviewId + "/result/" + resultId;
    }

    @GetMapping("/{id}")
    public String interview(@PathVariable Long id,
                            @ModelAttribute ResultRequest requestRequest,
                            Model model) {
        InterviewSchedule interviewSchedule = interviewService.findInterviewScheduleById(id);
        model.addAttribute("interviewSchedule", interviewSchedule);
        return "ui/interview/detail";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,Model model) throws MessagingException {
        interviewService.deleteInterviewSchedule(id);
        model.addAttribute("alert","Success");
        return "redirect:/admin/interview/";
    }
}
