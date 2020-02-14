package edu.progmatic.messageapp.modell;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long topicId;
    @Column
    private String topicAuthor;
    @Column
    private String topicTheme;

    @JsonIgnore
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "topic")
    private List<Message> messages;

    public Topic() {
    }

    public Topic(long topicId, String topicAuthor, String topicTheme) {
        this.topicId = topicId;
        this.topicAuthor = topicAuthor;
        this.topicTheme = topicTheme;
    }

    public long getTopicId() {
        return topicId;
    }

    public String getTopicAuthor() {
        return topicAuthor;
    }

    public String getTopicTheme() {
        return topicTheme;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public void setTopicAuthor(String topicAuthor) {
        this.topicAuthor = topicAuthor;
    }

    public void setTopicTheme(String topicTheme) {
        this.topicTheme = topicTheme;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
