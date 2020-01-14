package edu.progmatic.messageapp.controllers;


import org.springframework.boot.devtools.restart.AgentReloader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

//a contorller egy bean is egyben es singelton
@Controller
public class HomeController {

    static Map<String, String > greetingsMap = new HashMap<>();
    static {
        greetingsMap.put("hu", "szia");
        greetingsMap.put("jp", "Koniciwa");
        greetingsMap.put("en", "Hello");
    }

    @RequestMapping(value = {"/", "/home"}, method = GET)
    public String home(@RequestParam(name = "name", required = false) String userName,
                       @RequestParam(name = "lang", required = false, defaultValue = "jp") String language,
                               Model model){
        if (!greetingsMap.containsKey(language)) {
            language = "jp";
        }
        model.addAttribute("greeting", greetingsMap.get(language) + " " + userName);
        return "home";
    }

}
