package pl.walasik.wypozyczalnia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.walasik.wypozyczalnia.dao.UserDAO;
import pl.walasik.wypozyczalnia.dto.AddressDto;
import pl.walasik.wypozyczalnia.dto.UserDto;
import pl.walasik.wypozyczalnia.model.User;
import pl.walasik.wypozyczalnia.model.UserAddress;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDAO userDAO;

    @Override
    public void register(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("user");
        ResponseEntity response = userDAO.save(user);
        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new RuntimeException(String.format("Problem with registration. User login %s", user.getUsername()));
        }
    }

    @Override
    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByLogin(username);
    }

    @Override
    public User login(User user) {
        return new User();
    }

    @Override
    public UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(mapToAddressDto(user.getAddress()));
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    public User mapToUser(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(mapToUserAddress(dto.getAddress()));
        user.setRole(dto.getRole());
        return user;
    }

    @Override
    public AddressDto mapToAddressDto(UserAddress address) {
        if (address == null) {
            return new AddressDto();
        }
        AddressDto dto = new AddressDto();
        dto.setCountry(address.getCountry());
        dto.setCity(address.getCity());
        dto.setPostalCode(address.getPostalCode());
        dto.setStreet(address.getStreet());
        dto.setStreetNo(address.getStreetNo());
        dto.setApartmentNo(address.getApartmentNo());
        return dto;
    }

    @Override
    public UserAddress mapToUserAddress(AddressDto dto) {
        UserAddress address = new UserAddress();
        address.setCountry(dto.getCountry());
        address.setPostalCode(dto.getPostalCode());
        address.setCity(dto.getCity());
        address.setStreet(dto.getStreet());
        address.setStreetNo(dto.getStreetNo());
        address.setApartmentNo(dto.getApartmentNo());
        return address;
    }

    private List<GrantedAuthority> getUserAuthority(String userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(userRoles));
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                true, true, true, true, authorities);
    }
}
