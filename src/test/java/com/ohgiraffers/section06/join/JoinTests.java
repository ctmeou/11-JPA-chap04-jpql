package com.ohgiraffers.section06.join;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JoinTests {

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
    public void 내부조인을_이용한_조회_테스트() {

        // when
        String jpql = "SELECT m FROM menu_section06 m JOIN m.category c"; //메뉴를 조회 + join 구문(카테고리 필드 명시해서 해당 부분에서 조인해서 가져오는 것을 명시)
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList(); //조회할 것인데 여러 개니까 List<Menu>

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

    @Test
    public void 외부조인을_이용한_조회_테스트() {

        // when
        String jpql = "SELECT m.menuName, c.categoryName FROM menu_section06 m RIGHT JOIN m.category c" +
                     " ORDER BY m.category.categoryCode"; //정렬 순서를 카테고리 코드 기준으로 설정
        List<Object[]> menuList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });

    }

    @Test
    public void 컬렉션조인을_이용한_조회_테스트() {

        // when
        //현재 menuList가 컬렉션으로 되어있어 그것을 기준으로 작성
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section06 c LEFT JOIN c.menuList m";
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });

    }

    //cross join 모든 경우를 join해서 반환
    @Test
    public void 세타조인을_이용한_조회_테스트() { //세타조인 = cross join : 두 테이블의 모든 행의 가능한 조합을 반환한다.(카테이션 곱)

        // when
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section06 c, menu_section06 m";
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });

    }

    @Test
    public void 페치조인을_이용한_조회_테스트() {

        // when
        String jpql = "SELECT m FROM menu_section06 m JOIN FETCH m.category c"; //메뉴를 조회 + join 구문(카테고리 필드 명시해서 해당 부분에서 조인해서 가져오는 것을 명시)
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList(); //조회할 것인데 여러 개니까 List<Menu>

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

}
