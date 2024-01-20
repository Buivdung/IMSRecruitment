package com.hust.interviewmanagement.controllers;

import com.hust.interviewmanagement.entities.Job;
import com.hust.interviewmanagement.enums.ELabelCommon;
import com.hust.interviewmanagement.enums.EMessageJob;
import com.hust.interviewmanagement.service.JobService;
import com.hust.interviewmanagement.service.LevelService;
import com.hust.interviewmanagement.service.SkillService;
import com.hust.interviewmanagement.utils.SearchUtil;
import com.hust.interviewmanagement.web.request.JobRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/job")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final SearchUtil searchUtil;
    private final SkillService skillService;
    private final LevelService levelService;

    @GetMapping({"", "/"})
    public String getJobs(@ModelAttribute SearchRequest searchRequest,
                          Model model) {
        Page<Job> jobs = jobService.findAllJob(searchUtil.getSearchRequest(searchRequest));
        SearchRequest searchResponse = searchUtil.setPageMax(jobs.getTotalPages(), searchRequest);
        model.addAttribute(ELabelCommon.JOBS.getValue(), jobs);
        model.addAttribute(ELabelCommon.SEARCH_RESPONSE.getValue(), searchResponse);
        return "ui/job/list";
    }

    @GetMapping("/create")
    public String getCreateJob(Model model,
                               @ModelAttribute JobRequest jobRequest) {
        model.addAttribute(ELabelCommon.SKILLS.getValue(), skillService.findAllSkill());
        model.addAttribute(ELabelCommon.LEVELS.getValue(), levelService.findAllLevel());
        return "ui/job/add";
    }

    @PostMapping("/create")
    public String jobCreateForm(@ModelAttribute @Validated JobRequest jobRequest,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes ra) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(ELabelCommon.JOB_REQUEST.getValue(), jobRequest);
            model.addAttribute(ELabelCommon.ALERT.getValue(), EMessageJob.CREATE_JOB_FAIL.getValue());
            model.addAttribute(ELabelCommon.SKILLS.getValue(), skillService.findAllSkill());
            model.addAttribute(ELabelCommon.LEVELS.getValue(), levelService.findAllLevel());
            return "ui/job/add";
        }
        if (validateDate(jobRequest)) {
            ra.addFlashAttribute(ELabelCommon.MESSAGE.getValue(), EMessageJob.M_DATE.getValue());
        } else if (validateSalary(jobRequest)) {
            ra.addFlashAttribute(ELabelCommon.MESSAGE.getValue(), EMessageJob.M_SALARY.getValue());
        } else {
            Job job = jobService.saveJob(jobRequest);
            if (job != null) {
                ra.addFlashAttribute(ELabelCommon.ALERT.getValue(), EMessageJob.CREATE_JOB_SUCCESS.getValue());
                return "redirect:/admin/job/create";
            } else {
                ra.addFlashAttribute(ELabelCommon.MESSAGE.getValue(), EMessageJob.DUPLICATE_JOB.getValue());
            }
        }
        ra.addFlashAttribute(ELabelCommon.JOB_REQUEST.getValue(), jobRequest);
        ra.addFlashAttribute(ELabelCommon.ALERT.getValue(), EMessageJob.CREATE_JOB_FAIL.getValue());
        return "redirect:/admin/job/create";
    }

    @GetMapping("/{id}")
    public String getJob(@PathVariable Long id,
                         Model model) {
        Job job = jobService.findJobById(id);
        model.addAttribute(ELabelCommon.JOB.getValue(), job);
        return "ui/job/detail";
    }

    @GetMapping("/edit/{id}")
    public String getEditJob(@PathVariable Long id,
                             Model model,
                             @ModelAttribute JobRequest jobRequest) {
        Job job = jobService.findJobById(id);
        List<Long> skillIds = job.getSkillJobs().stream().map(s -> s.getSkill().getId()).toList();
        model.addAttribute(ELabelCommon.JOB.getValue(), job);
        model.addAttribute(ELabelCommon.SKILLS.getValue(), skillService.findAllSkill());
        model.addAttribute(ELabelCommon.LEVELS.getValue(), levelService.findAllLevel());
        model.addAttribute(ELabelCommon.SKILL_IDS.getValue(), skillIds);
        return "ui/job/edit";
    }

    @PostMapping("/edit/{id}")
    public String postEditJob(@PathVariable Long id,
                              Model model,
                              @ModelAttribute @Validated JobRequest jobRequest,
                              BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute(ELabelCommon.JOB_REQUEST.getValue(), jobRequest);
            model.addAttribute(ELabelCommon.ALERT.getValue(), EMessageJob.CREATE_JOB_FAIL.getValue());
            model.addAttribute(ELabelCommon.SKILLS.getValue(), skillService.findAllSkill());
            model.addAttribute(ELabelCommon.LEVELS.getValue(), levelService.findAllLevel());
            return "ui/job/add";
        }
        if (validateDate(jobRequest)) {
            model.addAttribute(ELabelCommon.MESSAGE.getValue(), EMessageJob.M_DATE.getValue());
        } else if (validateSalary(jobRequest)) {
            model.addAttribute(ELabelCommon.MESSAGE.getValue(), EMessageJob.M_SALARY.getValue());
        } else {
            Job job = jobService.findJobById(id);
            if (jobService.updateJob(job, jobRequest) != null) {
                model.addAttribute(ELabelCommon.ALERT.getValue(), EMessageJob.EDIT_JOB_SUCCESS.getValue());
                return getEditJob(id, model, jobRequest);
            } else {
                model.addAttribute(ELabelCommon.MESSAGE.getValue(), EMessageJob.DUPLICATE_JOB.getValue());
            }
        }
        model.addAttribute(ELabelCommon.ALERT.getValue(), EMessageJob.EDIT_JOB_FAIL.getValue());
        return getEditJob(id, model, jobRequest);
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id, RedirectAttributes ra) {
        jobService.deleteJobById(id);
        ra.addFlashAttribute(ELabelCommon.MESSAGE.getValue(), EMessageJob.DELETE_JOB_SUCCESS.getValue());
        return "redirect:/admin/job";
    }

    @PostMapping("/import")
    public String importJob(@ModelAttribute MultipartFile fileImport,
                            RedirectAttributes ra) {
        if (jobService.saveAllJobs(fileImport).isEmpty()) {
            ra.addFlashAttribute(ELabelCommon.ALERT.getValue(), EMessageJob.CREATE_JOB_FAIL.getValue());
            ra.addFlashAttribute(ELabelCommon.MESSAGE.getValue(), EMessageJob.DUPLICATE_JOB.getValue());
        } else {
            ra.addFlashAttribute(ELabelCommon.ALERT.getValue(), EMessageJob.CREATE_JOB_SUCCESS.getValue());
        }
        return "redirect:/admin/job";
    }

    private boolean validateDate(JobRequest jobRequest) {
        return jobRequest.getEndDate().isBefore(jobRequest.getStartDate());
    }

    private boolean validateSalary(JobRequest jobRequest) {
        return jobRequest.getSalaryFrom().compareTo(jobRequest.getSalaryTo()) > 0;
    }
}
