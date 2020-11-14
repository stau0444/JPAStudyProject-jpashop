package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

    @Data
    public class OrderSimpleQueryDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        //DTO 가 엔티티를 파라미터로 받는거는 문제가 되지않는다.
        public OrderSimpleQueryDto(Long orderId, String name ,
                                   LocalDateTime orderDate,
                                   OrderStatus orderStatus,
                                   Address address) {
            this.orderId =orderId;
            this.name =name;
            this.orderDate =orderDate;
            this.orderStatus =orderStatus;
            this.address =address;
        }
    }

