package edu.progmatic.messageapp.repositories;

import edu.progmatic.messageapp.modell.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByTopicTheme(String topicTheme);
}
