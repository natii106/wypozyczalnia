package pl.walasik.wypozyczalnia.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.walasik.wypozyczalnia.model.RentDates;
import pl.walasik.wypozyczalnia.web.ProductController;

import javax.transaction.Transactional;

@Transactional
@Repository
public class RentDatesDAO {

    private final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void saveRentDates(RentDates rentDates) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(rentDates);
        } catch (HibernateException e) {
            throw new HibernateException(e.getMessage());
        }
    }
}
