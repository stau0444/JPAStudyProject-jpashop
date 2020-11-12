package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //스프링이 엔티티 매니저를 만들어 주입해준다
    //@Autowired가 가능하다 스프링부트기 떄문에
    private final EntityManager em;
    
    //회원 정보 저장
    public void save(Member member){
        em.persist(member);
    }
    //회원 한명 정보 불러오기
    public Member findOne(Long id){
        return em.find(Member.class,id);
    }
    //회원 여러명 정보 불러오기
    //em.createQuery(쿼리 , 반환타입) JPQL 생성 해주는 메서드'
    //getResultList(); 리스트를 불러옴
    //ctrl+alt+N -> 변수 합쳐주는 단축키
    //ctrl+alt+V -> 변수 자동생성 단축키
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    
    //이름을 이용하여 회원을 불러옴
    //setParameter(파라미터 키, 파라미터 값)
    //쿼리에 파라미터 바인딩은  :name 이런식으로 함
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}
