package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;//주문 가격
    private int count;//주문 수량
    
    //생성하여 set하는 방식을 막기위해 기본생성자를 만들고 protected로 지정해준다
    //lombock 을 사용하면 아래의 코드를 @NoArgsConstructor를 통해 지정할 수 있다.
    //protected  OrderItem(){}
    //생성메서드
    public static OrderItem createOrderItem(Item item, int orderPrice , int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;

    }

    //비지니스 로직
    public void cancel(){
        getItem().addStock(count);
    }
    
    //조회 로직
    //주문 상품 전체 가격 조회
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }

}
