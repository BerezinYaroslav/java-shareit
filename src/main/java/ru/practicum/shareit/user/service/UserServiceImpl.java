package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        if (repository.checkDuplicateEmail(user.getEmail())) {
            log.info("User with email " + userDto.getEmail() + " already exists");
            throw new DuplicateException("User with email " + userDto.getEmail() + " already exists");
        }

        log.info("User " + user.getName() + " created successfully");
        return UserMapper.toUserDto(repository.createUser(user));
    }

    @Override
    public UserDto getUser(Long id) {
        Optional<User> optionalUser = repository.getUser(id);

        if (optionalUser.isEmpty()) {
            log.info("User with id " + id + " not found for getting");
            throw new NotFoundException("User with id " + id + " not found for getting");
        }

        log.info("User with id " + id + " returned successfully");
        return UserMapper.toUserDto(optionalUser.get());
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = new ArrayList<>();

        for (User user : repository.getAllUsers()) {
            usersDto.add(UserMapper.toUserDto(user));
        }

        log.info("All users returned successfully");
        return usersDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User userForUpdate = UserMapper.toUser(getUser(id));

        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            if (repository.checkDuplicateEmail(userDto.getEmail())) {
                if (!userDto.getEmail().equals(userForUpdate.getEmail())) {
                    log.info("User with email " + userDto.getEmail() + " already exists");
                    throw new DuplicateException("User with email " + userDto.getEmail() + " already exists");
                }
            }

            userForUpdate.setEmail(userDto.getEmail());
        }

        log.info("User " + userForUpdate.getName() + " updated successfully");
        return UserMapper.toUserDto(repository.updateUser(userForUpdate, id));
    }

    @Override
    public UserDto deleteUser(Long id) {
        Optional<User> optionalUser = repository.deleteUser(id);

        if (optionalUser.isEmpty()) {
            log.info("User with id " + id + " not found for deleting");
            throw new NotFoundException("User with id " + id + " not found for deleting");
        }

        log.info("User with id " + id + " deleted successfully");
        return UserMapper.toUserDto(optionalUser.get());
    }
}
