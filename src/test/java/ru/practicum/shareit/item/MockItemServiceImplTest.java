package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.user.UserMapper.toUser;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MockItemServiceImplTest {
    @MockBean
    private final ItemService service;

    private final UserDto user1 = new UserDto(
            1L,
            "user1",
            "user1@mail.com"
    );
    private final UserDto user2 = new UserDto(
            2L,
            "user2",
            "user2@mail.com"
    );
    private final ItemForRequest item1 = new ItemForRequest(
            "item1",
            "descr1",
            true,
            null
    );
    private final CommentDto commentDto1 = new CommentDto(
            1L,
            "text from comment1",
            user2.getName(),
            1L,
            LocalDateTime.now()
    );
    private final ItemDto itemDto2 = new ItemDto(
            2L,
            "item2",
            "descr2",
            true,
            toUser(user1),
            null,
            null,
            null,
            List.of(commentDto1)
    );
    private final ItemDto itemDto1 = new ItemDto(
            1L,
            "item1",
            "descr1",
            true,
            toUser(user1),
            null,
            null,
            null,
            new ArrayList<>()
    );
    private final Pageable pageable = PageRequest.of(0 / 10, 10);

    @Test
    void testGetAllItemsByOwnerId_returnListItemDto_length2() {
        when(service.getItems(anyLong()))
                .thenReturn(List.of(itemDto1, itemDto2));

        List<ItemDto> items = service.getItems(user1.getId());

        assertNotNull(items, "Предметов нет");
        assertEquals(2, items.size(), "Количесвто предметов не совпадает");
    }

    @Test
    void testCreateNewUser_returnItemDto() {
        when(service.addItem(anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto1);

        ItemDto item = service.addItem(user1.getId(), itemDto1);

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto1.getName()));
        assertThat(item.getDescription(), equalTo(itemDto1.getDescription()));
    }

    @Test
    void updateItem_correctUpdate() {
        ItemDto updateItem = itemDto1;
        updateItem.setAvailable(false);
        updateItem.setDescription("Update");
        updateItem.setName("Update");

        when(service.updateItem(anyLong(), any(ItemDto.class), anyLong()))
                .thenReturn(updateItem);

        ItemDto itemBeforeUpdate = service.updateItem(itemDto1.getId(), updateItem, user1.getId());

        assertEquals(updateItem.getName(), itemBeforeUpdate.getName(), "Name not update");
        assertEquals(updateItem.getAvailable(), itemBeforeUpdate.getAvailable(), "Available not update");
        assertEquals(updateItem.getDescription(), itemBeforeUpdate.getDescription(), "Description not update");
    }

    @Test
    void updateItem_allNull_expectedNull() {
        ItemDto updateItem = itemDto1;
        updateItem.setAvailable(null);
        updateItem.setDescription(null);
        updateItem.setName(null);

        when(service.updateItem(anyLong(), any(ItemDto.class), anyLong()))
                .thenReturn(null);

        ItemDto itemBeforeUpdate = service.updateItem(itemDto1.getId(), updateItem, user1.getId());

        assertNull(itemBeforeUpdate);
    }

    @Test
    void updateItem_availableIsNull_availableNotUpdate() {
        ItemDto updateItem = itemDto1;
        updateItem.setAvailable(null);

        when(service.updateItem(anyLong(), any(ItemDto.class), anyLong()))
                .thenReturn(itemDto1);

        ItemDto itemBeforeUpdate = service.updateItem(itemDto1.getId(), updateItem, user1.getId());

        assertEquals(itemDto1.getAvailable(), itemBeforeUpdate.getAvailable());
    }

    @Test
    void updateItem_descriptionIsNull_descriptionNotUpdate() {
        ItemDto updateItem = itemDto1;
        updateItem.setDescription(null);

        when(service.updateItem(anyLong(), any(ItemDto.class), anyLong())).thenReturn(itemDto1);

        ItemDto itemBeforeUpdate = service.updateItem(itemDto1.getId(), updateItem, user1.getId());

        assertEquals(itemDto1.getDescription(), itemBeforeUpdate.getDescription());
    }

    @Test
    void updateItem_userNotOwner_expectedError() {
        ItemDto updateItem = itemDto1;
        updateItem.setName("Name update");

        when(service.updateItem(anyLong(), any(ItemDto.class), anyLong()))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.updateItem(itemDto1.getId(), updateItem, user2.getId()));
    }

    @Test
    void getItemById_returnItemDto() {
        when(service.getItemById(anyLong(), anyLong())).thenReturn(itemDto1);

        ItemDto findItem = service.getItemById(1L, 1L);

        assertEquals(itemDto1.getName(), findItem.getName());
    }

    @Test
    void getItemByID_itemNotFound_expectedError() {
        when(service.getItemById(anyLong(), anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getItemById(99L, 1L));
    }

    @Test
    void addComment_returnCommentDto() {
        when(service.addComment(anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(commentDto1);

        CommentDto comment = new CommentDto();
        comment.setText("text from comment1");

        CommentDto commentDto = service.addComment(2L, 1L, comment);

        assertEquals(commentDto1.getText(), commentDto.getText());
    }

    @Test
    void addComment_commentIsNull_expectedError() {
        when(service.addComment(anyLong(), anyLong(), any(CommentDto.class)))
                .thenThrow(NotAvailableException.class);

        assertThrows(NotAvailableException.class, () -> service.addComment(2L, 1L, new CommentDto()));
    }
}
