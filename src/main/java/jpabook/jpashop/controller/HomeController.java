package jpabook.jpashop.controller;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//롬복을 사용한다면 아래의 어노테이션으로 로거를 사용할 수 있다.
@Controller
@Slf4j
public class HomeController {
    //로거 사용준비 slf4j 로거를 사용해야 한다
    //Logger log = LoggerFactory.getLogger(getClass());
    @RequestMapping("/")
    public String home(){
        log.info("home controller");
        return  "home";
    }
}
