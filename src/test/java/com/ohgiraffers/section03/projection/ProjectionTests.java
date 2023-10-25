package com.ohgiraffers.section03.projection;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class ProjectionTests {

    //application 당 1개만
    private static EntityManagerFactory entityManagerFactory;

    //스레드 세이프 하지 않고, 요청 당 1개
    private EntityManager entityManager;

    @BeforeAll //junit에서 오는 어노테이션, 테스트가 진행되기 전에 한 번 진행된다.
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach //테스트 하나가 진행되기 전에 한 번씩
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll //테스트가 끝나기 전에 한 번만
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach //테스트가 끝날 떄마다 한 번씩
    public void closeManager() {
        entityManager.close();
    }

    @Test
    public void 단일_엔티티_프로젝션_테스트() {

        // when
        String jpql = "SELECT m FROM menu_section03 m";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        // then
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        menuList.get(0).setMenuName("test");
        entityTransaction.commit();

    }

/*
    @Test
    public void 양방향_연관관계_엔티티_프로젝션_테스트() {

        // given
        int menuCodeParameter = 3;

        // when
        //메뉴 엔티티에 참조하도록 만들어 놔서 메뉴 엔티티의 카테고리 필드를 조회하도록 하더라도
        //대상은 카테고리 프로젝션, 전체를 select를 할 것은 아니고 menuCodeParameter에 대해 조회
        String jpql = "SELECT m.category FROM bidirection_menu m WHERE m.menuCode = :menuCode";
        //조회대상은 카테고리 엔티티
        BiDirectionCategory categoryOfMenu = entityManager.createQuery(jpql, BiDirectionCategory.class)
                //파라미터 설정
                .setParameter("menuCode", menuCodeParameter)
                .getSingleResult();

        // then
        System.out.println(categoryOfMenu);
        System.out.println(categoryOfMenu.getMenuList());

    }
*/

    @Test
    public void 양방향_연관관계_엔티티_프로젝션_테스트() {

        // given
        int menuCodeParameter = 3;

        // when
        String jpql = "SELECT m.category FROM bidirection_menu m WHERE m.menuCode = :menuCode";
        BiDirectionCategory categoryOfMenu = entityManager.createQuery(jpql, BiDirectionCategory.class)
                //파라미터 설정
                .setParameter("menuCode", menuCodeParameter)
                .getSingleResult();

        // then
        System.out.println(categoryOfMenu);
        System.out.println(categoryOfMenu.getMenuList());

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        categoryOfMenu.setCategoryName("test2");
        categoryOfMenu.getMenuList().get(1).setMenuName("test3");
        entityTransaction.commit();

    }

    @Test
    public void 임베디드_타입_프로젝션_테스트() {

    }

}
