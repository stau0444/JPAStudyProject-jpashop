package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private  final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
            model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        //@Valid MemberForm객체에 유효성 검사 할 것이 있으면 검증을한다
        //현재 MemberForm에는 @NotEmpty가 걸려 있다.
        //검증에 실패하면 해당오류가 result로 넘어온다.
        //타임리프가 제공하는 thymeleaf spring 라이브러리와 연계되어
        //에러가 있을시 지정한 페이지로 넘어가고
        //해당 검증사항에 적어놨던 메시지를
        //해당 태그 th:class 를 이용하여 받아오고 에러시 표시한다.
        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }
    //이것도 DTO를 사용하여 화면에 필요한 데이터만 출력하는게 좋다.
    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }

}
