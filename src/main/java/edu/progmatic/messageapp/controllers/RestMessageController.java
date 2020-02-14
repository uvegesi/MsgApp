package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.dto.CreateMsgDto;
import edu.progmatic.messageapp.dto.MessageRepDTO;
import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.modell.User;
import edu.progmatic.messageapp.services.MessageService;
import edu.progmatic.messageapp.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class RestMessageController {

    private MessageService messageServices;
    private final UserStatistics userStatistics;
    private TopicService topicService;

    @Autowired
    public RestMessageController(MessageService messageServices, UserStatistics userStatistics, TopicService topicService) {
        this.messageServices = messageServices;
        this.userStatistics = userStatistics;
        this.topicService = topicService;
    }

    @GetMapping("/rest/messages")
    public List<MessageRepDTO> allMessages() {
        List<MessageRepDTO> allMessages = messageServices.messageList();
        return allMessages;
    }

    @DeleteMapping("/rest/messages/{msgId}")
    public void deleteJsonMsg(@Valid @PathVariable long msgId) {
        messageServices.deleteMessage(msgId);
    }

    @GetMapping("/rest/messages/{msgId}")
    public Message getMsg(@Valid @PathVariable long msgId) {
        return messageServices.getMessage(msgId);
    }

    @PostMapping("/rest/messages")
    public MessageRepDTO createMsg(@Valid @RequestBody CreateMsgDto message) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message newMessage = messageServices.addMessage(message);
        MessageRepDTO msg = new MessageRepDTO();
        msg.setAuthor(loggedInUser.getUserName());
        msg.setCreationDate(LocalDateTime.now());
        msg.setText(newMessage.getText());
        msg.setTopic(newMessage.getTopic().getTopicTheme());
        msg.setId(newMessage.getId());
        msg.setDeleted(newMessage.isDeleted());

        return msg;
    }
}
