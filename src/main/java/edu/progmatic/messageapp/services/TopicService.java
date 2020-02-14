package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.repositories.CustomTopicRepository;
import org.hibernate.sql.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TopicService {

    @PersistenceContext
    EntityManager em;

    private CustomTopicRepository topicRepository;

    @Autowired
    public TopicService(CustomTopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> topicsList() {
        //return em.createQuery("SELECT t FROM Topic t", Topic.class).getResultList();
        return topicRepository.findAll();
    }


    @Transactional
    public void addTopic(Topic topic) {
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        topic.setTopicAuthor(loggedInUser);
        //em.persist(topic);
        topicRepository.save(topic);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteTopic(long topicId){
        Topic topic = getTopic(topicId);
        //em.remove(topic);
        topicRepository.removeTopic(topicId);
    }

    public Topic getTopic(Long topicId) {
        //return em.find(Topic.class, topicId);
        return topicRepository.findByTopicId(topicId);
    }

    public Topic getTopicByTitle(String topicTheme) {
        return topicRepository.getTopicByTitle(topicTheme);
    }
}
