package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor = 밑에 생성자 인젝션을 롬복이 만들어준다
@RequiredArgsConstructor // 파이널이 있는 필드를 가지고 생성자를 만들어준다
public class MemberService {
    //필드 인젝션
    //@Autowired
    private final MemberRepository memberRepository;

    //setter 인젝션
    //장점 테스트 코드 작성할때 mock 같은걸 주입할 수 있다.
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }
    //생성자 인젝션
    //최신버전에서는 생성자에 하나만 인젝션할 것이 하나만 있을 경우
    //autowired 없이도 인젝션을 자동으로 해준다
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    @Transactional
    //회원 가입
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        //persist 할때 영속성 컨텍스트에 값이 저장되기 때문에 리턴이 가능하다.
        return member.getId();
    }

    //회원 중복확인 메서드
    private void validateDuplicateMember(Member member){
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 한명조회
    public Member findOne(Long memberId){
        return  memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        //JPA를 통해 아이디를 찾아오기 때문에 영속상태가되어
        Member member = memberRepository.findOne(id);
        //변경감지가 일어난다
        member.setName(name);
    }
}
