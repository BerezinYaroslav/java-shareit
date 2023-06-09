package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                 @Valid @RequestBody BookingEntryDto bookingDto) throws ValidationException {
        return bookingService.addBooking(id, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        return bookingService.approveBooking(id, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long id,
                                     @PathVariable Long bookingId) {
        return bookingService.getBookingById(id, bookingId);
    }

    @Validated
    @GetMapping()
    public List<BookingDto> getAllBookingByState(@RequestHeader("X-Sharer-User-Id") Long id,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) throws ValidationException {
        return bookingService.getAllBookingByState(id, state, from, size);
    }

    @Validated
    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookings(@RequestHeader("X-Sharer-User-Id") Long id,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) throws ValidationException {
        return bookingService.getAllOwnersBookingByState(id, state, from, size);
    }
}
