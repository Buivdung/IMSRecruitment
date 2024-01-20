package com.hust.interviewmanagement.controllerAdvice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class HandlerError {

//    @ExceptionHandler(Exception.class)
//    public String handlerError(Exception e, Model model){
//        model.addAttribute("messages",e.getMessage());
//        model.addAttribute("stackTrace",e.getStackTrace());
//        return "error";
//    }
}
