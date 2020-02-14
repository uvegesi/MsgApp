package edu.progmatic.messageapp.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllAdvice {

    /*@ExceptionHandler(Exception.class)
    public String handleErrors(Exception ex, Model model){
        String stacktrace = ExceptionUtils.getStackTrace(ex);
        model.addAttribute("exceptionMessage", ex.getMessage());
        model.addAttribute("exceptionMessage2", stacktrace);
        return "errorPage";
    }

     */
}
