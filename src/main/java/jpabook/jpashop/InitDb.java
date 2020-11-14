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
        initService.dbInit2();
       System.out.println("----------------샘플데이터 추가됨-----------------");
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private  final EntityManager em;

        public void dbInit1(){
            Member member = createMember("userA", "서울", "1", "111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK",10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000 , 100);
            em.persist(book2);

            Book book3 = createBook("JPA3 BOOK", 30000 , 100);
            em.persist(book3);

            Book book4 = createBook("JPA4 BOOK", 30000 , 100);
            em.persist(book4);

            Book book5 = createBook("JPA5 BOOK", 30000 , 100);
            em.persist(book5);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
            OrderItem orderItem3 = OrderItem.createOrderItem(book3, 30000, 3);
            OrderItem orderItem4 = OrderItem.createOrderItem(book4, 30000, 4);
            OrderItem orderItem5 = OrderItem.createOrderItem(book5, 30000, 5);

            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3, orderItem4,orderItem5);

            em.persist(order);

        }

        public void dbInit2() {
            Member member = createMember("userB", "진주", "2", "222");
            em.persist(member);

            Book book1 = createBook("spring1 BOOK", 20000 , 100);
            em.persist(book1);

            Book book2 = createBook("spring2 BOOK", 40000 , 200);
            em.persist(book2);

            Book book3 = createBook("spring3 BOOK", 50000 , 300);
            em.persist(book3);

            Book book4 = createBook("spring4 BOOK", 60000 , 400);
            em.persist(book4);

            Book book5 = createBook("spring5 BOOK", 50000 , 100);
            em.persist(book5);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 2);
            OrderItem orderItem3 = OrderItem.createOrderItem(book3, 50000, 3);
            OrderItem orderItem4 = OrderItem.createOrderItem(book4, 60000, 4);
            OrderItem orderItem5 = OrderItem.createOrderItem(book5, 50000, 4);

            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3, orderItem4,orderItem5);

            em.persist(order);
        }
    }

    private static Delivery createDelivery(Member member) {
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        return delivery;
    }

    private static Book createBook(String name, int price, int stockQuantity) {
        Book book1 = new Book();
        book1.setName(name);
        book1.setPrice(price);
        book1.setStockQuantity(stockQuantity);
        return book1;
    }

    private static Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
        return member;
    }
}

