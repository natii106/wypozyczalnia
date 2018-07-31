package pl.walasik.wypozyczalnia.dao;

import org.apache.http.HttpStatus;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.walasik.wypozyczalnia.model.User;
import pl.walasik.wypozyczalnia.security.TokenHelper;
import pl.walasik.wypozyczalnia.web.ProductController;

import javax.transaction.Transactional;

@Transactional
@Repository
public class UserDAO {

    private final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final SessionFactory sessionFactory;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenHelper tokenHelper;


    @Autowired
    public UserDAO(SessionFactory sessionFactory, BCryptPasswordEncoder bCryptPasswordEncoder, TokenHelper tokenHelper) {
        this.sessionFactory = sessionFactory;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenHelper = tokenHelper;
    }

    public ResponseEntity save(User user) {
        Session session = sessionFactory.getCurrentSession();
        LOG.info("Session created");
        try {
            if (emailExist(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.SC_CONFLICT).build();
            }
            session.save(user);
            LOG.info("Added user with id {}", user.getId());
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }


    public ResponseEntity update(User user) {
        Session session = sessionFactory.getCurrentSession();
        LOG.info("Session created");
        try {
            updateAddress(user);
            session.update(user);
            LOG.info("Updated user with id {}", user.getId());
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }

    public ResponseEntity updateAddress(User user) {
        Session session = sessionFactory.getCurrentSession();
        LOG.info("Session created");
        try {
            session.saveOrUpdate(user.getAddress());
            LOG.info("Added user address with id {}", user.getId());
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }

    public User findByLogin(String login) {
        Session session = sessionFactory.getCurrentSession();
        User user;
        try {
            user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("username", login))
                    .uniqueResult();
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    public User findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        User user;
        try {
            user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("email", email));
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    private boolean emailExist(String email) {
        return findByEmail(email) != null;
    }

    public User getUserFromToken(String token) {
        String username = tokenHelper.getUsernameFromToken(token);
        return findByLogin(username);
    }

    public void deleteUser(String userId) {
        Session session = sessionFactory.getCurrentSession();
        LOG.info("Session created");
        try {
            User user = session.load(User.class, userId);
            session.delete(user);
            LOG.info("Deleted user with id {}", user.getId());
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
        }
    }
}
