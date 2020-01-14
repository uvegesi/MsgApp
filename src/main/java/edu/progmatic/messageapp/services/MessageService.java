package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Message;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Service
public class MessageService {

    private List<Message> messages = new ArrayList<>();

    {
        messages.add(new Message("Aladár", "Mz/x jelkezz", LocalDateTime.now()));
        messages.add(new Message("Kriszta", "Bemutatom lüke Aladárt", LocalDateTime.now()));
        messages.add(new Message("Blöki", "Vauvau", LocalDateTime.now()));
        messages.add(new Message("MAffia", "Miau", LocalDateTime.now()));
        messages.add(new Message("Aladár", "Kapcs/ford", LocalDateTime.now()));
        messages.add(new Message("Aladár", "Adj pénzt", LocalDateTime.now()));
    }

    public List<Message> filterMessage(Long id, String author, String text, LocalDateTime from, LocalDateTime to,
                                       String orderby, String order, Integer limit) {

        Comparator<Message> messageComparator = Comparator.comparing(Message::getCreationDate);
        switch (orderby) {
            case "text":
                messageComparator = Comparator.comparing(Message::getText);
                break;
            case "id":
                messageComparator = Comparator.comparing(Message::getId);
                break;
            case "author":
                messageComparator = Comparator.comparing(Message::getAuthor);
                break;
        }
        if (order.equals("desc")) {
            messageComparator.reversed();
        }
        List<Message> msgs = messages.stream()
                .filter(m-> id == null || m.getId().equals(id))
                .filter(m-> StringUtils.isEmpty(author) || m.getAuthor().contains(author))
                .filter(m-> StringUtils.isEmpty(text) || m.getText().contains(text))
                .filter(m-> from == null || m.getCreationDate().isAfter(from))
                .filter(m-> to == null || m.getCreationDate().isBefore(to))
                .sorted(messageComparator)
                .limit(limit).collect(Collectors.toList());

        return msgs;
    }

    public Message getMessage(Long msgId) {
        Optional<Message> message = messages.stream().filter(m -> m.getId().equals(msgId)).findFirst();
        return message.get();
    }

    public void createMessage(Message m) {
        m.setId((long)messages.size());
        messages.add(m);
    }

    @RequestMapping(value = "/msgTab", method = GET)
    public String msgTable(@RequestParam(name = "numOfMsg", defaultValue = "100", required = false) Integer numOfMsg,
                           Model model) {
        List<Message> displayedMsgs = new ArrayList<>();
        for (int i = 0; i < numOfMsg; i++) {
            displayedMsgs.add(messages.get(i));
        }
        model.addAttribute("messages", displayedMsgs);
        return "messages";
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public int getNumOfMsg() {
        return messages.size();
    }
}
