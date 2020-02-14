package edu.progmatic.messageapp.controllers;


import edu.progmatic.messageapp.dto.CreateMsgDto;
import edu.progmatic.messageapp.dto.MessageRepDTO;
import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.modell.User;
import edu.progmatic.messageapp.services.MessageService;
import edu.progmatic.messageapp.services.TopicService;
import edu.progmatic.messageapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)

public class MessageController {

    //UserDto userDto = new UserDto();


    private final UserStatistics userStatistics;
    private MessageService messageServices;
    private TopicService topicService;

    @Autowired
    public MessageController(UserStatistics userStatistics,
                             MessageService messageServices, TopicService topicService) throws InterruptedException {
        this.userStatistics = userStatistics;
        this.messageServices = messageServices;
        this.topicService = topicService;
    }

    @RequestMapping(value = "/messages", method = GET)
    public String messages(@RequestParam(name = "limit", defaultValue = "100", required = false) Integer limit,
                           @RequestParam(name = "orderby", defaultValue = "", required = false) String orderby,
                           @RequestParam(name = "order", defaultValue = "asc", required = false) String order,
                           @RequestParam(name = "text", required = false) String text,
                           @RequestParam(name = "author", required = false) String author,
                           @RequestParam(name = "from", required = false) LocalDateTime from,
                           @RequestParam(name = "to", required = false) LocalDateTime to,
                           @RequestParam(name = "id", required = false) Long id,
                           @RequestParam(name = "isDeleted", required = false) boolean isDeleted,
                           @RequestParam(name = "topic", required = false) Topic topic,
                           Model model) {
        List<Message> msgs = messageServices.filterMessage(id, author, text, from, to, orderby, order, limit,
                isDeleted, topic);

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
        }

        model.addAttribute("msgList", msgs);
        return "messages";
    }

    /*@GetMapping("/messagesjson")
    public @ResponseBody List<Message> allMessagesJason() {
        List<Message> allMsgs = messageServices.messageList();
        return allMsgs;
    }

     */

    @GetMapping("/message/{id}")
    public String showOneMessage(
            @PathVariable("id") Long msgId,
            Model model) {
        Message message = messageServices.getMessage(msgId);
        model.addAttribute("message", message);
        return "oneMessage";
    }


    @GetMapping("/newmessages")
    public String showCreate(Model model) {
        CreateMsgDto message = new CreateMsgDto();
//        message.setAuthor(userStatistics.getName());
        model.addAttribute("message", message);
        //Topic topic = new Topic();

        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        //topic.setTopicAuthor(loggedInUser);
        model.addAttribute("topics", topicService.topicsList());
        //userStatistics.getName();
        return "newMessage";
    }

    //public String modifiedMessage()

    @GetMapping("/messages/modifymessage/{id}")
    public String modifyMessage(
                @PathVariable("id") long msgId,
                @RequestParam(name = "text", defaultValue = "hello", required = false) String text,
                @RequestParam(name = "sleep", defaultValue = "0", required = false) Long sleep) {

        messageServices.getMessageToModify(msgId, text, sleep);

        return "redirect:/messages";
    }

    @PostMapping("/createmessage")
    public String createMessage(@Valid @ModelAttribute("message") CreateMsgDto m, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newMessage";
        }
        messageServices.addMessage(m);


//        userStatistics.setName(m.getAuthor());
//        Map<String, Integer> map = userStatistics.getMsgCounter();
//        map.putIfAbsent(m.getAuthor(), 0);
//        map.put(m.getAuthor(), map.get(m.getAuthor()) + 1);
//        String user1 = "";
//        Integer numOfMsg = 0;
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            user1 = entry.getKey();
//            numOfMsg = entry.getValue();
//        }
//        System.out.println(user1 + " " + numOfMsg);

        /*List<String> usersInOneSession = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            if (user.getUsername().equals(m.getAuthor())) {
                usersInOneSession.add(m.getAuthor());
            }
        }
        System.out.println(usersInOneSession);
         */
        return "redirect:/messages";
    }


    @RequestMapping(value = "/msgList/{numOfMsg}", method = GET)
    public String msgTable(@RequestParam(name = "numOfMsg", defaultValue = "100", required = false) Integer numOfMsg,
                           Message message,
                           Model model) {
        List<Message> displayedMsgs = new ArrayList<>();
        for (int i = 0; i < numOfMsg; i++) {
            message = messageServices.getMessage(message.getId());
            displayedMsgs.add(message);       //messages.get(i)
        }
        model.addAttribute("msgList", displayedMsgs);
        return "msgList";
    }


    public String listMessages(Model model) {
        Map<String, Integer> map = userStatistics.getMsgCounter();

        return "statistics";
    }



    @PostMapping("/messages/delete/{msgId}")
    // a @pathvariable helyett lehetne @ requestparam is, ez vallás kérdése
    public String deleteMsg(@PathVariable int msgId) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            messageServices.deleteMessage(msgId);
        } else {
            return "redirect:/messages";
        }
        return "redirect:/messages";
    }

    /*@RequestMapping(method = RequestMethod.DELETE, path = "/messages/delete/{msgId}")
    public @ResponseBody void deleteJson(@PathVariable long msgId) {
        messageServices.deleteMessage(msgId);
        //return "redirect:/messages";
    }
     */
}
