package edu.progmatic.messageapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No such message")
public class MessageNotFoundException extends RuntimeException {
}
