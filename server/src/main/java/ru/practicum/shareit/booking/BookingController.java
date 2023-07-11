package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingEntity;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.BadRequestException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    @Autowired
    BookingService bookingService;

    @GetMapping("/owner")
    public List<BookingDto> getBookingsOwner(@RequestHeader("X-Sharer-User-Id") Long id,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "5") Integer size) throws BadRequestException {
        log.info("GET bookings with owner(userId) and state: {}, {}", id, state);
        return bookingService.getBookingsOwner(id, state, from, size);
    }

    @GetMapping
    public List<BookingDto> getBookingState(@RequestHeader("X-Sharer-User-Id") Long id,
                                            @RequestParam(defaultValue = "ALL") String state,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "5") Integer size) throws BadRequestException {
        log.info("GET bookings with userId and state: {}, {}", id, state);
        return bookingService.getBookingState(id, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("GET booking with userId and bookingId: {}, {}", userId, bookingId);
        return bookingService.getBooking(userId, bookingId);
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingEntity bookingEntity)
            throws BadRequestException {
        log.info("POST booking with userId: {}", userId);
        return bookingService.createBooking(userId, bookingEntity);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto bookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long bookingId, @RequestParam Boolean approved) throws BadRequestException {
        log.info("PATCH booking with userId and bookingId: {}, {}", userId, bookingId);
        return bookingService.bookingStatus(userId, bookingId, approved);
    }
}
