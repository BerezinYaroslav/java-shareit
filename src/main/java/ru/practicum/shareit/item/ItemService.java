package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.CommentDto;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto getItem(Long itemId, Long userId);

    List<ItemDto> searchItems(String text, int from, int size) throws ValidationException;

    List<ItemDto> getAllItems(Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId);

    ItemDto deleteItem(Long itemId, Long userId);

    CommentDto addComment(Long id, Long itemId, CommentDto commentDto);
}
