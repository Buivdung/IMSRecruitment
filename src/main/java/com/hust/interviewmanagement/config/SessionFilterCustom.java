package com.hust.interviewmanagement.config;

import com.hust.interviewmanagement.entities.Account;
import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.service.AccountService;
import com.hust.interviewmanagement.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SessionFilterCustom extends OncePerRequestFilter {

    private final AccountService accountService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        String title = getTitle(servletPath);
        session.setAttribute("title", title);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null
//                && accountService.existedAccountByEmail(authentication.getName())) {
//            Account account = accountService.findAccountByEmail(authentication.getName());
//            if (!account.isCheckPassword() && !servletPath.contains("/admin/user/changePassword")) {
//                response.sendRedirect("/admin/user/changePassword");
//            } else {
//                filterChain.doFilter(request, response);
//            }
//        } else {
        filterChain.doFilter(request, response);
//        }
    }

    private String getTitle(String servletPath) {
        String title;
        if (servletPath.contains("/user/")) {
            title = "USER MANAGEMENT";
        } else if (servletPath.contains("/candidate/")) {
            title = "CANDIDATE";
        } else if (servletPath.contains("/offer/")) {
            title = "OFFER";
        } else if (servletPath.contains("/job/")) {
            title = "JOB";
        } else if (servletPath.contains("/interview/")) {
            title = "INTERVIEW SCHEDULE";
        } else {
            title = "DASHBOARD";
        }
        return title;
    }
}
