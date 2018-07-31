package pl.walasik.wypozyczalnia.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "product_details")
public class ProductDetails {

    private String id = UUID.randomUUID().toString();
    private Product product;
    private String size;

    public ProductDetails() {
    }

    public ProductDetails(Product product, String size) {
        this.product = product;
        this.size = size;
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Basic
    @Column(name = "size")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}