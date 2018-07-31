package pl.walasik.wypozyczalnia.web;

import org.apache.http.HttpStatus;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.walasik.wypozyczalnia.dao.ProductDAO;
import pl.walasik.wypozyczalnia.model.Image;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@RestController
@RequestMapping("api/rest/images")
@Transactional
public class ImageController {

    protected final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ProductDAO productDAO;

    @POST
    @RequestMapping("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity uploadFile(@RequestParam(value = "image") MultipartFile multipartFile,
                                     @RequestParam(value = "productId") String productId) throws IOException {
        InputStream inputStream = new BufferedInputStream(multipartFile.getInputStream());
        byte[] imageBytes = new byte[(int) multipartFile.getSize()];
        inputStream.read(imageBytes, 0, imageBytes.length);
        inputStream.close();
        String imageStr = Base64.getEncoder().encodeToString(imageBytes);

        Image image = new Image();
        image.setFile(imageStr);
        image.setName(imageStr.substring(0, 15));
        productDAO.saveImage(productId, image);
        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }

    @GET
    @RequestMapping("/image/{imageId}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity getFile(@PathVariable String imageId) {
        Session session = sessionFactory.getCurrentSession();
        LOG.info("Session created");
        Image image;
        try {
            image = session.get(Image.class, imageId);
        } catch (HibernateException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(image);
    }
}
