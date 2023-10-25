package com.ohgiraffers.section03.projection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

//연관 관계를 맺은
@Entity(name = "bidirection_category")
@Table(name = "tbl_category")
public class BiDirectionCategory {

    @Id
    private int categoryCode;
    private String categoryName;
    private Integer refCategoryCode;
    //카테고리는 여러 메뉴를 가질 수 있기 떄문에
    @OneToMany(mappedBy = "category")
    //joinColumn을 붙이면 안됨, 양방향일 경우 주인은 JoinColumn을 붙이지만, 여기에는 mappedBy를 붙여야 된다.
    private List<BiDirectionMenu> menuList;

    public BiDirectionCategory() {
    }

    public BiDirectionCategory(int categoryCode, String categoryName, Integer refCategoryCode, List<BiDirectionMenu> menuList) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.refCategoryCode = refCategoryCode;
        this.menuList = menuList;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getRefCategoryCode() {
        return refCategoryCode;
    }

    public void setRefCategoryCode(Integer refCategoryCode) {
        this.refCategoryCode = refCategoryCode;
    }

    public List<BiDirectionMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<BiDirectionMenu> menuList) {
        this.menuList = menuList;
    }

    @Override
    public String toString() {
        return "BiDirectionCategory{" +
                "categoryCode=" + categoryCode +
                ", categoryName='" + categoryName + '\'' +
                ", refCategoryCode=" + refCategoryCode +
                //", menuList=" + menuList +
                '}';
    }

}
