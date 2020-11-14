package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
xToOne 관계(ManyToOne, OneToOne)
order
order -> Member
Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private  final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findByCriteria(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        //====오더가 2개넘어온다 쿼리:1번
        List<Order> orders = orderRepository.findByCriteria(new OrderSearch());
        //Stream() 설명
        //변환해주는 메서드
        //위의 상황은 orders를 .map을통해 simpleOrderDto로 바꾸고
        //바꾼것을  collert로 리스트로 바꿔주고 리턴한다.
        //=====레이지로딩을 통해 SimpleOrderDto가 new 될때
        //Delivery 가 2번, Member가 2번 불러와진다 쿼리 4번
        //총쿼리수는 5번이 된다  => n+1 문제
        //n은 넘어온 엔티티 수 , 1은 맨처음 쿼리
        //연관된 테이블 수마다 n 번씩 추가된다.
        //n(배송) + n(회원) + 1 n=2(오더수)
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return  collect;
    }
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4(){
        return orderRepository.findOrderDtos();
    }
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        //DTO 가 엔티티를 파라미터로 받는거는 문제가 되지않는다.
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
