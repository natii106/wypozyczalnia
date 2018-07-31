package pl.walasik.wypozyczalnia.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.walasik.wypozyczalnia.dao.UserDAO;
import pl.walasik.wypozyczalnia.dto.UserDto;
import pl.walasik.wypozyczalnia.dto.UserRentDto;
import pl.walasik.wypozyczalnia.model.User;
import pl.walasik.wypozyczalnia.security.TokenHelper;
import pl.walasik.wypozyczalnia.services.RentService;
import pl.walasik.wypozyczalnia.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("api/rest/user")
@Transactional
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private RentService rentService;

    @Autowired
    private TokenHelper tokenHelper;

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntity edit(@RequestBody UserDto dto, HttpServletRequest request) {
        User user = userDAO.getUserFromToken(tokenHelper.getToken(request));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(userService.mapToUserAddress(dto.getAddress()));
        userDAO.update(user);
        return ResponseEntity.ok().build();

    }

    @RequestMapping(value = "/rent", method = RequestMethod.GET)
    public ResponseEntity getRents(HttpServletRequest request) {
        String token = tokenHelper.getToken(request);
        User user = userDAO.getUserFromToken(token);
        List<UserRentDto> rentsForUser = rentService.getRentsForUser(user.getId());
        return ResponseEntity.ok(rentsForUser);
    }

}
