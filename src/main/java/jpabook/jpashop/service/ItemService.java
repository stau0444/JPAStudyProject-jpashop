package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//읽기 전용 트렌젝션 설정
@RequiredArgsConstructor
public class ItemService {
    private  final ItemRepository itemRepository;

    //커멘드성 메서드는 일반 트랜젝션을 사용한다.
    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public List<Item> findItem(){
        return  itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return  itemRepository.fineOne(itemId);
    }

}
