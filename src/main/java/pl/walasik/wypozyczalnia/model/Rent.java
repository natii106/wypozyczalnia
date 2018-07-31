package pl.walasik.wypozyczalnia.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Rent {
    private String id = UUID.randomUUID().toString();
    private String number;
    private String status;
    private Long price;
    private User user;
    private Payment payment;
    private List<RentDates> rentDates;

    public Rent() {
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "price")
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @OneToMany(targetEntity = RentDates.class)
    @JoinColumn(name = "rent_id")
    public List<RentDates> getRentDates() {
        return rentDates;
    }

    public void setRentDates(List<RentDates> rentDates) {
        this.rentDates = rentDates;
    }
}
