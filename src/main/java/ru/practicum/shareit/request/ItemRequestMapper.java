package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester() != null ? itemRequest.getRequester() : null,
                itemRequest.getCreated()
        );
    }

    public static ItemRequest toObject(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getRequester() != null ? itemRequestDto.getRequester() : null,
                itemRequestDto.getCreated() != null ? itemRequestDto.getCreated() : Timestamp.valueOf(LocalDateTime.now())
        );
    }

    public static ItemRequestWithItems toObjectWith(ItemRequest itemRequest) {
        return new ItemRequestWithItems(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }
}