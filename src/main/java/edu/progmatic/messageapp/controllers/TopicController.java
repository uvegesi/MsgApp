package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.exceptions.TopicException;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.services.MessageService;
import edu.progmatic.messageapp.services.TopicService;
import edu.progmatic.messageapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;
import java.util.List;

@Controller
@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS
)

public class TopicController {

    private TopicService topicService;
    private UserService userService;
    private MessageService messageService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/addTopic")
    public String showCreatedTopics(Model model) {
        Topic topic = new Topic();
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        topic.setTopicAuthor(loggedInUser);
        model.addAttribute("topic", topic);
        return "topic";
    }

    @PostMapping("/addTopic")
    public String creatTopic(@Valid @ModelAttribute("topic")Topic topic, BindingResult bindingResult) throws TopicException {
        if (bindingResult.hasErrors()) {
            throw new TopicException("Már van ilyen neű topik");
        } else {
            topicService.addTopic(topic);
        }
        return "redirect:/newmessages";
    }

    @GetMapping("/topics")
    public String topicsList(Model model) {
        List<Topic> topicList = topicService.topicsList();
        model.addAttribute("topics", topicList);
        return "topics";
    }

    @GetMapping("/topics/{topicId}")
    public String showOneTopic(@PathVariable ("topicId") Long topicId, Model model) {
        Topic topic = topicService.getTopic(topicId);
        model.addAttribute("topic", topic);
        return "messageList";
    }

    @PostMapping("/topics/delete/{topicId}")
    public String removeTopic(@PathVariable long topicId) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            topicService.deleteTopic(topicId);
            //messageService.deleteMessage(topicId);
        } else {
            return "redirect:/topics";
        }
        return "redirect:/topics";
    }
}
