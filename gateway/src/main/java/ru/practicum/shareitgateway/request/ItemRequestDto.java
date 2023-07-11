package ru.practicum.shareitgateway.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareitgateway.item.ItemDto;
import ru.practicum.shareitgateway.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDto {
    Long id;
    @NotBlank
    @Size(max = 250)
    String description;
    User requestor;
    @DateTimeFormat
    LocalDateTime created;
    List<ItemDto> items;
}
