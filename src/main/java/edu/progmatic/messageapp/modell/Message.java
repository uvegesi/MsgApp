package edu.progmatic.messageapp.modell;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Message {

    //private static Long maxId = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Size(min = 10, max = 50, message = "Not between {2} and {1} characters!")
    //@NotNull
    //@Pattern(regexp = "^[A-Z]\\w* [A-Z]\\w*", message = "Not a valid name!")

    @NotNull
    @NotEmpty
    @Column(name = "TEXT")
    private String text;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @ManyToOne
    private Topic topic;

    @JsonIgnore
    @ManyToOne
    private User author;

    public Message() {
    }

    public Message(User author, String text, LocalDateTime creationDate) {
        //this.id = maxId++;
        this.author = author;
        this.text = text;
        this.creationDate = creationDate;
        this.isDeleted = false;
    }

    public Message(User author, String text) {
        this.author = author;
        this.text = text;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setAuthor(User author) {
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

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
