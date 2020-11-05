package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//컴포넌트 스캔에 대상이되어 자동으로 빈에 저장됨
@Repository
public class MemberRepository {
    //EntityManager 주입
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return  member.getId();
    }
    public Member find(Long id){
        return  em.find(Member.class,id);
    }
}
