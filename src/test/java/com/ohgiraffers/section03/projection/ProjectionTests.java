package com.ohgiraffers.section03.projection;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    //필요한 것만 추출을 할 수 있으나 엔티티는 아니다.
    @Test
    public void 임베디드_타입_프로젝션_테스트() {

        // when
        String jpql = "SELECT m.menuInfo FROM embedded_menu m";
        List<MenuInfo> menuInfoList = entityManager.createQuery(jpql, MenuInfo.class).getResultList();

        // then
        assertNotNull(menuInfoList);
        menuInfoList.forEach(System.out::println);

    }

    //스칼라 : 단일값(하나의 숫자, 하나의 문자 등)
    @Test
    public void TypeQuery를_이용한_스칼라_타입_프로젝션_테스트() {

        // when
        String jpql = "SELECT c.categoryName FROM category_section03 c";
        List<String> categoryNameList = entityManager.createQuery(jpql, String.class).getResultList();

        // then
        assertNotNull(categoryNameList);
        categoryNameList.forEach(System.out::println);

    }

    @Test
    public void Query를_이용한_스칼라_타입_프로젝션_테스트(){

        // when
        String jpql = "SELECT c.categoryCode, c.categoryName FROM category_section03 c";
        //categoryCode와 categoryName를 반환받을 수 있는 타입이 없기 때문에 우선 배열 형태로 받는다.
        List<Object[]> categoryList = entityManager.createQuery(jpql).getResultList();
                                                        //타입을 지정할 수 없어서 jpql만 보낸다.

        // then
        //반환 값 출력
        assertNotNull(categoryList);
        categoryList.forEach(row -> { //람다식 작성, 하나하나 반복하는 것이 한 행, 한 행
            Arrays.stream(row).forEach(System.out::println);
        });

    }

    //Object[]타입이 아닌
    @Test
    public void new_명령어를_활용한_프로젝션_테스트(){

        // when                                                           매개변수 생성자 호출
        String jpql = "SELECT new com.ohgiraffers.section03.projection.CategoryInfo(c.categoryCode, c.categoryName) FROM category_section03 c";
        List<CategoryInfo> categoryList = entityManager.createQuery(jpql, CategoryInfo.class).getResultList();

        // then
        assertNotNull(categoryList);
        categoryList.forEach(System.out::println);

    }

}
