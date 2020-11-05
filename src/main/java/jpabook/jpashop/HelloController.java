package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        //data라는 키로 hello를 넘긴다
        model.addAttribute("data","hello!!!");
        return "hello";
    }

}
