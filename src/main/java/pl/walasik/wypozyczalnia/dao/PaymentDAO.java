package pl.walasik.wypozyczalnia.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.walasik.wypozyczalnia.model.Payment;
import pl.walasik.wypozyczalnia.web.ProductController;

import javax.transaction.Transactional;

@Repository
@Transactional
public class PaymentDAO {

    private final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void setPayment(Payment payment) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(payment);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
