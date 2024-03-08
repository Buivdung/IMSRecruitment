package com.hust.interviewmanagement.config;

import com.hust.interviewmanagement.entities.Notification;
import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.service.NotificationService;
import com.hust.interviewmanagement.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SessionFilterCustom extends OncePerRequestFilter {

    private final UserService userService;
    private final NotificationService notificationService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        String title = getTitle(servletPath);
        session.setAttribute("title", title);
        if(servletPath.contains("/admin/")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!Objects.isNull(authentication)){
                Users user = userService.findUserByAccount_Email(authentication.getName());
                List<Notification> notifications = notificationService.getNotifications(user.getId());
                int numberNo = notifications.stream().filter(x -> !x.isChecked()).toList().size();
                session.setAttribute("notifications",notifications);
                session.setAttribute("numberNo",numberNo);
            }
        }
        filterChain.doFilter(request, response);
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
