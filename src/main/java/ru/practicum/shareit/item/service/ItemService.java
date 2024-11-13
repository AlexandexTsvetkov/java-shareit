package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.ItemLastAndNextBooking;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;


import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    @Autowired
    private final ItemRepository itemStorage;

    @Autowired
    private final UserRepository userStorage;

    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;

    public Collection<ItemDto> findAll(long userId) {

        Optional<User> user = userStorage.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        List<Item> items = itemStorage.findItemsByOwnerId(userId);

        List<Long> itemIds = items.stream().map(Item::getId).toList();

        final Map<Item, List<CommentDto>> commentsByItems = commentRepository.findCommentsByItemIds(itemIds)
                .stream()
                .collect(Collectors.groupingBy(
                Comment::getItem,
                Collectors.mapping(comment -> CommentMapper.mapCommentDto(comment, comment.getAuthor().getName()), Collectors.toList())
        ));

        List<ItemLastAndNextBooking> itemLastAndNextBookings = bookingRepository.findLastAndNextBookings(userId, Instant.now());

        if (!itemLastAndNextBookings.isEmpty()) {

            final Map<Long, ItemLastAndNextBooking> itemLastAndNextBookingMap = itemLastAndNextBookings
                    .stream().collect(Collectors.toMap(ItemLastAndNextBooking::getItemId, itemLastAndNextBooking -> itemLastAndNextBooking));

            return items.stream().map(item -> ItemMapper.mapItemDto(item, commentsByItems.get(item),
                    itemLastAndNextBookingMap.get(item.getId()).getLastBooking(),
                    itemLastAndNextBookingMap.get(item.getId()).getNextBooking())).toList();

        } else {

            return items.stream().map(item -> ItemMapper.mapItemDto(item, commentsByItems.get(item))).toList();
        }
    }

    public ItemDto findById(long id) {
        Optional<Item> item = itemStorage.findById(id);

        if (item.isPresent()) {

            List<CommentDto> comments = commentRepository.findCommentsByItemId(id).stream()
                    .map(comment -> CommentMapper.mapCommentDto(comment, comment.getAuthor().getName()))
                    .toList();

            return ItemMapper.mapItemDto(item.get(), comments);
        }

        throw new NotFoundException(MessageFormat.format("Вещь с id {0, number} не найдена", id));
    }

    @Transactional
    public ItemDto create(NewItemRequest newItemRequest, long userId) {

        Optional<User> user = userStorage.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        Item newItem = ItemMapper.mapToItem(newItemRequest, user.get());

        return ItemMapper.mapItemDto(itemStorage.save(newItem));
    }

    @Transactional
    public ItemDto update(UpdateItemRequest updateItemRequest, long userId, long id) {

        Optional<User> user = userStorage.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id {0, number} не найден", userId));
        }

        Optional<Item> optionalUpdatedItem = itemStorage.findById(id);

        if (optionalUpdatedItem.isPresent()) {

            Item item = optionalUpdatedItem.get();

            if (!(item.getOwner().equals(user.get()))) {
                throw new InternalServerException("Текущий пользователь не является владельцем вещи");
            }
            ItemMapper.updateItemFields(item, updateItemRequest);

            Item updatedItem = itemStorage.save(item);
            return ItemMapper.mapItemDto(updatedItem);
        } else {
            throw new NotFoundException(MessageFormat.format("Вещь с id {0, number} не найден", id));
        }
    }

    @Transactional
    public CommentDto addComment(NewCommentRequest newCommentRequest, long userId, long itemId) {

        List<Booking> bookings = bookingRepository.findPastBookingsOfUserByItem(userId, itemId, Instant.now());

        if (!bookings.isEmpty()) {

            Booking booking = bookings.getFirst();

            Comment newComment = commentRepository.save(CommentMapper.mapToComment(newCommentRequest, booking.getBooker(), booking.getItem()));

            return CommentMapper.mapCommentDto(newComment, newComment.getAuthor().getName());
        } else {
            throw new InternalServerException("Бронирования вещи не найдены");
        }
    }

    public Collection<ItemDto> findByText(String text) {

        return itemStorage.findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseAAndAvailable(text).stream()
                .map(ItemMapper::mapItemDto)
                .toList();
    }
}

