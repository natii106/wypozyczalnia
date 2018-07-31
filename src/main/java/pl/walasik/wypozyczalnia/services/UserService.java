package pl.walasik.wypozyczalnia.services;

import pl.walasik.wypozyczalnia.dto.AddressDto;
import pl.walasik.wypozyczalnia.dto.UserDto;
import pl.walasik.wypozyczalnia.model.User;
import pl.walasik.wypozyczalnia.model.UserAddress;

public interface UserService {

    void register(User user);

    User loadUserByUsername(String username);

    User login(User user);

    UserDto mapToUserDto(User user);

    AddressDto mapToAddressDto(UserAddress address);

    UserAddress mapToUserAddress(AddressDto dto);

    User mapToUser(UserDto dto);
}
