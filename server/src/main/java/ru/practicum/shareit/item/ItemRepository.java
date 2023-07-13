package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select it " +
            "from Item as it " +
            "order by it.id asc")
    List<Item> findAllItem(Pageable pageable);

    @Query("select it " +
            "from Item as it " +
            "where it.owner.id = ?1 " +
            "order by it.id asc")
    List<Item> findAllItemWhereOwner(Long userId, Pageable pageable);

    @Query("select it " +
            "from Item as it " +
            "where it.requestId.id = ?1 ")
    List<Item> findAllItemWhereRequester(Long itemRequestId);
}
