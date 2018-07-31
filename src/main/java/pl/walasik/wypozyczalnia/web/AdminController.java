package pl.walasik.wypozyczalnia.web;

import org.apache.http.HttpStatus;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.walasik.wypozyczalnia.dao.ProductDAO;
import pl.walasik.wypozyczalnia.dao.UserDAO;
import pl.walasik.wypozyczalnia.dto.AddedProductDto;
import pl.walasik.wypozyczalnia.dto.UserRentDto;
import pl.walasik.wypozyczalnia.model.Image;
import pl.walasik.wypozyczalnia.model.Product;
import pl.walasik.wypozyczalnia.model.ProductCategory;
import pl.walasik.wypozyczalnia.services.ProductTransformer;
import pl.walasik.wypozyczalnia.services.RentService;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("api/rest/admin")
@Transactional
public class AdminController {

    private final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ProductTransformer productDtoTransformer;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private RentService rentService;

    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/{categoryId}/product/add", method = RequestMethod.POST, produces = "text/plain")
    public ResponseEntity addProduct(@RequestBody AddedProductDto productDto, @PathVariable String categoryId) {
        Session session = sessionFactory.getCurrentSession();
        Product product;
        try {
            ProductCategory productDtoCategory = session.get(ProductCategory.class, categoryId);
            productDto.setProductCategory(productDtoCategory);
            product = productDtoTransformer.mapToProduct(productDto);
            productDAO.saveProduct(product);
            List<String> sizes = productDto.getSizes();
            if (sizes.size() > 0) {
                sizes.forEach(s -> productDAO.saveSizes(s, product));
            } else {
                throw new HibernateException("No available sizes for the product with id:" + product.getId());
            }
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(product.getId());
    }

    @RequestMapping(value = "/{categoryId}/product/{productId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity deleteProduct(@PathVariable String productId, @PathVariable String categoryId) {
        Session session = sessionFactory.getCurrentSession();
        Product product = productDAO.getProduct(productId);
        LOG.info("Session created");
        try {
            deleteImages(product.getImages());
            session.delete(product);
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    private void deleteImages(List<Image> images) {
        Session session = sessionFactory.getCurrentSession();
        LOG.info("Session created");
        try {
            for (Image image : images) {
                session.delete(image);
            }
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/rent/all", method = RequestMethod.GET)
    public List<UserRentDto> getRents() {
        return rentService.getActualRents();
    }

    @RequestMapping(value = "/user/{userId}/delete", method = RequestMethod.POST)
    public void deleteUser(@RequestParam String userId) {
        userDAO.deleteUser(userId);
    }
}
