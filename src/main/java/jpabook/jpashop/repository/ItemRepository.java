package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){
            //아이템의 아이디값이 없으면 새로운 상품을 등록하는 것
            em.persist(item);
        }else{
            //아이템의 아이디값이 있으면 있는 상품을 업데이트하는 것
            em.merge(item);
        }
    }

    public Item fineOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return  em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }

}
