package edu.progmatic.messageapp.dto;

import edu.progmatic.messageapp.modell.Topic;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CreateMsgDto {

    private String text;
    private long topicId;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }
}
