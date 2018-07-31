package pl.walasik.wypozyczalnia.dto;

import pl.walasik.wypozyczalnia.model.Image;
import pl.walasik.wypozyczalnia.model.ProductCategory;

import java.util.List;

public class ProductDetailsDto {

    private String id;
    private String name;
    private String description;
    private int price;
    private String color;
    private String make;
    private ProductCategory productCategory;
    private String review;
    private List<Image> images;
    private List<RentDatesInfoDto> sizesWithDates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<RentDatesInfoDto> getSizesWithDates() {
        return sizesWithDates;
    }

    public void setSizesWithDates(List<RentDatesInfoDto> sizesWithDates) {
        this.sizesWithDates = sizesWithDates;
    }

    @Override
    public String toString() {
        return "ProductDetailsDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", color='" + color + '\'' +
                ", make='" + make + '\'' +
                ", productCategory=" + productCategory +
                ", review='" + review + '\'' +
                ", images=" + images +
                ", sizesWithDates=" + sizesWithDates +
                '}';
    }
}
