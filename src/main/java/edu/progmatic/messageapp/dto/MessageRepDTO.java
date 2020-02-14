package edu.progmatic.messageapp.dto;

import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.modell.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class MessageRepDTO {

    private Long id;
    //@Size(min = 10, max = 50, message = "Not between {2} and {1} characters!")
    //@NotNull
    //@Pattern(regexp = "^[A-Z]\\w* [A-Z]\\w*", message = "Not a valid name!")


    private String text;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime creationDate;

    private boolean isDeleted;

    private String author;

    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
