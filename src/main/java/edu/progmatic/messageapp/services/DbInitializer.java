package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Authority;
import edu.progmatic.messageapp.modell.User;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;

@Component
public class DbInitializer {

    @PersistenceContext
    EntityManager em;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @EventListener(ContextRefreshedEvent.class)
    public void onAppStartup(ContextRefreshedEvent ev) throws ServletException {
        DbInitializer me = ev.getApplicationContext().getBean(DbInitializer.class);
        me.init();
    }

    @Transactional
    public void init() {
        createAuthoritiesIfNotExist();
        createUserIfNotExist();
    }

    public void createAuthoritiesIfNotExist() {
        long authorityCount = em.createQuery("select count(a) from Authority a", Long.class).getSingleResult();
        if (authorityCount == 0) {
            Authority user = new Authority("ROLE_USER");
            Authority admin = new Authority("ROLE_ADMIN");
            em.persist(user);
            em.persist(admin);
        }
    }

    private void createUserIfNotExist() {
        long userDbCnt = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
        if (userDbCnt == 1) {
            Authority userAuth = em.createQuery("select a from Authority a where a.name = 'ROLE_USER'",
                    Authority.class).getSingleResult();
            Authority adminAuth = em.createQuery("select a from Authority a where a.name = 'ROLE_ADMIN'",
                    Authority.class).getSingleResult();

            User user = new User("user", "sanyi", "sanyika",
                    LocalDate.of(1986, 03, 24), "pass",
                    "pass", "sanyi@s.com" );
            user.setAuthorities(Collections.singleton(userAuth));

            User admin = new User("admin", "alexa", "alexia",
                    LocalDate.of(1986, 03, 24),
                    "pass", "pass", "alexa@a.com");
            admin.setAuthorities(Collections.singleton(adminAuth));
            //long auId = new Authority().getId();
            //Authority userId = em.find(Authority.class, auId);
            //userAuth.setId(auId);
            //adminAuth.setId(auId);
            String encodeUserPass = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodeUserPass);
            String encodePass = passwordEncoder.encode(admin.getPassword());
            admin.setPassword(encodePass);
            em.persist(user);
            em.persist(admin);
        }
    }
}
   // String userName, String firstName, String lastName, LocalDate dOB, String password,
     //   String matchingPassword, String email