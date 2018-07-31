package pl.walasik.wypozyczalnia.dto;

import java.util.List;

public class AdminRentDto {

    //jakies info o produkcie - nazwa, kategoria>?
    List<RentDto> rentDto;
    String user;
    String status;


    public List<RentDto> getRentDto() {
        return rentDto;
    }

    public void setRentDto(List<RentDto> rentDto) {
        this.rentDto = rentDto;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AdminRentDto{" +
                "rentDto=" + rentDto +
                ", user='" + user + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
