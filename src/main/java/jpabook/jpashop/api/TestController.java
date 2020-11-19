package jpabook.jpashop.api;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@PathVariable- {}안에 지정한 URL의 파라미터가 컨트롤러
//파라미터에 담긴다  데이터 타입을 지정하여 받을 수 있다.
@RestController
@RequiredArgsConstructor
public class TestController {
    @GetMapping("/test/{numbs}")
    public Integer pathVariable(@PathVariable Integer numbs){
        System.out.println(numbs);
        return numbs;
    }

    // 바디에 정보가 담겨오는 post 방식 요청에서 파라미터를 읽어들일 때 사용하는 어노테이션
    //
    @PostMapping("/test")
    @ResponseBody
    public String requestBody(@RequestBody String name ){
        System.out.println(name);
        String response = "responseBody";
        return response;
    }

}

@Controller
class TestController2{

    @GetMapping("/testForm")
    public String testForm(){
        return "hello";
    }
}
