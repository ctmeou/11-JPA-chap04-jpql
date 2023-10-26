package com.ohgiraffers.section04.paging;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PagingTests {

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
    public void 페이징_API를_이용한_조회_테스트() {

        // given
        //필요한 input값이 필요하고, offset은 조회를 건너뛰는 수를 정하고, limit는 조회 시 가져올 컨텐츠 수
        //아래에 지정한 것은 11 ~ 15까지 조회를 하겠다.
        int offset = 10;
        int limit = 5;

        // when
        String jpql = "SELECT m FROM menu_section04 m ORDER BY m.menuCode DESC";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                //페이징 설정 후 값을 꺼냄
                .setFirstResult(offset) //첫 번째 결과가 무엇인지 좋겠는지 = 조회 시작 위치(설정하는 이유는 0부터 시작하기 때문에)
                .setMaxResults(limit) //얼만큼 많이 가져올 것인지(최대 조회 수)
                .getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

}
