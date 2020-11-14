package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.hibernate.Hibernate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);

	}

	//레이지 로딩을 무시하게 한다
	//Db에 들어가지 않았음으로 DB에 들어있는 데이터는 Null로 처리된다.
	
//	@Bean
//	Hibernate5Module hibernate5Module(){
//		return  new Hibernate5Module();
//	}
	//강제로 레이지 로딩을 시키는방법
	@Bean
	Hibernate5Module hibernate5Module(){
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING , true);
		return hibernate5Module;
	}


}
