package ru.practicum.shareitgateway.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareitgateway.booking.dto.BookingStatus;
import ru.practicum.shareitgateway.item.ItemDto;
import ru.practicum.shareitgateway.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    @DateTimeFormat
    private LocalDateTime start;
    @DateTimeFormat
    private LocalDateTime end;
    @NotNull
    private ItemDto item;
    private User booker;
    private BookingStatus status;
}
