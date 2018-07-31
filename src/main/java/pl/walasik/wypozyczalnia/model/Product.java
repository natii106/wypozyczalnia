package pl.walasik.wypozyczalnia.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Product {
    private String id = UUID.randomUUID().toString();
    private String name;
    private int price;
    private String color;
    private String description;
    private String brand;
    private String review;
    private ProductCategory category;
    private List<Image> images;

    public Product() {
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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "price")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Basic
    @Column(name = "color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Basic
    @Column(name = "brand")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Basic
    @Column(name = "review")
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> imageByProductId) {
        this.images = imageByProductId;
    }

}
