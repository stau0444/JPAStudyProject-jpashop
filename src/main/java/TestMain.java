import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestMain {
    public static void main(String[] args) {
        // 스트림 람다 Test -forEach()
        List<Integer> numbs = new ArrayList<>();
        numbs.add(1);
        numbs.add(2);
        numbs.add(3);
        numbs.stream().forEach(n -> System.out.println(n));
        //numbs를 하나씩 꺼내 n에 담고 출력하였다.
        //for(int i = 0 ; i<numbs.size(); i++){System.out.println(numbs.get(i)}와 같다

        //스트림 람다 Test - map()
        //형태 변환메서드
        List<Integer> numbs2 = new ArrayList<>();
        numbs2.add(1);
        numbs2.add(2);
        numbs2.add(3);
        List<NDto> collect = numbs2.stream().map(n -> new NDto(n))
                .collect(Collectors.toList());
        //numbs2를 하나씩 꺼내 n에담고 NDto로 변환하였다
        //파라미터로 Integer 타입인 n 을 넘겨서 NDto에 n이 하나씩 set 되었다
        //collect(Collectors.toList());는 NDto로 변환된 것을 다시 리스트에 넣어준다.
        System.out.println("-----------------------");
        System.out.println(numbs2);
        //Integer 배열로 출력된다 [1,2,3]
        System.out.println(collect);
        //Ndto 배열로 출력된다 [TestMain$NDto@14514713, TestMain$NDto@69663380, TestMain$NDto@5b37e0d2]
        System.out.println("-----------------------");
        for (NDto nDto : collect) {
            System.out.println("----------------------------------------");
            System.out.println(nDto.getNum());
            //
        }

        //Stream () 람다 distinct 와 filter
        //distinct는 중복을 제거하고 filter는 필터링을 한다.
        List<String> numbs5 = new ArrayList<>();
        numbs5.add("12");
        numbs5.add("22");
        numbs5.add("33");
        numbs5.add("34");
        //리스트에서 중복을 제거한 후 다시 리스트로 만든다.
        List<String> collect2 = numbs5.stream()
                 .distinct()
                 .collect(Collectors.toList());
        System.out.println("distinct-----------------------------------");
        System.out.println(collect2);

        //filter - 데이터를 걸러준다 데이터 타입에 따라 여러가지 메소드가 있다
        //리스트안에 아래는 1로 시작하는 것만 가져온다.
        List<String> collect3 = numbs5.stream()
                .filter(n->n.startsWith("1"))
                .collect(Collectors.toList());
        System.out.println("filter-----------------------------------");
        System.out.println(collect3);

        List<Integer> numbs3 = new ArrayList<>();
        numbs3.add(1);
        numbs3.add(2);
        numbs3.add(3);
        numbs3.add(3);
        Set<Integer> collect1 = numbs3.stream().collect(Collectors.toSet());
        System.out.println("set2--------------------------");
        System.out.println(collect1);

        //자료구조 테스트
        //HashSet - 중복을 허용하지 않고 순서가 따로없다 , set.hashCode()는  .
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        set.add("C");
        set.add("D");
        set.add("D");
        System.out.println("set.hashCode()--------------------"+set.hashCode());
        System.out.println("set1---------------------");
        System.out.println(set);

        //Hash Map - 오브젝트에 키와 벨류로  데이터를 담는다 {}
        Map<Integer,Object> map = new HashMap<>();
        map.put(1,"hello");
        map.put(2,"hello2");
        map.put(3,"hello3");
        map.put(4,"hello4");
        map.put(5,"hello5");
        System.out.println("map---------------------------");
        System.out.println(map);

    }

    @Getter
    static class NDto{
        private int num;

        public NDto(int num) {
            this.num = num;
        }
    }
}
