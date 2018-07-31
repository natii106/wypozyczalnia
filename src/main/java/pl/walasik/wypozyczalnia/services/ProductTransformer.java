package pl.walasik.wypozyczalnia.services;

import org.springframework.stereotype.Service;
import pl.walasik.wypozyczalnia.dto.AddedProductDto;
import pl.walasik.wypozyczalnia.dto.ProductDetailsDto;
import pl.walasik.wypozyczalnia.model.Product;

import java.util.UUID;

@Service
public class ProductTransformer {

    public Product mapToProduct(AddedProductDto productDto) {
        Product product = new Product();
        product.setColor(productDto.getColor());
        product.setDescription(productDto.getDescription());
        product.setImages(productDto.getImages());
        product.setBrand(productDto.getMake());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getProductCategory());
        product.setReview(productDto.getReview());
        product.setId(UUID.randomUUID().toString());
        return product;
    }

    public ProductDetailsDto mapToProductDetails(Product product) {
        ProductDetailsDto dto = new ProductDetailsDto();
        dto.setColor(product.getColor());
        dto.setDescription(product.getDescription());
        dto.setImages(product.getImages());
        dto.setMake(product.getBrand());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setProductCategory(product.getCategory());
        dto.setId(product.getId());
        dto.setReview(product.getReview());
        return dto;
    }
}
