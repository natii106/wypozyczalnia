package pl.walasik.wypozyczalnia.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.walasik.wypozyczalnia.dao.PaymentDAO;
import pl.walasik.wypozyczalnia.dao.RentDAO;
import pl.walasik.wypozyczalnia.dao.RentDatesDAO;
import pl.walasik.wypozyczalnia.dao.UserDAO;
import pl.walasik.wypozyczalnia.dto.RentDto;
import pl.walasik.wypozyczalnia.model.Payment;
import pl.walasik.wypozyczalnia.model.Rent;
import pl.walasik.wypozyczalnia.model.RentDates;
import pl.walasik.wypozyczalnia.security.TokenHelper;
import pl.walasik.wypozyczalnia.services.RentService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("api/rest/rent")
@Transactional
public class RentController {

    private RentService rentService;
    private UserDAO userDAO;
    private TokenHelper tokenHelper;
    private RentDAO rentDAO;
    private PaymentDAO paymentDAO;
    private RentDatesDAO rentDatesDAO;

    @Autowired
    public RentController(RentService rentService, UserDAO userDAO, TokenHelper tokenHelper,
                          RentDAO rentDAO, PaymentDAO paymentDAO, RentDatesDAO rentDatesDAO) {
        this.rentService = rentService;
        this.userDAO = userDAO;
        this.tokenHelper = tokenHelper;
        this.rentDAO = rentDAO;
        this.paymentDAO = paymentDAO;
        this.rentDatesDAO = rentDatesDAO;
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public ResponseEntity processNewRent(@RequestBody List<RentDto> dto, HttpServletRequest request) {
        String token = tokenHelper.getToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Rent rent = new Rent();
        rent.setStatus("ACCEPTED");
        rent.setNumber(rentService.generateRentNumber());
        Payment payment = new Payment("new");
        rent.setPayment(payment);
        rent.setUser(userDAO.getUserFromToken(token));

        List<RentDates> rentDates = new ArrayList<>();
        for (RentDto rentDto : dto) {
            RentDates rentDate = new RentDates();
            rentDate.setDateFrom(rentDto.getFrom());
            rentDate.setDateTo(rentDto.getTo());
            rentDate.setProductDetails(rentService.mapToProductDetails(rentDto.getProductId()));
            rentDates.add(rentDate);
        }
        rent.setRentDates(rentDates);
        rent.setPrice(rentService.calculateTotalPrice(rentDates));
        for (RentDates rentDate : rent.getRentDates()) {
            rentDatesDAO.saveRentDates(rentDate);
        }
        paymentDAO.setPayment(payment);
        rentDAO.save(rent);
        return ResponseEntity.ok(rent.getPrice());
    }

    private List<RentDates> transformRentDates(List<RentDates> rentDates, RentDates calculatedRentDates) {
        List<RentDates> newRentDates = new ArrayList<>();
        for (RentDates rentDate : rentDates) {
            RentDates newDate = new RentDates();
            newDate.setDateFrom(calculatedRentDates.getDateFrom());
            newDate.setDateTo(calculatedRentDates.getDateTo());
            newDate.setProductDetails(rentDate.getProductDetails());
            newRentDates.add(newDate);
        }
        return newRentDates;
    }
}
