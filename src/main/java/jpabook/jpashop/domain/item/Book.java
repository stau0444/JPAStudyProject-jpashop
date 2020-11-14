package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//자식테이블에서 DTYPE에 입력될 값을 지정
@DiscriminatorValue("B")
@Getter @Setter
public class Book  extends  Item {
    private String author;
    private String isbn;

}
