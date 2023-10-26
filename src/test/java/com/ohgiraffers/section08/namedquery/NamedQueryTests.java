package com.ohgiraffers.section08.namedquery;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NamedQueryTests {

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
    public void 동적쿼리를_이용한_조회_테스트() {

        // given
        String searchName = "한우"; //검색하고자 하는 이름이 넘어갈 수도 있고 안넘어갈 수도 있고
        int searchCategoryCode = 4; //검색하고자 하는 코드가 넘어갈 수도 있고 안넘어갈 수도 있고

        // when
        StringBuilder jpql = new StringBuilder("SELECT m FROM menu_section08 m ");
        if (searchName != null && !searchName.isEmpty() && searchCategoryCode > 0) {
                                //빈문자가 아닌 의미가 있다(한우)
            jpql.append("WHERE ");
            jpql.append("m. menuName LIKE '%' || :menuName || '%' ");
            jpql.append("AND ");
            jpql.append("m.categoryCode = :categoryCode "); //categoryCode에 categoryCode 검색 조건이 되게
        } else {
            if (searchName != null && !searchName.isEmpty()) { //searchName만 넘어온 경우
                jpql.append("WHERE ");
                jpql.append("m. menuName LIKE '%' || :menuName || '%' ");
            } else if (searchCategoryCode > 0) { //searchCategoryCode만 넘어온 경우
                jpql.append("WHERE ");
                jpql.append("m.categoryCode = :categoryCode ");
            }
        }
        //파라미터 전달해서 수행하고 파라미터가 필요한 경우, 필요없는 경우를 세팅
        TypedQuery<Menu> query = entityManager.createQuery(jpql.toString(), Menu.class);
        if (searchName != null && !searchName.isEmpty() && searchCategoryCode > 0) {
            query.setParameter("menuName", searchName);
            query.setParameter("categoryCode", searchCategoryCode);
        } else {
            if (searchName != null && !searchName.isEmpty()) {
                query.setParameter("menuName", searchName);
            } else if (searchCategoryCode > 0) {
                query.setParameter("categoryCode", searchCategoryCode);
            }
        }
        List<Menu> menuList = query.getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

    //정적 쿼리를 저장해두고 이용한 테스트(문자를 이용하는 것이 아니라 동적 쿼리처럼 추가해서 이용하는 것은 안됨)
    @Test
    public void 네임드쿼리를_이용한_조회_테스트() {

        // when
        List<Menu> menuList = entityManager.createNamedQuery("menu_section08.selectMenuList", Menu.class).getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

}
