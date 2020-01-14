package edu.progmatic.messageapp.modell;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class Message {

    private static Long maxId = 1L;

    private Long id;
    //@Size(min = 10, max = 50, message = "Not between {2} and {1} characters!")
    //@NotNull
    //@Pattern(regexp = "^[A-Z]\\w* [A-Z]\\w*", message = "Not a valid name!")
    private String author;
    @NotNull
    @NotEmpty
    private String text;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime creationDate;

    public Message() {
    }

    public Message(String author, String text, LocalDateTime creationDate) {
        this.id = maxId++;
        this.author = author;
        this.text = text;
        this.creationDate = creationDate;
    }

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
