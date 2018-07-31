package pl.walasik.wypozyczalnia.web;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.walasik.wypozyczalnia.dao.UserDAO;
import pl.walasik.wypozyczalnia.dto.UserDto;
import pl.walasik.wypozyczalnia.model.User;
import pl.walasik.wypozyczalnia.model.UserTokenState;
import pl.walasik.wypozyczalnia.security.JWTAuthenticationRequest;
import pl.walasik.wypozyczalnia.security.TokenHelper;
import pl.walasik.wypozyczalnia.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/rest/auth")
public class AuthenticationController {

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JWTAuthenticationRequest authenticationRequest,
                                                       HttpServletResponse response) throws AuthenticationException {
        //move to security service
        if (userDAO.findByLogin(authenticationRequest.getUsername()) == null) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).build();
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jws = tokenHelper.generateToken(user.getUsername());
        int expiresIn = tokenHelper.getExpiredIn();
        UserDto dto = userService.mapToUserDto(user);
        dto.setToken(jws);
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity createNewUser(@RequestBody User user) {
        UserDetails userExists = userDAO.findByLogin(user.getUsername());
        if (userExists != null) {
            return ResponseEntity.status(HttpStatus.SC_CONFLICT).build();
        }
        userService.register(user);
        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshAuthenticationToken(
            HttpServletRequest request,
            HttpServletResponse response,
            Principal principal
    ) {

        String authToken = tokenHelper.getToken(request);


        if (authToken != null && principal != null) {
            String refreshedToken = tokenHelper.refreshToken(authToken);
            int expiresIn = tokenHelper.getExpiredIn();
            return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
        } else {
            UserTokenState userTokenState = new UserTokenState();
            return ResponseEntity.accepted().body(userTokenState);
        }
    }

    @RequestMapping(value = "/userByToken", method = RequestMethod.POST)
    public ResponseEntity getUserByToken(HttpServletRequest request) {
        String authToken = tokenHelper.getToken(request);
        String username = tokenHelper.getUsernameFromToken(authToken);
        User user = userDAO.findByLogin(username);
        UserDto dto = userService.mapToUserDto(user);
        dto.setToken(authToken);
        return ResponseEntity.ok(dto);
    }
}
