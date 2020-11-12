package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private  final OrderRepository orderRepository;
    private  final MemberRepository memberRepository;
    private  final ItemRepository itemRepository;

    //주문
    @Transactional
    public  Long order(Long memberId, Long itemId , int count){

        //엔티티 조회
        //주문 회원 정보를가져오고
        Member member = memberRepository.findOne(memberId);

        //주문 상품 정보를 가져온다
        Item item = itemRepository.fineOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(),count);
        //생성 메서드를 통해서맨 객체를 생성할 수 있다.
        //OrderItem orderItem1 = new OrderItem();
        //주문 생성
        Order order = Order.createOrder(member, delivery , orderItem);
        
        //주문 저장
        //cascade 옵션때문에 오더만 저장을해도 자동으로 orderItem 과 delivery가 자동으로 insert된다.
        //cascade 옵션은 오더만 참조하는 필드에만 사용하는 것이 좋다
        //라이프사이클이 동일하게 관리될 때 사용하면 좋다
        orderRepository.save(order);

        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId){ 
        //넘어온 주문아이디로 주문을 조회하고
        Order order = orderRepository.findOne(orderId);
        //order에 취소메소드를 사용하여 order를 취소해 준다.
        /*
          - JPA의 장점
          마이바티스, JDBC 템플릿 같은 라이브러리에서는
          데이터를 변경하기 위해 별도의 update 쿼리를 만들어야 하지만
          JPA는 dirtychecking을 이용하여 해당 메서드에서 변경이 일어난 사항을
          찾아 자동으로 업데이트 해준다.
         */
        order.cancel();
    }


    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findByCriteria(orderSearch);
    }
}
