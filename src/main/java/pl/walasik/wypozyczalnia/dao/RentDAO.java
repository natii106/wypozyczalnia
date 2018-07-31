package pl.walasik.wypozyczalnia.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.walasik.wypozyczalnia.model.Rent;
import pl.walasik.wypozyczalnia.services.TimeProvider;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class RentDAO {

    private final SessionFactory sessionFactory;
    private final TimeProvider timeProvider;

    @Autowired
    public RentDAO(SessionFactory sessionFactory, TimeProvider timeProvider) {
        this.sessionFactory = sessionFactory;
        this.timeProvider = timeProvider;
    }

    public void save(Rent rent) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(rent);
        } catch (HibernateException e) {
            throw new HibernateException(e.getMessage());
        }
    }

    public List<Rent> getActualRents() {
        Session session = sessionFactory.getCurrentSession();
        List<Rent> rents = new ArrayList<>();
        try {
            rents = session.createCriteria(Rent.class).list();
        } catch (HibernateException e) {
            return rents;
        }
        return rents;
    }

    public List<Rent> getRentsForUser(String userId) {
        Session session = sessionFactory.getCurrentSession();
        List<Rent> rents = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Rent.class);
            criteria.add(Restrictions.eq("user.id", userId));
            rents = criteria.list();
        } catch (HibernateException e) {
            return rents;
        }
        return rents;
    }
}
