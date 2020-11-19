package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//화면에
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private  final EntityManager em;
    public List<OrderQueryDto> findOrderQueryDtos(){
    // ToOne 관계를 먼저가져오고
        List<OrderQueryDto> result = findOrders();
        //ToMany 관계를 따로 가져온다.
        result.forEach(o->{
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    public List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id , i.name, oi.orderPrice, oi.count) "+
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId",OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id , m.name , o.orderDate , o.status , d.address) from Order o " +
                        " join o.member m " +
                        " join o.delivery d",OrderQueryDto.class
        ).getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        //오더 리스트를 가져오고
        List<OrderQueryDto> result = findOrders();
        //오더 리스트에서 아이디를 뽑아낸다.
        List<Long> orderIds= result.stream().map(o -> o.getOrderId())
                .collect(Collectors.toList());
        //in절로 orderIds에 있는 아이디를 모두 조회해서 한번에가져오고
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id , i.name, oi.orderPrice, oi.count) "+
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds",OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        //in 쿼리로 한번에 가져온 데이터를 메모리에서 가공하여 사용한다.
        //가져온 데이터를 맵으로 변환해주고
        //Collector.groupingBy(OrderItemQueryDto::getOrderId (key가 된다))
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
        //가져온 OrderDto에 꽂아준다.
        //쿼리가 두번으로 최적화 된다(오더를 가져오는 쿼리 한번, 오더아이템을 가져오는 쿼리한번)
        result.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return  result;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
         return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name , o.orderDate, o.status , d.address , i.name , oi.orderPrice , oi.count) " +
                        " from Order o" +
                        " join o.member m " +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i ", OrderFlatDto.class).getResultList();
    }
}
