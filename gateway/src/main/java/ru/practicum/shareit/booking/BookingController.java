package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingEntryDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.exception.NotSupportedStateException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping()
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                             @Valid @RequestBody BookingEntryDto bookingDto) {
        return bookingClient.addBooking(id, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam Boolean approved) {
        return bookingClient.approveBooking(id, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Long id,
                                                 @PathVariable Long bookingId) {
        return bookingClient.getBookingById(id, bookingId);
    }

    @Validated
    @GetMapping()
    public ResponseEntity<Object> getAllBookingByState(@RequestHeader("X-Sharer-User-Id") Long id,
                                                       @RequestParam(defaultValue = "ALL") String stateParam,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        State state = State.from(stateParam).orElseThrow(() -> new NotSupportedStateException("Unknown state: " + stateParam));
        return bookingClient.getAllBookingByState(id, state, from, size);
    }

    @Validated
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllItemsBookings(@RequestHeader("X-Sharer-User-Id") Long id,
                                                      @RequestParam(defaultValue = "ALL") String stateParam,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        State state = State.from(stateParam).orElseThrow(() -> new NotSupportedStateException("Unknown state: " + stateParam));
        return bookingClient.getAllOwnersBookingByState(id, state, from, size);
    }
}
