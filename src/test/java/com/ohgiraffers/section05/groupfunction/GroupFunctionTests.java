package com.ohgiraffers.section05.groupfunction;

import oracle.jdbc.internal.XSCacheOutput;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GroupFunctionTests {

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
    public void 특정_카테고리의_등록된_메뉴_수_조회() {

        // given
        int categoryCodeParameter = 4;

        // when             //count이기 때문에 오류가 발생하는 일은 없음 = 0이어도 count는 0이기 때문
        String jpql = "SELECT COUNT(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";
        long countOfMenu = entityManager.createQuery(jpql, Long.class) //반환 값이 정수면 Long 타입으로 작성
                .setParameter("categoryCode", categoryCodeParameter)
                .getSingleResult();

        // then
        assertTrue(countOfMenu >= 0);
        System.out.println(countOfMenu);

    }

    @Test
    public void count를_제외한_다른_그룹함수의_조회결과가_없는_경우_테스트() {

        // given
        int categoryCodeParameter = 0;

        // when
        String jpql = "SELECT SUM(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";

        // then
        //여기에서 차이는 long 기본 자료형일 때와 래퍼 클래스 Long 객체일 때의 차이를 알기 위해서이고, long은 null을 담을 수 없지만, Long은 null을 담을 수 있다.
        assertThrows(NullPointerException.class, () -> { //exception이 발생하기 때문에 작성하고,
            long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryCode", categoryCodeParameter) //파라미터는 카테고리코드로 보낸다.
                .getSingleResult(); //null이 값으로 반환될 것이라 exception 발생
        });

        assertDoesNotThrow(() -> {
            Long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                    .setParameter("categoryCode", categoryCodeParameter)
                    .getSingleResult();
            System.out.println(sumOfPrice);
        });

    }

    @Test
    public void groupby절과_having절을_사용한_조회_테스트() {

        // given
        long minPrice = 50000L; //비교타입 또한 타입을 맞게 작성해줘야 한다.

        // when
        String jpql = "SELECT m.categoryCode, SUM(m.menuPrice)" +
                     " FROM menu_section05 m" +
                     " GROUP BY m.categoryCode" +
                     " HAVING SUM(m.menuPrice) >= :minPrice"; //파라미터 데이터 타입 일치하게 작성해야 한다(Long타입)

        List<Object[]> sumPriceOfCategoryList = entityManager.createQuery(jpql, Object[].class)
                .setParameter("minPrice", minPrice)
                .getResultList();

        // then
        assertNotNull(sumPriceOfCategoryList);
        sumPriceOfCategoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println);
        });

    }

}
