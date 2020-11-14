package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address Address;

    //컬랙션은  변경하지 않고 있는 그대로 사용하는게 좋다.
    //엔티티에서 프레젠테이션 계층으로 의존관계가 가게되고
    //양방향으로 의존관계를 갖게 되어 유지보수가 어려워진다
    //@JsonIgnore// Json으로 데이터를 받을때 해당정보는 안가져오게 설정하는 어노테이션
    //양방향 연관관계에서는 한방향에 JasonIgnore를 해줘야한다 안해주면 무한루프
    //안해주면 젝슨라이브러리가 무한정 제이슨을 뽑아낸다.
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
