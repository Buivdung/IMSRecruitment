package com.hust.interviewmanagement.controllers;

import com.hust.interviewmanagement.entities.Candidate;
import com.hust.interviewmanagement.enums.ELabelCommon;
import com.hust.interviewmanagement.service.CandidateService;
import com.hust.interviewmanagement.service.JobService;
import com.hust.interviewmanagement.utils.GoogleApiUtil;
import com.hust.interviewmanagement.utils.SearchUtil;
import com.hust.interviewmanagement.web.request.CandidateRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("/admin/candidate")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 5242880L)
public class CandidateController {

    private final CandidateService candidateService;
    private final SearchUtil searchUtil;
    private final JobService jobService;
    private final GoogleApiUtil googleApiUtil;


    @GetMapping({"/", ""})
    public String getCandidates(@ModelAttribute SearchRequest searchRequest,
                                Model model) {
        Page<Candidate> candidates = candidateService.findAllCandidate(searchUtil.getSearchRequest(searchRequest));
        SearchRequest searchResponse = searchUtil.setPageMax(candidates.getTotalPages(), searchRequest);
        model.addAttribute("candidates", candidates);
        model.addAttribute("searchResponse", searchResponse);
        return "ui/candidate/list";
    }

    @GetMapping("/{id}")
    public String candidateDetail(@PathVariable Long id, Model model) {
        Candidate candidate = candidateService.findCandidateById(id);
        model.addAttribute("candidate", candidate);
        return "ui/candidate/detail";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute CandidateRequest candidateRequest, Model model) {
        model.addAttribute("candidateRequest", candidateRequest);
        model.addAttribute("jobs", jobService.findJobByStatusOpen());
        return "ui/candidate/add";
    }

    @PostMapping("/create")
    public String createCandidate(@ModelAttribute CandidateRequest candidateRequest,
                                  Model model
    ) throws GeneralSecurityException, IOException {
        if (candidateService.saveCandidate(candidateRequest) != null) {
            model.addAttribute(ELabelCommon.ALERT.getValue(), "Success");
        } else {
            model.addAttribute("candidateRequest", candidateRequest);
            model.addAttribute(ELabelCommon.ALERT.getValue(), "Fail");
            model.addAttribute(ELabelCommon.MESSAGE.getValue(), "Candidate da ton tai");
        }
        return "ui/candidate/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Candidate candidate = candidateService.findCandidateById(id);
        model.addAttribute("jobs", jobService.findJobByStatusOpen());
        model.addAttribute("candidate", candidate);
        return "ui/candidate/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCandidate(@PathVariable Long id,
                                @ModelAttribute CandidateRequest candidateRequest,
                                RedirectAttributes ra,
                                Model model) throws IOException, GeneralSecurityException {
        candidateService.updateCandidate(candidateRequest);
        ra.addFlashAttribute(ELabelCommon.ALERT.getValue(), "Success");
        return "redirect:/admin/candidate/edit/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteCandidate(@PathVariable Long id,
                                  RedirectAttributes ra) {
        candidateService.deleteCandidate(id);
        ra.addFlashAttribute(ELabelCommon.ALERT.getValue(), "Success");
        return "redirect:/admin/candidate";
    }

    @GetMapping("/download/{id}")
    public void download(HttpServletResponse resp, @PathVariable Long id) {
        Candidate candidate = candidateService.findCandidateById(id);
        resp.setContentType("application/octet-stream");
        String keyHeader = "Content-Disposition";
        String valueHeader = "attachment; filename=" + candidate.getCv();
        resp.setHeader(keyHeader, valueHeader);
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            outputStream.write(googleApiUtil.downloadFile(candidate.getCvId()).toByteArray());
        } catch (IOException e) {
            throw new IllegalArgumentException("Don't download.");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
