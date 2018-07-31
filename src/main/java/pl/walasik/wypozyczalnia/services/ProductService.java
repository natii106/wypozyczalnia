package pl.walasik.wypozyczalnia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.walasik.wypozyczalnia.dao.ProductDAO;
import pl.walasik.wypozyczalnia.dto.RentDatesDto;
import pl.walasik.wypozyczalnia.dto.RentDatesInfoDto;
import pl.walasik.wypozyczalnia.model.Product;
import pl.walasik.wypozyczalnia.model.ProductDetails;
import pl.walasik.wypozyczalnia.model.RentDates;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private RentDatesCalculator rentDatesCalculator;

    public List<RentDatesInfoDto> getSizesWithDates(Product product) {
        List<RentDatesInfoDto> sizesWithDates = new ArrayList<>();
        List<ProductDetails> details = productDAO.getAvailableSizes(product.getId());
        for (ProductDetails detail : details) {
            sizesWithDates.add(mapToRentDatesInfoDto(detail));
        }
        return sizesWithDates;
    }

    private RentDatesInfoDto mapToRentDatesInfoDto(ProductDetails details) {
        RentDatesInfoDto dto = new RentDatesInfoDto();
        dto.setSize(details.getSize());
        dto.setProductId(details.getId());
        dto.setRentDates(mapToRentDatesDto(rentDatesCalculator.calculateUnavailableRentDates(details.getId())));
        return dto;
    }

    private List<RentDatesDto> mapToRentDatesDto(List<RentDates> dates) {
        List<RentDatesDto> datesDto = new ArrayList<>();
        for (RentDates date : dates) {
            RentDatesDto dto = new RentDatesDto();
            dto.setFrom(date.getDateFrom());
            dto.setTo(date.getDateTo());
            datesDto.add(dto);
        }
        return datesDto;
    }
}
