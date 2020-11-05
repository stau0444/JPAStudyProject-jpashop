package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
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


}
