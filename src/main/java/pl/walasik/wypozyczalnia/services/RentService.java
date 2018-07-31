package pl.walasik.wypozyczalnia.services;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.walasik.wypozyczalnia.dao.ProductDAO;
import pl.walasik.wypozyczalnia.dao.RentDAO;
import pl.walasik.wypozyczalnia.dto.UserRentDto;
import pl.walasik.wypozyczalnia.model.ProductDetails;
import pl.walasik.wypozyczalnia.model.Rent;
import pl.walasik.wypozyczalnia.model.RentDates;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RentService {

    private static final String NEW_RENT_PREFIX = "00N";

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RentDAO rentDAO;

    @Autowired
    private TimeProvider timeProvider;

    public String generateRentNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append(RentService.NEW_RENT_PREFIX).append(Math.random());
        return sb.toString();
    }

    public RentDates calculateDates(List<RentDates> rentDates) {
        Date now = timeProvider.now();
        Optional<RentDates> firstDate = rentDates.stream().findFirst();
        Date start = firstDate.isPresent() ? firstDate.get().getDateFrom() : now;
        Date end = firstDate.isPresent() ? firstDate.get().getDateTo() : now;
        for (RentDates rentDate : rentDates) {
            Date rentFrom = rentDate.getDateFrom();
            Date rentTo = rentDate.getDateTo();
            if (rentFrom.before(start)) {
                start = rentFrom;
            }
            if (rentTo.after(end)) {
                end = rentTo;
            }
        }
        return new RentDates(start, end);
    }

    public long calculateTotalPrice(List<RentDates> dates) {
        long sum = 0;
        for (RentDates date : dates) {
            long diffInMillies = Math.abs(date.getDateTo().getTime() - date.getDateFrom().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            int pricePerDay = date.getProductDetails().getProduct().getPrice();
            sum = Math.addExact(sum, Math.multiplyExact(diff, pricePerDay));

        }
        return sum;
    }

    public ProductDetails mapToProductDetails(String productDetailsId) {
        return productDAO.productDetailsById(productDetailsId);
    }

    public List<UserRentDto> getActualRents() {
        List<Rent> rents;
        try {
            rents = rentDAO.getActualRents();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
        List<UserRentDto> rentsDto = new ArrayList<>();

        for (Rent rent : rents) {
            UserRentDto dto = new UserRentDto();
            List<String> details = new ArrayList<>();
            for (RentDates dates : rent.getRentDates()) {
                details.add(dates.getProductDetails().getProduct().getName());
                dto.setFrom(dates.getDateFrom());
                dto.setTo(dates.getDateTo());
                dto.setPrice(rent.getPrice());
            }

            dto.setProducts(details);
            rentsDto.add(dto);
        }
        return rentsDto;
    }

    public List<UserRentDto> getRentsForUser(String userId) {
        List<Rent> rents;
        try {
            rents = rentDAO.getRentsForUser(userId);
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
        List<UserRentDto> rentsDto = new ArrayList<>();

        for (Rent rent : rents) {
            UserRentDto dto = new UserRentDto();
            List<String> details = new ArrayList<>();
            for (RentDates dates : rent.getRentDates()) {
                details.add(dates.getProductDetails().getProduct().getName());
                dto.setFrom(dates.getDateFrom());
                dto.setTo(dates.getDateTo());
                dto.setPrice(rent.getPrice());
            }
            dto.setProducts(details);
            rentsDto.add(dto);
        }
        return rentsDto;
    }

}
