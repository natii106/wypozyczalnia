package pl.walasik.wypozyczalnia.dto;

import java.util.List;

public class RentDatesInfoDto {

    private String size;
    private String productId;
    private List<RentDatesDto> rentDates;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<RentDatesDto> getRentDates() {
        return rentDates;
    }

    public void setRentDates(List<RentDatesDto> rentDates) {
        this.rentDates = rentDates;
    }
}
