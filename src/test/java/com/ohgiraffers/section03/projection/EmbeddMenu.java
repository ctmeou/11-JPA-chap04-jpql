package com.ohgiraffers.section03.projection;

import javax.persistence.*;

//embedded : 포함된다
@Entity(name = "embedded_menu")
@Table(name = "tbl_menu")
public class EmbeddMenu {

    @Id
    private int menuCode;
    private String menuName;
    private int menuPrice;
    //메뉴 입장에서는 하나의 메뉴 당 하나의 카테고리
    @ManyToOne
    @JoinColumn(name = "categoryCode")
    private BiDirectionCategory category;
    private String orderableStatus;

    public EmbeddMenu() {
    }

    public EmbeddMenu(int menuCode, String menuName, int menuPrice, BiDirectionCategory category, String orderableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
        this.orderableStatus = orderableStatus;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public BiDirectionCategory getCategory() {
        return category;
    }

    public void setCategory(BiDirectionCategory category) {
        this.category = category;
    }

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return "BiDirectionMenu{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", category=" + category +
                ", orderableStatus='" + orderableStatus + '\'' +
                '}';
    }

}
