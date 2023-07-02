package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MockBookingServiceImplTest {
    @MockBean
    private final BookingService service;

    private final BookingEntryDto inputDto = BookingEntryDto.builder()
            .id(1L)
            .start(LocalDateTime.now().plusSeconds(20))
            .end(LocalDateTime.now().plusMinutes(2)).build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(inputDto.getStart())
            .end(inputDto.getEnd())
            .item(new Item())
            .booker(new User())
            .status(Status.WAITING).build();

    @Test
    void createNewBooking_expectedCorrect_returnDto() throws ValidationException {
        when(service.addBooking(anyLong(), any(BookingEntryDto.class)))
                .thenReturn(bookingDto);

        BookingDto booking = service.addBooking(1L, inputDto);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
    }

    @Test
    void createNewBooking_bookingTimeRentIsNotCorrect_expectedError() throws ValidationException {
        when(service.addBooking(anyLong(), any(BookingEntryDto.class)))
                .thenThrow(NotAvailableException.class);

        assertThrows(NotAvailableException.class, () -> service.addBooking(1L, inputDto));
    }

    @Test
    void createNewBooking_bookingCreatedOwnerItems_expectedError() throws ValidationException {
        when(service.addBooking(anyLong(), any(BookingEntryDto.class)))
                .thenThrow(NotAvailableException.class);

        assertThrows(NotAvailableException.class, () -> service.addBooking(2L, inputDto));
    }

    @Test
    void setStatusBooking_returnBookingDto() {
        when(service.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        BookingDto booking = service.approveBooking(1L, 1L, true);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
    }

    @Test
    void createNewBooking_bookingStatusNotWaiting_expectedError() {
        when(service.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(NotAvailableException.class);

        assertThrows(NotAvailableException.class, () -> service.approveBooking(1L, 1L, true));
    }

    @Test
    void createNewBooking_ownerApproveBooking_expectedError() {
        when(service.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(NotAvailableException.class);

        assertThrows(NotAvailableException.class, () -> service.approveBooking(1L, 1L, true));
    }

    @Test
    void getBookingById_returnDto() {
        when(service.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        BookingDto booking = service.getBookingById(1L, 1L);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
    }
}
