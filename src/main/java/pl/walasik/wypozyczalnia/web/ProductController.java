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
import pl.walasik.wypozyczalnia.dto.ProductDetailsDto;
import pl.walasik.wypozyczalnia.model.Product;
import pl.walasik.wypozyczalnia.model.ProductCategory;
import pl.walasik.wypozyczalnia.services.ProductService;
import pl.walasik.wypozyczalnia.services.ProductTransformer;
import pl.walasik.wypozyczalnia.services.RentDatesCalculator;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/rest/categories")
@Transactional
public class ProductController {

    private final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RentDatesCalculator rentDatesCalculator;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTransformer productTransformer;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity categoriesToChoose() {
        Session session = sessionFactory.getCurrentSession();
        try {
            List<ProductCategory> categories = session.createCriteria(ProductCategory.class).list();
            List<ProductCategory> filteredCategories = categories
                    .stream()
                    .filter(pc -> (!pc.getParentId().equals("0") && pc.isSubcategory()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredCategories);
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }


    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ResponseEntity categoriesOnView() {
        Session session = sessionFactory.getCurrentSession();
        try {
            List<ProductCategory> categories = session.createCriteria(ProductCategory.class).list();
            return ResponseEntity.ok(categories);
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @RequestMapping(value = "/{categoryId}/products/{id}", method = RequestMethod.GET)
    public ResponseEntity product(@PathVariable String id, @PathVariable String categoryId) {
        Product productResult = productDAO.getProduct(id);
        ProductDetailsDto dto = productTransformer.mapToProductDetails(productResult);
        dto.setSizesWithDates(productService.getSizesWithDates(productResult));
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/{categoryId}/products", method = RequestMethod.GET)
    public ResponseEntity products(@PathVariable String categoryId) {
        Session session = sessionFactory.getCurrentSession();
        List<Product> productsList = productDAO.getProductsByCategory(categoryId);
        return ResponseEntity.ok(productsList);
    }

    @RequestMapping(value = "/{categoryId}/products/{productId}", method = RequestMethod.PUT)
    public ResponseEntity editProduct(@RequestBody Product product, @PathVariable String categoryId, @PathVariable String productId) {
        Session session = sessionFactory.getCurrentSession();
        LOG.info("Session created");
        try {
            session.update(product);
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

}
