package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    //엔티티 직접 노출 버전 ( 비추 )
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findByCriteria(new OrderSearch());
        //프록시 초기화 (프록시는 실제 값을 사용할때 초기화된다)
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
//            for (OrderItem orderItem : orderItems) {
//                //orderItem에 대한 프록시 초기화
//                orderItem.getItem().getName();
//            }
            //stream의 forEach 활용한 roof
            //위의 반복문과 같은 결과
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }
    //DTO를 만들어서 사용하는 버전(추천)
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findByCriteria(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }
    
    //패치조인 버전 (비추)
    //단점 : 페이징불가.
    //컬랙션 패치조인은 하나만 가능하다.
    //로우 수가 너무 많아져 정합성이 안맞는다.

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        //이때 데이터를 다 가져와서 영속성 컨텍스트에 캐싱하고
        System.out.println("프록시 초기화 전-----------------------------");
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        //이때 프록시에 초기화가 된다.
        System.out.println("프록시 초기화 후-----------------------------");
        return collect;
    }

    //패치 조인 페이징 가능버전
    //ToOne 관계에 있는 것들은 일단 모두 패치조인으로 받아온다.(데이터가 뻥튀기 되지않기 떄문에 페이징에 영향을 주지 않는다.)
    //컬렉션은 지연로딩으로 가져온다. 지연로딩을 통해 연관관계에 있는 것들이 프록싱으로 가져와지기 때문에
    //Order를 가져오는 첫번째 쿼리에서  Order만 가져오더라도  나머지 ToOne관계(Member,Delivery)
    //도 프록싱이 초기화될때 가져오게되며 배치사이즈를 통해서 인쿼리로 한번에 가져와 지게됨으로
    //성능상 크게 문제가 생기지 않는다.
    //배치사이즈를 글로벌하게 관리하고 싶으면 application.yml 에 hibernate: 밑으로 설정해주면되고
    //디테일하게 하고싶으면 해당엔티티에 배치사이즈를 적용시키고 싶은 필드 위에 @BatchSize(size = 100)어노테이션을 붙혀주면 된다.
    //배치사이즈 맥시멈은 DB에 따라 다르고 100~1000개가 적당
    //어떤 데이터는 1000개이상에서 오류가날 수도 있다.
    //크기가 너무크면 순간 부하가 갑자기 늘어남으로 was가 버티는 선으로 정하는게 좋다.
    //

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit" , defaultValue = "100") int limit){
        
        //여기서는 패치조인한 것을이 들어오고(ToOne관계에있는것들)
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit);
        //여기서는 프록시 초기화를 통해 데이터가 들어옴
        //OrderDto에 OrderItems를 가져올때
        //배치사이즈를 지정해서  in쿼리로  컬랙션을 다가져온다
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;

    //DTO로 직접 조회해서 가져오는 버전
    //ToOne 관계를 먼저 다가져오고
    //ToMany 관계는 레이지로딩을 통해 루프를 돌려 초기화 시켜줬다
    }@GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
         return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
    }


//페치조인 페이징 가능한 버전

    //Order엔티티를 담을 Dto
    //orderItems도 일대다의 관계를 가지고있음으로
    //OrderItem도 Dto에 담아줘야한다.
    //map을 통해 엔티티의 데이터가 get될때마다
    //프록시가 초기화되면서 실제 값이 들어간다.
    //궁금한것 : 프록시가 초기화될때 DB에서 값을 가져올텐데 왜 쿼리는 따로안나갈까
    //이미 데이터를 다가져오고 영속성 컨텍스트에서 관리하다가 값을 넘겨주는건가.
    @Getter
    static  class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            //여기서 배치사이즈를 사용하면 한번에 지정한 숫자만큼 가져온다.
            orderItems = order.getOrderItems()
                    .stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
        @Getter
        static class OrderItemDto {

            private String itemName;
            private int orderPrice;
            private int count;

            public OrderItemDto(OrderItem orderItem) {
                itemName = orderItem.getItem().getName();
                orderPrice = orderItem.getOrderPrice();
                count = orderItem.getCount();
            }
        }
    }
}
