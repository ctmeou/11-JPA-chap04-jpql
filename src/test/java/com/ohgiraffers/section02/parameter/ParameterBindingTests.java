package com.ohgiraffers.section02.parameter;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParameterBindingTests {

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
    public void 이름_기준_파라미터_바인딩_메뉴_목록_조회_테스트() {

        // given
        String menuNameParameter = "한우딸기국밥";

        // when
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = :menuName"; //:파라미터의 값 지정
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter("menuName", menuNameParameter) //어떤 파라미터를 건넬 건지 작성, 원하는 이름을 보내고
                .getResultList(); //그것을 실행하겠다.

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

    @Test
    public void 위치_기준_파라미터_바인딩_메뉴_목록_조회_테스트() {

        // given
        String menuNameParameter = "한우딸기국밥";

        // when
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = ?1"; //위치일 때는 ? 작성 후 숫자 기업(어디서부터 시작인지)
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter(1, menuNameParameter) //첫 번째 포지션에 파라미터를 보내고
                .getResultList(); //그것을 실행하겠다.

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

}
