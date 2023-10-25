package com.ohgiraffers.section01.simple;

import org.junit.jupiter.api.*;

import javax.persistence.*;
import javax.swing.event.MenuListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleJPQLTests {

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

    //TypedQuery를 선언했을 경우
    @Test
    public void TypedQuery를_이용한_단일메뉴_조회_테스트() {

        // when
        //구문 작성
        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode =7";
        //실제 조회할 수 있게 작성
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
        //실제 실행하기 위해 getSingleResult() 작성
        String resultMenuName = query.getSingleResult();

        // then
        assertEquals("민트미역국", resultMenuName);

    }

    //TypedQuery를 선언하지 않았을 경우 -> Query가 반환이 된다.
    //반환값 타입이 지정되지 않았기 때문에 상위 타입인 Objcet로 반환이 된다.
    @Test
    public void Query를_이용한_단일메뉴_조회_테스트() {

        // when
        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode = 7";
        Query query = entityManager.createQuery(jpql);
        Object resultMenuName = query.getSingleResult();

        // then
        assertEquals("민트미역국", resultMenuName);

    }

    //특정 컬럼이 아닌 엔티티만 조회할 경우
    @Test
    public void TypedQuery를_이용한_단일행_조회_테스트() {

        // when
        //select절에 m을 쓴 경우에는 전체 컬럼에서 조회
        String jpql = "SELECT m FROM menu_section01 as m WHERE m.menuCode = 7";
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);
        Menu foundMenu = query.getSingleResult();

        // then
        assertEquals(7, foundMenu.getMenuCode());
        System.out.println(foundMenu);

    }

    @Test
    public void TypedQuery를_이용한_다중행_조회_테스트() {

        // when
        String jpql = "SELECT m FROM menu_section01 as m";
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);
        List<Menu> foundMenuList = query.getResultList(); //다중행일 경우 실행 부분에 getResultList로 작성해야 한다.

        // then
        assertNotNull(foundMenuList);
        //List로 반환했기 때문에 forEach로 출력
        foundMenuList.forEach(System.out::println);

    }

    @Test
    public void Query를_이용한_다중행_조회_테스트() {

        // when
        String jpql = "SELECT m FROM menu_section01 as m";
        Query query = entityManager.createQuery(jpql);
        List<Menu> foundMenuList = query.getResultList(); //다중행일 경우 실행 부분에 getResultList로 작성해야 한다.

        // then
        assertNotNull(foundMenuList);
        //List로 반환했기 때문에 forEach로 출력
        foundMenuList.forEach(System.out::println);

    }

    @Test
    public void distinct를_활용한_중복제거_여러_행_조회_테스트() {

        // when
        //메뉴 테이블에 있는 카테고리코드를 중복 없이 알고 싶기 때문에 DISTINCT를 추가한다.
        String jpql = "SELECT DISTINCT m.categoryCode FROM menu_section01 m";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        //조회 결과가 하나가 아닌 여러 개이기 때문에 List로 받는다.
        List<Integer> categoryCodeList = query.getResultList();

        // then
        //출력
        assertNotNull(categoryCodeList);
        categoryCodeList.forEach(System.out::println);

    }

    @Test
    public void in_연산자를_활용한_조회_테스트() {

        // 카테고리 코드가 6이거나 10인 menu 조회
        // when
        String jpql = "SELECT m FROM menu_section01 m WHERE m.categoryCode IN (6, 10)";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

    @Test
    public void like_연산자를_활용한_조회_테스트() {

        // "마늘"이 메뉴 이름으로 들어간 menu 조회
        // when
        String jpql = "SELECT m FROM menu_section01 m WHERE m.menuName LIKE '%마늘%'";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }


}
