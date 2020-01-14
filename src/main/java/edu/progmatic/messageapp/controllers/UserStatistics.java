package edu.progmatic.messageapp.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserStatistics {

    private String name;
    private int messageCounter;
    private Map<String, Integer> msgCounter;

    public UserStatistics() {
        this.msgCounter = new HashMap<>();
    }

    public Map<String, Integer> getMsgCounter() {
        return msgCounter;
    }

    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    }

    public int getMessageCounter() {
        return messageCounter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
