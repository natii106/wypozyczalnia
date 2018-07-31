package pl.walasik.wypozyczalnia.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "product_category")
public class ProductCategory {

    private String id = UUID.randomUUID().toString();
    private String name;
    private boolean subcategory;
    private String parentId;

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
    @Column(name = "subcategory")
    public boolean isSubcategory() {
        return subcategory;
    }

    public void setSubcategory(boolean subcategory) {
        this.subcategory = subcategory;
    }

    @Basic
    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
