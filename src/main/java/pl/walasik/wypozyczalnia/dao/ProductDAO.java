package pl.walasik.wypozyczalnia.dao;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.walasik.wypozyczalnia.model.Image;
import pl.walasik.wypozyczalnia.model.Product;
import pl.walasik.wypozyczalnia.model.ProductDetails;
import pl.walasik.wypozyczalnia.model.RentDates;
import pl.walasik.wypozyczalnia.web.ProductController;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class ProductDAO {

    private final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private SessionFactory sessionFactory;

    public String saveProduct(Product product) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(product);
            LOG.info("Added product with id {}", product.getId());
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            tx.rollback();
            throw new HibernateException(e.getMessage());
        }
        return product.getId();
    }

    public void saveSizes(String size, Product product) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            ProductDetails details = new ProductDetails(product, size);
            session.save(details);
            tx.commit();
            session.close();
        } catch (Exception e) {
            tx.rollback();
            throw new HibernateException("Could not save sizes for product. " + e.getMessage());
        }
    }

    public void saveImage(String productId, Image image) {
        Session session = sessionFactory.getCurrentSession();
        LOG.info("Session created");
        Product product;
        try {
            product = session.get(Product.class, productId);
            image.setProduct(product);
            session.save(image);
        } catch (HibernateException e) {
            throw new HibernateException("Could not save image" + e.getMessage());
        }
    }

    public Product getProduct(String productId) {
        Session session = sessionFactory.getCurrentSession();
        Product productResult = new Product();
        try {
            if (productId == null) {
                return productResult;
            }
            productResult = session.get(Product.class, productId);
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
        }
        return productResult;
    }

    public List<ProductDetails> getAvailableSizes(String productId) {
        Session session = sessionFactory.getCurrentSession();
        List<ProductDetails> details;
        try {
            details = (List<ProductDetails>) session.createCriteria(ProductDetails.class)
                    .add(Restrictions.eq("product.id", productId))
                    .list();
        } catch (HibernateException e) {
            throw new HibernateException(e.getMessage());
        }
        return details;
    }

    public List<RentDates> getRentDatesForProductDetails(String productDetailsId) {
        Session session = sessionFactory.getCurrentSession();
        List<RentDates> rentDates;
        try {
            rentDates = (List<RentDates>) session.createCriteria(RentDates.class)
                    .add(Restrictions.eq("productDetails.id", productDetailsId))
                    .list();
        } catch (HibernateException e) {
            throw new HibernateException(e.getMessage());
        }
        return rentDates;
    }

    public List<Product> getProductsByCategory(String categoryId) {
        List<Product> productsList;
        Session session = sessionFactory.getCurrentSession();
        LOG.info("Session created");
        try {
            if (categoryId == null) {
                productsList = Collections.emptyList();
            }
            productsList = session.createCriteria(Product.class).createCriteria("category")
                    .add(Restrictions.eq("id", categoryId))
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .list();
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            productsList = Collections.emptyList();
        }
        return productsList;
    }

    public ProductDetails productDetailsById(String id) {
        ProductDetails details = new ProductDetails();
        Session session = sessionFactory.getCurrentSession();
        try {
            if (id == null) {
                return details;
            }
            details = session.get(ProductDetails.class, id);
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
        }
        return details;
    }
}