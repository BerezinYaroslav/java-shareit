package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.toDto;
import static ru.practicum.shareit.user.UserMapper.toObject;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        return toDto(userRepository.save(toObject(userDto)));
    }

    @Override
    public UserDto getUser(Long id) {
        return toDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User userInfo = toObject(userDto);
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User oldUser = userOptional.get();

            if (userInfo.getName() != null) {
                oldUser.setName(userInfo.getName());
            }
            if (userInfo.getEmail() != null) {
                oldUser.setEmail(userInfo.getEmail());
            }

            return toDto(userRepository.save(oldUser));
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public UserDto deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            UserDto userDto = getUser(id);
            itemRepository.deleteAll(itemRepository.findAllByOwnerId(id));
            userRepository.deleteById(id);
            return userDto;
        }

        throw new NotFoundException("User not found");
    }
}
