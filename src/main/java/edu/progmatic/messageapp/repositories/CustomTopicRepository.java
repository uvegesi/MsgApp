package edu.progmatic.messageapp.repositories;

import edu.progmatic.messageapp.modell.Topic;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomTopicRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Topic> findAll()  {
        return em.createQuery("SELECT t FROM Topic t", Topic.class).getResultList();
    }

    public void save(Topic topic) {
        em.persist(topic);
    }

    public void removeTopic(long topicId) {
        em.remove(em.find(Topic.class, topicId));
    }

    public Topic findByTopicId(long topicId) {
       return em.find(Topic.class, topicId);
    }

    public Topic getTopicByTitle(String topicTheme) {
        //return em.find(Topic.class, topicTheme);
        return em.createQuery("select t from Topic t left join fetch t.messages where t.topicTheme = :topicTheme",
                Topic.class)
                .setParameter("topicTheme", topicTheme).getSingleResult();
    }
}
