package ru.practicum.shareit.request.service;

import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequestDto> getRequests(Long userId) throws BadRequestException;

    List<ItemRequestDto> getRequestsFrom(Long userId, Integer from, Integer size) throws BadRequestException;

    ItemRequestDto getRequestsById(Long userId, Long requestId) throws BadRequestException;

    ItemRequestDto createRequests(Long userId, ItemRequestDto itemRequestDto) throws BadRequestException;

    void setId(Long id);
}
