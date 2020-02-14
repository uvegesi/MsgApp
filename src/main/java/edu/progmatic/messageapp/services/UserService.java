package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @PersistenceContext
    EntityManager em;

    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*if(users.containsKey(username)) {
            return users.get(username);
        } else {
            throw new UsernameNotFoundException("User not found" + username);
        }

         */
        return em.createQuery("SELECT u from User u left join fetch u.authorities where u.userName = :username", User.class)
                .setParameter("username", username).getSingleResult();
    }

    @Transactional
    public void createUser(User user) {
        em.persist(user);
    }

    @Transactional
    public boolean userExists(String username) {
        /*boolean user = false;
        if(users.containsKey(username)) {
            user= true;
        }
        return user;

         */
        try {
            User ur = em.createQuery("select u from User u where u.userName = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            LOGGER.debug("User found:  {}", ur);
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public User getUser(long id) {
        return em.find(User.class, id);
    }
}
