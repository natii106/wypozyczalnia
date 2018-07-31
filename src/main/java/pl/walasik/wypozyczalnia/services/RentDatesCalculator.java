package pl.walasik.wypozyczalnia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.walasik.wypozyczalnia.dao.ProductDAO;
import pl.walasik.wypozyczalnia.model.RentDates;

import java.util.List;

@Service
public class RentDatesCalculator {

    @Autowired
    private ProductDAO productDAO;

    public List<RentDates> calculateUnavailableRentDates(String productDetailsId) {
        return productDAO.getRentDatesForProductDetails(productDetailsId);
    }
}
