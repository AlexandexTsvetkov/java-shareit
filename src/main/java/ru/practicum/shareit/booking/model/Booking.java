package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemLastAndNextBooking;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Entity
@Table(name = "booking", schema = "public")
@Getter
@Setter
@ToString
@SqlResultSetMapping(
        name = "itemLastAndNextBookingMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ItemLastAndNextBooking.class,
                        columns = {
                                @ColumnResult(name = "itemId", type = Long.class),
                                @ColumnResult(name = "lastBooking", type = Instant.class),
                                @ColumnResult(name = "nextBooking", type = Instant.class)
                        }
                )
        }
)
@NamedNativeQuery(
        name = "Booking.findLastAndNextBookings",
        query = "select allitems.id as itemId, max(bookingsWithDates.lastBooking) as lastBooking, max(bookingsWithDates.nextBooking) as nextBooking " +
        "from item as allitems " +
        "left join (select b.item_id, max(b.start_date) as lastBooking, null as nextBooking " +
        "from booking as b " +
        "join item as i on b.item_id = i.id " +
        "where i.owner_id = ?1 " +
        "and b.end_date < ?2 " +
        "group by b.item_id " +
        "union all " +
        "select b.item_id, null as lastBooking, min(b.start_date) as nextBooking " +
        "from booking as b " +
        "join item as i on b.item_id = i.id " +
        "where i.owner_id = ?1 " +
        "and b.start_date > ?2 " +
        "group by b.item_id) as bookingsWithDates on allitems.id = bookingsWithDates.item_id "+
        "where allitems.owner_id = ?1 " +
        "group by allitems.id ",
        resultSetMapping = "itemLastAndNextBookingMapping"
)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_date")
    private Instant start;

    @Column(name = "end_date")
    private Instant end;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "booker_id")
    private User booker;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}

