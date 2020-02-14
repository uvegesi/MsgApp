package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.dto.CreateMsgDto;
import edu.progmatic.messageapp.dto.MessageRepDTO;
import edu.progmatic.messageapp.exceptions.MessageNotFoundException;
import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Message_;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.modell.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @PersistenceContext
    EntityManager em;

    /*private List<Message> messages = new ArrayList<>();

    {
        messages.add(new Message("Aladár", "Mz/x jelkezz", LocalDateTime.now()));
        messages.add(new Message("Kriszta", "Bemutatom lüke Aladárt", LocalDateTime.now()));
        messages.add(new Message("Blöki", "Vauvau", LocalDateTime.now()));
        messages.add(new Message("Maffia", "Miau", LocalDateTime.now()));
        messages.add(new Message("Aladár", "Kapcs/ford", LocalDateTime.now()));
        messages.add(new Message("Aladár", "Adj pénzt", LocalDateTime.now()));
    }

     */

    @Transactional
    public List<Message> filterMessage(Long id, String author, String text, LocalDateTime from, LocalDateTime to,
                                       String orderby, String order, Integer limit, boolean isDeleted, Topic topic) {

        /*List<Message> messages = em.createQuery(
                "SELECT m FROM Message m", Message.class)
                .getResultList();

         */

        /*CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Message> messageCriteriaQuery = cb.createQuery(Message.class);
        Root<Message> messageFrom = messageCriteriaQuery.from(Message.class);
        messageCriteriaQuery.select(messageFrom);
        List<Message> messages = em.createQuery(messageCriteriaQuery).getResultList();

         */

        LOGGER.info("filterMessages method started");
        LOGGER.debug("id: {}, author: {}, text: {}", id, author, text);

        //Comparator<Message> messageComparator = Comparator.comparing(Message::getCreationDate);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Message> msgQuery = cb.createQuery(Message.class);
        Root<Message> msgRoot = msgQuery.from(Message.class);
        List<Predicate> predicates = new ArrayList<>();

        if (from != null) {
            predicates.add(cb.greaterThan(msgRoot.get(Message_.creationDate), from));
        }

        if (to != null){
            predicates.add(cb.lessThan(msgRoot.get(Message_.creationDate), to));
        }
        if (!StringUtils.isEmpty(author)) {
            predicates.add(cb.equal(msgRoot.get(Message_.author), author));
        }
        if (!StringUtils.isEmpty(text)) {
            predicates.add(cb.equal(msgRoot.get(Message_.text), text));
        }
        if (id != null) {
            predicates.add(cb.equal(msgRoot.get(Message_.id), id));
        }
        if (!isDeleted) {
            predicates.add(cb.equal(msgRoot.get(Message_.isDeleted), isDeleted));
        }

        msgQuery.select(msgRoot).where(predicates.toArray(new Predicate[0]));

        LOGGER.debug("filterMessages method started");

        Expression orderByExpression =null;
        switch (orderby) {
            case "text":
                //messageComparator = Comparator.comparing(Message::getText);
                orderByExpression = msgRoot.get(Message_.text);
                break;
            case "author":
                //messageComparator = Comparator.comparing(m -> m.getAuthor().getUsername());
                orderByExpression = msgRoot.get(Message_.author);
                break;
            case"isDeleted":
                if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                    //messageComparator = Comparator.comparing(Message::isDeleted);
                    orderByExpression = msgRoot.get(Message_.IS_DELETED);
                }
                break;
            default:
                //messageComparator = Comparator.comparing(Message::getId);
                orderByExpression = msgRoot.get(Message_.id);
                break;
        }

        if (order.equals("desc")) {
            //messageComparator.reversed();
            msgQuery.orderBy(
                    cb.desc(orderByExpression));
            } else {
            msgQuery.orderBy(cb.asc(orderByExpression));
        }
        /*List<Message> msgs = messages.stream()
                .filter(m -> id == null || m.getId().equals(id))
                .filter(m -> StringUtils.isEmpty(author) || m.getAuthor().getUsername().contains(author))
                .filter(m -> StringUtils.isEmpty(text) || m.getText().contains(text))
                .filter(m -> from == null || m.getCreationDate().isAfter(from))
                .filter(m -> to == null || m.getCreationDate().isBefore(to))
                .filter(m -> !isDeleted || m.isDeleted())
                .sorted(messageComparator)
                .limit(limit).collect(Collectors.toList());

        return msgs;

         */

        return em.createQuery(msgQuery).getResultList();
    }

    public Message getMessage(Long msgId) {
        //Optional<Message> message = messages.stream().filter(m -> m.getId().equals(msgId)).findFirst();
        Message message = em.find(Message.class, msgId);
        if (message == null) {
            throw new MessageNotFoundException();
        } else {
            return message;
        }
    }

    /*@Transactional
    public void createMessage(Message m) {
        //m.setId((long) messages.size());
        String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        m.setAuthor(loggedInUserName);
        m.setCreationDate(LocalDateTime.now());
        em.persist(m);
        //messages.add(m);
    }
     */

    @Transactional
    public Message addMessage(CreateMsgDto message) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message newMessage = new Message();
        Topic newTopic = new Topic();
        newTopic.setTopicId(message.getTopicId());
        newMessage.setText(message.getText());
        newMessage.setAuthor(user);
        newMessage.setCreationDate(LocalDateTime.now());
        newMessage.setTopic(newTopic);
        //messages.add(message);
        em.persist(newMessage);
        em.refresh(newMessage); //Kell, hogy legyen topic.author
        return newMessage;
    }

    @Transactional
    public void getMessageToModify(long msgId, String newText, Long sleep) {
        Message message = em.find(Message.class, msgId);
        message.setText(newText);
        if(sleep != null){
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public int getNumOfMsg() {
         return (int) em.createQuery("SELECT count(m) from Message m").getSingleResult();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteMessage(long msgId) {
        //messages.remove(message);
        //messages.removeIf()
        Message message = getMessage(msgId);
        // ezzel is működik -- Message message1 = em.find(Message.class, msgId);
        /*if (message != null) {
            return false;
        } else {
            return true;
        }
         */

        message.setDeleted(true);
    }

    public List<MessageRepDTO> messageList() {
        List<Message> msgs = em.createQuery("select m from Message m", Message.class).getResultList();
        List<MessageRepDTO> dtoMsgs = new ArrayList<>();
        for (Message message : msgs) {
            MessageRepDTO dto = new MessageRepDTO();
            dto.setAuthor(message.getAuthor().getUserName());
            dto.setCreationDate(message.getCreationDate());
            dto.setId(message.getId());
            dto.setText(message.getText());
            dto.setTopic(message.getTopic().getTopicTheme());
            dtoMsgs.add(dto);
        }
        return dtoMsgs;

        //return em.createQuery("SELECT t FROM Topic t", Topic.class).getResultList();
        //return em.createQuery("SELECT m from Message m", Message.class).getResultList();
    }

}
