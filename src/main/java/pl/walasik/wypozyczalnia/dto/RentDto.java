package pl.walasik.wypozyczalnia.dto;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RentDto {

    private String productId;
    private Date from;
    private Date to;

    public RentDto() {
    }

    public RentDto(String productId, Date from, Date to) {
        this.productId = productId;
        this.from = from;
        this.to = to;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "RentDto{" +
                "productId='" + productId + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
