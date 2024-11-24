package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequestAnswer;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemsByOwnerId(long userId);

    @Query("select it " +
            "from Item as it " +
            "where ?1 <> '' " +
            "and (lower(it.name) like lower(concat('%', ?1, '%')) " +
            "or lower(it.description) like lower(concat('%', ?1, '%'))) " +
            "and it.available = true ")
    List<Item> findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseAAndAvailable(String nameSearch);

    List<ItemRequestAnswer> findAllByItemRequestIdIn(List<Long> requestIds);

    List<ItemRequestAnswer> findAllByItemRequestId(Long requestId);
}


