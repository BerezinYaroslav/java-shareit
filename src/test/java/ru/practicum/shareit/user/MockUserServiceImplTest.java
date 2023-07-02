package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exceptions.DuplicateException;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;

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
public class MockUserServiceImplTest {
    private final UserDto user1 = new UserDto(1L, "1", "1@mail.com");
    private final UserDto user2 = new UserDto(2L, "2", "2@mail.com");
    private final UserDto user3 = new UserDto(3L, "3", "3@mail.com");
    private final UserDto user4 = new UserDto(4L, "4", "4@mail.com");

    @MockBean
    private final UserService service;

    @Test
    void createNewUser_returnUserDto() throws DuplicateException {
        when(service.addUser(any(UserDto.class)))
                .thenReturn(user1);

        UserDto user = service.addUser(user1);

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(user1.getName()));
        assertThat(user.getEmail(), equalTo(user1.getEmail()));
    }

    @Test
    void getAllUsers_returnListDto_existLength4() {
        List<User> users = new ArrayList<>();
        users.add(toUser(user1));
        users.add(toUser(user2));
        users.add(toUser(user3));
        users.add(toUser(user4));

        when(service.getUsers())
                .thenReturn(users);

        List<User> usersDto = service.getUsers();

        assertNotNull(usersDto, "Юзеров нет");
        assertEquals(4, usersDto.size(), "Количесвто юзеров не совпадает");
    }

    @Test
    void searchUserById_returnUserOne_existUserOne() {
        UserDto user = new UserDto(1L, "name1", "emai1@mail.com");

        when(service.getUserById(anyLong()))
                .thenReturn(user);

        UserDto foundUser = service.getUserById(user.getId());

        assertNotNull(foundUser, "Пользователь пуст");
        assertEquals(user.getId(), foundUser.getId(), "Ид не совпадает");
        assertEquals(user.getName(), foundUser.getName(), "имя не совпадает");
        assertEquals(user.getEmail(), foundUser.getEmail(), "емейл не совпадает");
    }

    @Test
    void searchUserById_expectedThrow_user99notExist() {
        when(service.getUserById(anyLong()))
                .thenThrow(new NotFoundException("User not found"));

        assertThrows(NotFoundException.class, () -> service.getUserById(99L), "Юзер 99 обнаружен");
    }

    @Test
    void updateUserWithEmailWrong_emailNull_expectedError() throws DuplicateException {
        when(service.updateUser(anyLong(), any(UserDto.class)))
                .thenThrow(NotAvailableException.class);

        UserDto user = user1;
        user.setEmail(null);

        assertThrows(NotAvailableException.class, () -> service.updateUser(1L, user));
    }

    @Test
    void updateUserWithEmailWrong_emailSpace_expectedError() throws DuplicateException {
        when(service.updateUser(anyLong(), any(UserDto.class)))
                .thenThrow(NotAvailableException.class);

        UserDto user = user1;
        user.setEmail(" ");

        assertThrows(NotAvailableException.class, () -> service.updateUser(1L, user));
    }

    @Test
    void updateUserWithEmailWrong_emailEmpty_expectedError() throws DuplicateException {
        when(service.updateUser(anyLong(), any(UserDto.class)))
                .thenThrow(NotAvailableException.class);

        UserDto user = user1;
        user.setEmail("");

        assertThrows(NotAvailableException.class, () -> service.updateUser(1L, user));
    }

    @Test
    void updateUserWithEmailWrong_emailWithSpace_expectedError() throws DuplicateException {
        when(service.updateUser(anyLong(), any(UserDto.class)))
                .thenThrow(NotAvailableException.class);

        UserDto user = user1;
        user.setEmail("email @mail.com");

        assertThrows(NotAvailableException.class, () -> service.updateUser(1L, user));
    }

    @Test
    void updateUser_userNotExist_expectedError() throws DuplicateException {
        when(service.updateUser(anyLong(), any(UserDto.class)))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.updateUser(99L, user1));
    }

    @Test
    void updateUserName_returnUSerDto() throws DuplicateException {
        UserDto updateUser = user1;
        updateUser.setName("Update name");

        when(service.updateUser(anyLong(), any(UserDto.class)))
                .thenReturn(updateUser);

        assertEquals(updateUser.getName(), service.updateUser(1L, updateUser).getName(), "Name not update");
    }
}
