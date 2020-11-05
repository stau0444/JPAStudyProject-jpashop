package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private  Address address;

    //이넘타입 사용시 어노테이션 타입은 스트링으로 사용하는게 좋다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY,CAMP

}
