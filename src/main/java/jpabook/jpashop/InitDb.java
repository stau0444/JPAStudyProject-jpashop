package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/*
총 주문 2개
-userA
JPA1 BOOK
JPA2 BOOK
-userB
SPRING1 BOOK
SPRING1 BOOK
 */
//서버가 뜰떄 스프링 컴포넌트 스캔의 대상이 되고
@Component
@RequiredArgsConstructor
public class InitDb {
    
    private  final  InitService initService;

    //스프링 빈이 생성된후 아래 코드를 호출해준다.
   @PostConstruct
    public void init(){
        initService.dbInit1();
       System.out.println("----------------샘플데이터 추가됨-----------------");
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private  final EntityManager em;

        public void dbInit1(){
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("서울","1","111"));
            em.persist(member);

            Member member2 = new Member();
            member2.setName("userB");
            member2.setAddress(new Address("서울","1","111"));
            em.persist(member2);

            Book book1 = new Book();
            book1.setName("JPA1 BOOK");
            book1.setPrice(10000);
            book1.setStockQuantity(100);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA2 BOOK");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            em.persist(book2);

            Book book3 = new Book();
            book3.setName("JPA3 BOOK");
            book3.setPrice(30000);
            book3.setStockQuantity(100);
            em.persist(book3);

            Book book4 = new Book();
            book4.setName("JPA4 BOOK");
            book4.setPrice(30000);
            book4.setStockQuantity(100);
            em.persist(book4);

            Book book5 = new Book();
            book5.setName("JPA5 BOOK");
            book5.setPrice(30000);
            book5.setStockQuantity(100);
            em.persist(book5);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
            OrderItem orderItem3 = OrderItem.createOrderItem(book3, 30000, 3);
            OrderItem orderItem4 = OrderItem.createOrderItem(book4, 40000, 4);
            OrderItem orderItem9 = OrderItem.createOrderItem(book5, 40000, 4);

            OrderItem orderItem5 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem6 = OrderItem.createOrderItem(book2, 20000, 2);
            OrderItem orderItem7 = OrderItem.createOrderItem(book3, 30000, 3);
            OrderItem orderItem8 = OrderItem.createOrderItem(book4, 40000, 4);


            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Delivery delivery2 = new Delivery();
            delivery.setAddress(member2.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3, orderItem4,orderItem9);
            Order order2 = Order.createOrder(member2, delivery2, orderItem5, orderItem6, orderItem7, orderItem8);
            em.persist(order);
            em.persist(order2);
        }
    }
}

