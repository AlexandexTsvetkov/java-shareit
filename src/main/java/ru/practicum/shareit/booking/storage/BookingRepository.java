package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.ItemLastAndNextBooking;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerIdOrderByStart(long bookerId);

    @Query("select book " +
            "from Booking as book " +
            "join book.booker as b " +
            "where b.id = ?1 " +
            "and ?2 between book.start and book.end " +
            "order by book.start")
    List<Booking> findCurrentBookingsByBookerIdOrderByStart(long bookerId, Instant currentDate);

    @Query("select book " +
            "from Booking as book " +
            "join book.booker as b " +
            "where b.id = ?1 " +
            "and ?2 > book.end " +
            "order by book.start")
    List<Booking> findPastBookingsByBookerIdOrderByStart(long bookerId, Instant currentDate);

    @Query("select book " +
            "from Booking as book " +
            "join book.booker as b " +
            "where b.id = ?1 " +
            "and book.status = ?2 ")
    List<Booking> findBookingsByBookerIdAndByStatusOrderByStart(long bookerId, String status);

    @Query("select book " +
            "from Booking as book " +
            "join book.booker as b " +
            "where b.id = ?1 " +
            "and ?2 < book.start " +
            "order by book.start")
    List<Booking> findFutureBookingsByBookerIdOrderByStart(long bookerId, Instant currentDate);

    @Query("select book " +
            "from Booking as book " +
            "join book.booker as b " +
            "join book.item as i " +
            "join i.owner as o " +
            "where book.id = ?1 " +
            "and (b.id = ?2 or o.id = ?2) " +
            "order by book.start")
    Optional<Booking> findBookingByIdAndOwnerOrBookerId(long bookerId, long userId);

    @Query("select book " +
            "from Booking as book " +
            "join book.item as i " +
            "join i.owner as o " +
            "where o.id = ?1 " +
            "order by book.start")
    List<Booking> findBookingsByOwner(long ownerId);

    @Query(nativeQuery = true)
    List<ItemLastAndNextBooking> findLastAndNextBookings(long ownerId, Instant currentDate);

    @Query("select book " +
            "from Booking as book " +
            "join book.item as i " +
            "join i.owner as o " +
            "where o.id = ?1 " +
            "and ?2 between book.start and book.end " +
            "order by book.start")
    List<Booking> findCurrentBookingsByOwner(long ownerId, Instant currentDate);

    @Query("select book " +
            "from Booking as book " +
            "join book.item as i " +
            "join i.owner as o " +
            "where o.id = ?1 " +
            "and ?2 > book.end " +
            "order by book.start")
    List<Booking> findPastBookingsByOwner(long ownerId, Instant currentDate);

    @Query("select book " +
            "from Booking as book " +
            "join book.item as i " +
            "join i.owner as o " +
            "where o.id = ?1 " +
            "and ?2 < book.start " +
            "order by book.start")
    List<Booking> findFutureBookingsByOwner(long ownerId, Instant currentDate);

    @Query("select book " +
            "from Booking as book " +
            "join book.item as i " +
            "join i.owner as o " +
            "where o.id = ?1 " +
            "and book.status = ?2 " +
            "order by book.start")
    List<Booking> findBookingsByOwnerAndStatus(long ownerId, String status);

    @Query("select book " +
            "from Booking as book " +
            "join book.item as i " +
            "join book.booker as b " +
            "where b.id = ?1 " +
            "and i.id = ?2 " +
            "and ?3 > book.end " +
            "and book.status <> 'REJECTED' " +
            "and book.status <> 'CANCELED' " +
            "order by book.start")
    List<Booking> findPastBookingsOfUserByItem(long bookerId, long itemId, Instant currentDate);
}
