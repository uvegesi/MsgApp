package edu.progmatic.messageapp.controllers;


import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.registration.UserDto;
import edu.progmatic.messageapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)

public class MessageController {

    //UserDto userDto = new UserDto();

    private final UserStatistics userStatistics;
    private InMemoryUserDetailsManager userService;
    private MessageService messageServices;

    @Autowired
    public MessageController(UserStatistics userStatistics, @Qualifier("userDetailsService") UserDetailsService userService,
                             MessageService messageServices) {
        this.userStatistics = userStatistics;
        this.userService = (InMemoryUserDetailsManager) userService;
        this.messageServices = messageServices;
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
                           Model model) {
        List<Message> msgs = messageServices.filterMessage(id, author, text, from, to, orderby, order, limit);

        model.addAttribute("msgList", msgs);
        return "messages";
    }

    @GetMapping("/message/{id}")
    public String showOneMessage(
            @PathVariable("id") Long msgId,
            Model model){
        Message message = messageServices.getMessage(msgId);
        model.addAttribute("message", message);
        return "oneMessage";
    }

    @GetMapping("/newmessages")
    public String showCreate (Model model) {
        Message message = new Message();
        message.setAuthor(userStatistics.getName());
        model.addAttribute("message", message);
        //userStatistics.getName();
        return "newMessage";
    }

    @PostMapping("/createmessage")
    public String createMessage(@Valid @ModelAttribute("message") Message m, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newMessage";
        }
        messageServices.addMessage(m);
        m.setCreationDate(LocalDateTime.now());
        m.setId((long)(messageServices.getNumOfMsg()));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        m.setAuthor(user.getUsername());

        userStatistics.setName(m.getAuthor());
        Map<String, Integer> map = userStatistics.getMsgCounter();
        map.putIfAbsent(m.getAuthor(), 0);
        map.put(m.getAuthor(), map.get(m.getAuthor())+1);
        String user1 = "";
        Integer numOfMsg = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            user1 = entry.getKey();
            numOfMsg = entry.getValue();
        }
        System.out.println(user1 + " " + numOfMsg);

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


    public String listMessages(Model model) {
        Map<String, Integer> map = userStatistics.getMsgCounter();

        return "statistics";
    }

    @PostMapping("registration")
    public String registerUsers(@ModelAttribute("user") UserDto userDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "registration";
        }

            if (userService.userExists(userDto.getUserName())){
                bindingResult.rejectValue("username", "Regisztration.username", "Existing username");
                return "registration";
            } else {
                userDto.addAuthority("ROLE_USER");
                userService.createUser(userDto);
            }
            return "redirect:/login";
        }

        //userService.createUser(User.withUsername(userDto.getUserName()).
        //password(userDto.getPassword()).roles("USER").build());


    @GetMapping("/registration")
    public String showRegistration(@ModelAttribute("user") UserDto userDto) {
        return "registration";
    }

    @GetMapping("/login")
    public String loginUsers(Model model) {
        return "login";
    }

    /*@RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

     */

}
