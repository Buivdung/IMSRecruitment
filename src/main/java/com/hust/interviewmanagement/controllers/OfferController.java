package com.hust.interviewmanagement.controllers;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.service.*;
import com.hust.interviewmanagement.utils.SearchUtil;
import com.hust.interviewmanagement.web.request.OfferRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import com.hust.interviewmanagement.web.response.CandidateResp;
import com.hust.interviewmanagement.web.response.OfferExport;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/offer")
@RequiredArgsConstructor
public class OfferController {
    private final ResultService resultService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final OfferService offerService;
    private final SearchUtil searchUtil;
    private final FileService<OfferExport> fileService;

    @GetMapping({"", "/"})
    public String getJobs(HttpServletRequest req,
                          Model model,
                          @ModelAttribute SearchRequest searchRequest) {
        Page<Offer> offers = offerService.findAllOffer(searchUtil.getSearchRequest(searchRequest));
        SearchRequest searchResponse = searchUtil.setPageMax(offers.getTotalPages(), searchRequest);
        model.addAttribute("offers", offers);
        model.addAttribute("departments", departmentService.findAllDepartment());
        model.addAttribute("searchResponse", searchResponse);
        return "ui/offer/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("resultInterview", resultService.findByResultPass());
        model.addAttribute("offerRequest", new OfferRequest());
        model.addAttribute("departments", departmentService.findAllDepartment());
        model.addAttribute("users", userService.findUserByRoleRecruiterAndManager());
        return "ui/offer/add";
    }

    @PostMapping("/create")
    public String createOffer(@ModelAttribute OfferRequest offerRequest,
                              RedirectAttributes ra) throws MessagingException {
        Offer offer = offerService.saveOffer(offerRequest);
        if (Objects.isNull(offer)) {
            ra.addFlashAttribute("offerRequest", offerRequest);
            ra.addFlashAttribute("alert", "Fail");
        } else {
            ra.addFlashAttribute("alert", "success");
        }
        return "redirect:/admin/offer/create";
    }

    @GetMapping("/getResultCandidate/{id}")
    @ResponseBody
    public CandidateResp getCandidateResp(@PathVariable Long id) {
        ResultInterview resultInterview = resultService.findResultInterviewByCandidate_Id(id);
        List<InterviewerSchedule> interviewerSchedules = resultInterview.getInterviewSchedule().getInterviewer();
        List<String> interview = interviewerSchedules.stream().map(x -> x.getInterviewer().getFullName()).toList();
        return CandidateResp.builder()
                .note(resultInterview.getNote())
                .interviewer(interview)
                .jobTitle(resultInterview.getCandidate().getJob().getTitle())
                .level(resultInterview.getCandidate().getJob().getLevel().getName())
                .build();
    }

    @GetMapping("/{id}")
    public String getOffer(@PathVariable Long id, Model model) {
        Offer offer = offerService.findOfferById(id);
        List<InterviewerSchedule> interviewerSchedules = offer.getResultInterview().getInterviewSchedule().getInterviewer();
        model.addAttribute("offer", offer);
        model.addAttribute("interviewerSchedules", interviewerSchedules);
        return "ui/offer/detail";
    }

    @GetMapping("/edit/{id}")
    public String getOfferEdit(@PathVariable Long id, Model model) {
        Offer offer = offerService.findOfferById(id);
        List<Users> users = userService.findUserByRoleRecruiterAndManager();
        List<InterviewerSchedule> interviewerSchedules = offer.getResultInterview().getInterviewSchedule().getInterviewer();
        List<Department> departments = departmentService.findAllDepartment();
        model.addAttribute("users", users);
        model.addAttribute("departments", departments);
        model.addAttribute("offer", offer);
        model.addAttribute("interviewSchedules", interviewerSchedules);
        return "ui/offer/edit";
    }

    @PostMapping("/edit/{id}")
    public String offerEdit(@PathVariable Long id,
                            RedirectAttributes ra,
                            OfferRequest offerRequest) {
        Offer offer = offerService.updateOffer(offerRequest);
        if (Objects.isNull(offer)) {
            ra.addFlashAttribute("alert", "fail");
        } else {
            ra.addFlashAttribute("alert", "Success");
        }
        return "redirect:/admin/offer/edit/" + id;
    }

    @GetMapping("/approve/{id}")
    public String getApproveOffer(@PathVariable Long id, Model model) {
        Offer offer = offerService.findOfferById(id);
        List<InterviewerSchedule> interviewerSchedules = offer.getResultInterview().getInterviewSchedule().getInterviewer();
        model.addAttribute("offer", offer);
        model.addAttribute("interviewSchedules", interviewerSchedules);
        return "ui/offer/approve";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        offerService.deleteOffer(id);
        ra.addFlashAttribute("alart","Delete success");
        return "redirect:/admin/offer/";
    }

    @GetMapping("/approve/accepted/{id}")
    @ResponseBody
    public void approveOffer(@PathVariable Long id, @RequestParam(required = false) String notes) {
        offerService.approveOffer(id, notes);
    }

    @GetMapping("/approve/rejected/{id}")
    @ResponseBody
    public void rejectOffer(@PathVariable Long id, @RequestParam(required = false) String notes) {
        offerService.rejectOffer(id, notes);
    }
    @PostMapping("/export")
    public void exportOffer(@RequestParam LocalDate fromDate,
                            @RequestParam LocalDate toDate,
                            HttpServletRequest req,
                            HttpServletResponse resp) throws IOException, ServletException {
        List<Offer> offers = offerService.findAllOfferByDate(fromDate, toDate);
        List<OfferExport> exports = offers.stream().map(this::getOfferExport).toList();
        if (!exports.isEmpty()) {
            fileService.export(resp, exports);
        } else {
            req.getRequestDispatcher("/admin/offer/").forward(req, resp);
        }
    }

    private OfferExport getOfferExport(Offer offer) {
        OfferExport offerExport = new OfferExport();
        offerExport.setApproved(offer.getManager().getFullName());
        offerExport.setCandidateName(offer.getResultInterview().getCandidate().getFullName());
        offerExport.setEmail(offer.getResultInterview().getCandidate().getEmail());
        offerExport.setInterviewNotes(offer.getResultInterview().getNote());
        offerExport.setDepartment(offer.getDepartment().getName());
        offerExport.setStatus(offer.getStatus().name());
        offerExport.setContractType(offer.getContractType().name());
        offerExport.setCreateDate(offer.getCreateDate().toString());
//        offerExport.setUpdateDate(offer.getUpdateDate().toString());
//        offerExport.setLevel(offer.getLevel().getName());
        offerExport.setDueDate(offer.getDueDate().toString());
        offerExport.setBasicSalary(offer.getBasicSalary().toString());
        offerExport.setNotes(offer.getNotes());
        return offerExport;
    }
}
