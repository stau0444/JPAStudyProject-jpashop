package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//상속관계를 정의하는 어노테이션 single_table은 한테이블에 다떄려박는다
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//DTYPE 지정하는 어노테이션 
@DiscriminatorColumn
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>() ;

    //비지니스 로직
    //해당 데이터를 가지고 있는 엔티티에서 비지니스 로직을 가지고 있는 것이 응집력이 좋다.
        //변경이 필요한 경우 핵심 비지니스 메서드를 가지고 변경을 해야한다 (객체지향적인방법)

    //재고 증가 메소드
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }
    //재고 감소 메소드
    public void removeStock(int quantity){
        int restStock = this.stockQuantity -= quantity;
        //재고량이 0 보다 작으면 예외를터트린다
        if(restStock<0){
            throw new NotEnoughStockException("need more stock");
        }

    }
    

}
