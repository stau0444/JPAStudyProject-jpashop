package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/*
    -폼 객체의 용도
    엔티티는 엔티티 고유 정보만 가지고있어야 함으로
    엔티티를 바로 폼에 사용하는 것은  맞지않다
    해당 폼에관한 폼 객체를 따로 만들어 사용하면
    검증등에 용이하며 조금더 해당 폼에 핏하게 사용할 수있다.
 */

@Getter @Setter
public class MemberForm {
    //
    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private  String name;

    private String city;
    private String street;
    private String zipcode;
}
