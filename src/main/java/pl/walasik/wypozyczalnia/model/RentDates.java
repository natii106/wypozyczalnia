package pl.walasik.wypozyczalnia.model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "rent_dates", schema = "wypozyczalnia")
public class RentDates {
    private String id = UUID.randomUUID().toString();
    private Date dateFrom;
    private Date dateTo;
    private ProductDetails productDetails;

    public RentDates() {

    }

    public RentDates(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
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
    @Column(name = "date_from")
    @Temporal(TemporalType.DATE)
    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    @Basic
    @Column(name = "date_to")
    @Temporal(TemporalType.DATE)
    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    @OneToOne(targetEntity = ProductDetails.class)
    @JoinColumn(name = "product_details_id")
    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }
}
