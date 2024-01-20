package com.hust.interviewmanagement.controllers;

import com.hust.interviewmanagement.entities.Account;
import com.hust.interviewmanagement.entities.Job;
import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.enums.ELabelCommon;
import com.hust.interviewmanagement.service.*;
import com.hust.interviewmanagement.utils.SearchUtil;
import com.hust.interviewmanagement.web.request.CandidateRequest;
import com.hust.interviewmanagement.web.request.JobSearch;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 5242880L)
public class IndexController {
    private final UserService userService;
    private final JobService jobService;
    private final SkillService skillService;
    private final LevelService levelService;
    private final CandidateService candidateService;
    private final SearchUtil searchUtil;

    @GetMapping("/admin/user")
    public String user() {
        return "layout/navigation";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "layout/navigation";
    }

    @GetMapping("/login")
    public String login() {
        return "ui/login";
    }

    @PostMapping("/login")
    public String loginFail() {
        return "ui/login";
    }


    @PostMapping("/")
    public String loginSuccess(HttpServletRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        Users user = userService.findUserByAccount_Email(authentication.getName());
        System.out.println("login success");
        HttpSession session = req.getSession();
        session.setAttribute(ELabelCommon.USER.getValue(), user);
        if (user.getAccount().isCheckPassword()) {
            return "redirect:/admin/job/";
        }
        return "redirect:/admin/user/changePassword";

    }


    @GetMapping("/oauth")
    public String oauthSuccess(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DefaultOidcUser defaultOidcUser = (DefaultOidcUser) authentication.getPrincipal();
        Users users = Users.builder().fullName(defaultOidcUser.getFullName())
                .account(Account.builder()
                        .email(defaultOidcUser.getEmail())
                        .build())
                .build();
        HttpSession session = request.getSession();
        session.setAttribute("user", users);
        return "redirect:/";
    }

    //    index
    @GetMapping("/")
    public String index(Model model, HttpServletRequest req) {
        model.addAttribute("jobs", jobService.findNewJob());
        List<String> address = List.of("Nghệ An", "Hà Nội", "Hồ Chí Minh", "Huế", "Đà Nẵng");
        HttpSession session = req.getSession();
        session.setAttribute("skills", skillService.findAllSkill());
        session.setAttribute("levels", levelService.findAllLevel());
        session.setAttribute("address", address);
        return "ui/index";
    }


    //    detail
    @GetMapping("/job-detail/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        model.addAttribute("job", jobService.findJobById(id));
        return "ui/job-detail";
    }

    @PostMapping("/applyCv")
    public String applyCv(@ModelAttribute CandidateRequest candidateRequest,
                          RedirectAttributes ra) throws GeneralSecurityException, IOException {
        if (candidateService.saveCandidate(candidateRequest) != null) {
            ra.addFlashAttribute(ELabelCommon.ALERT.getValue(), "Success");
        } else {
            ra.addFlashAttribute("candidateRequest", candidateRequest);
            ra.addFlashAttribute(ELabelCommon.ALERT.getValue(), "Thất bại, Bạn đã nạp cv rồi");
        }
        return "redirect:/job-detail/" + candidateRequest.getJobId();
    }

    @GetMapping("/search")
    public String search(@ModelAttribute JobSearch jobSearch,
                         Model model) throws GeneralSecurityException, IOException {
        Page<Job> jobs = jobService.findAllJob(searchUtil.getJobRequest(jobSearch));
        JobSearch jobResponse = searchUtil.setPageMax(jobs.getTotalPages(), jobSearch);
        model.addAttribute("jobs", jobs);
        model.addAttribute("jobResponse", jobResponse);
        return "ui/search";
    }

    //    error
    @GetMapping("/403")
    public String error() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();
        if (role.contains("ROLE_RECRUITER") || role.contains("ROLE_ADMIN")
                || role.contains("ROLE_MANAGER") || role.contains("ROLE_INTERVIEW")) {
            return "ui/403-admin";
        }
        return "ui/403-user";
    }


}
