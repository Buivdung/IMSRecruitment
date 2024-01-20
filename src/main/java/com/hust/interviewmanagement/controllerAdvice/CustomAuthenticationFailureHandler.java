package com.hust.interviewmanagement.controllerAdvice;

import com.hust.interviewmanagement.enums.ELabelCommon;
import com.hust.interviewmanagement.enums.EMessageCommon;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;


public class CustomAuthenticationFailureHandler
        implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        if (exception.getMessage().equals("User is disabled")) {
            request.setAttribute(ELabelCommon.MESSAGE.getValue(), EMessageCommon.NO_ACTIVE.getValue());
        }
        if (exception.getMessage().equals("Bad credentials")) {
            request.setAttribute(ELabelCommon.MESSAGE.getValue(), EMessageCommon.LOGIN_FAIL.getValue());
        }
        request.getRequestDispatcher("/login").forward(request, response);
    }


}