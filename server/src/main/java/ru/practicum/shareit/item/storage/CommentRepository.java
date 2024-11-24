package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByItemId(Long itemId);

    @Query("select c FROM Comment c " +
            "join c.item as i " +
            " WHERE i.id IN :ids")
    List<Comment> findCommentsByItemIds(List<Long> ids);
}
