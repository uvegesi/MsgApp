package edu.progmatic.messageapp.controllers;


import edu.progmatic.messageapp.modell.User;
import edu.progmatic.messageapp.services.MessageService;
import edu.progmatic.messageapp.services.UserService;
import edu.progmatic.messageapp.services.DbInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)

public class RegisterController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private DbInitializer dbInitializer;
    private MessageService messageService;

    @Autowired
    public RegisterController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("registration")
    public String registerUsers(@ModelAttribute("user") User userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (userService.userExists(userDto.getUserName())) {
            bindingResult.rejectValue("username", "Registration.username", "Existing username");
            return "registration";
        } else {
            //userDto.addAuthority("USER");
            //userDto.setAuthorities();
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userService.createUser(userDto);
            //BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            //bCryptPasswordEncoder.encode(userDto.getPassword());
        }
        return "redirect:/login";
    }

    //userService.createUser(User.withUsername(userDto.getUserName()).
    //password(userDto.getPassword()).roles("USER").build());


    @GetMapping("/registration")
    public String showRegistration(@ModelAttribute("user") User userDto) {
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

    @GetMapping("/users/{id}")
    public String messageLista(@PathVariable("id") long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        return "users";
    }
}
