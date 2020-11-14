package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

//도메인 모델 패턴
//서비스계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다.
//상태(필드)와 행위(메서드)를 몰아넣어 좀더 객체지향으로 코딩이 가능하다.
//엔티티에 핵심 비지니스 로직을 몰아 넣어 응집력을 높혀 유지보수가 용이하도록하는 패턴
//반대의 개념으로 트렌젝션 스크립트 패턴이 있다.
@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    //
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name= "member_id")
    private Member member;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;//주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태(ORDER,CANCLE)
    
    //연관관계 메서드
    //양방향 일대 한번에 양쪽에 데이터를 넣을 수 있도록 함
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    
    //생성 메서드
    public  static  Order createOrder(Member member,Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
           order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비지니스 로직
    //주문 취소

    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.CAMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    //조회 로직
    
    //전체 주문 가격 조회
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        System.out.println("total = " + totalPrice);
        return  totalPrice;
    }

}
