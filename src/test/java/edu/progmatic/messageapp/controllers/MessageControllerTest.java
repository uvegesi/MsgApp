package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.User;
import edu.progmatic.messageapp.services.MessageService;
import edu.progmatic.messageapp.services.TopicService;
import edu.progmatic.messageapp.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.security.access.method.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MessageControllerTest {

    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void messages() throws Exception {
        MessageService ms = Mockito.mock(MessageService.class);
        UserStatistics us = Mockito.mock(UserStatistics.class);
        TopicService ts = Mockito.mock(TopicService.class);
        List<Message> msgList = new ArrayList<>();
        msgList.add(new Message(new User(), "Kapcs-ford", LocalDateTime.now()));
        Mockito.when(ms.filterMessage(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.any())).thenReturn(null);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".jsp");
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new MessageController(us, ms, ts))
                .setViewResolvers(viewResolver)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(MockMvcResultMatchers.view().name("messages"));
    }

    @Test
    public void showOneMessage() throws Exception {
        //System.out.println("showOneMessage");
        MessageService ms = Mockito.mock(MessageService.class);
        UserStatistics us = Mockito.mock(UserStatistics.class);
        TopicService ts = Mockito.mock(TopicService.class);
        PasswordEncoder pe = Mockito.mock(PasswordEncoder.class);

        UserDetailsService uds = Mockito.mock(InMemoryUserDetailsManager.class);
        MockMvc mockMvc1 = MockMvcBuilders.standaloneSetup(new MessageController(us, ms, ts))
                .build();
        Mockito.when(ms.getMessage(Mockito.anyLong())).thenReturn(new Message());
        mockMvc1.perform(MockMvcRequestBuilders.get("/message/1"))
                .andExpect(MockMvcResultMatchers.view().name("oneMessage"));
                //.param("text", "üzenet szövege")
                //.param("aladár", "text"));
              //  .andExpect(MockMvcResultMatchers.redirectedUrl("message/id"));

    }

    @Test
    public void showCreate() {
    }

    @Test
    public void createMessage() {
    }

    @Test
    public void msgTable() {
    }

    @Test
    public void listMessages() {
    }

    @Test
    public void registerUsers() {
    }

    @Test
    public void showRegistration() {
    }

    @Test
    public void loginUsers() {
    }

    @Test
    public void deleteMsg() {
    }
}