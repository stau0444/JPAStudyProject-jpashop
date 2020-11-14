package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private  final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class,id);
    }
    //JPA동적쿼리 사용
    //1.스트링을 짜집기하여 만든다
    public List<Order> findAll(OrderSearch orderSearch){
        String jpql = "select o from Order o join o.member m" +
                " where o.status = :status " +
                " and m.name like :name";
        return em.createQuery(jpql, Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .getResultList();

    }
    //2.JPA criteria 사용(표준스펙)
    //무슨쿼리가 만들어지나 딱떠오르지가 않는다. 유지보수가 힘들다.
    public  List<Order> findByCriteria(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);
        List<Predicate> criteria = new ArrayList<>();
        
        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null){
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);

        return query.getResultList();
    }
    // 페치조인 사용하여 리스트를 가져옴
    // 연관 관계가 맺어진 것들을 패치 조인으로 명시한다.
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m " +
                        " join fetch o.delivery d ",Order.class
        ).getResultList();
    }

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.repository" +
                        ".OrderSimpleQueryDto(o.id , m.name, o.orderDate , o.status, d.address)" +
                        " from Order o " +
                        " join o.member m" +
                        " join o.delivery d",OrderSimpleQueryDto.class)
                .getResultList();
    }


    //3.QueryDSL 사용
}
