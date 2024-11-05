package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CommentMapper {

    public static Comment mapToComment(NewCommentRequest request, User user, Item item) {

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(Instant.now());

        return comment;
    }

    public static CommentDto mapCommentDto(Comment comment, String authorName) {

        CommentDto dto = new CommentDto();
        dto.setText(comment.getText());
        dto.setAuthorName(authorName);
        dto.setId(comment.getId());
        dto.setCreated(LocalDateTime.ofInstant(comment.getCreated(), ZoneId.systemDefault()));

        return dto;
    }
}
