package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingItemDto;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestsRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.comment.CommentMapper.toDto;
import static ru.practicum.shareit.item.ItemMapper.toDto;
import static ru.practicum.shareit.item.ItemMapper.toObject;
import static ru.practicum.shareit.user.UserMapper.toObject;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemRequestsRepository requestsRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        itemDto.setOwner(toObject(userService.getUser(userId)));
        Item item = toObject(itemDto);

        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = requestsRepository.findById(itemDto.getRequestId()).orElseThrow(()
                    -> new NotFoundException("Request not found!"));
            item.setRequest(itemRequest);
        }

        return toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User Not found");
        }

        ItemDto item = toDto(itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found")));
        setBookings(item, userId);
        item.setComments(getComments(itemId));
        return item;
    }

    public List<ItemDto> searchItems(String text, int from, int size) throws ValidationException {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        if (from < 0 || size < 0) {
            throw new ValidationException("Not valid page");
        }

        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemRepository.findByNameOrDescriptionAvailable(text, pageable).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User Not found");
        }

        List<ItemDto> dtoList = itemRepository.findAllByOwnerId(userId).stream().map(ItemMapper::toDto).collect(Collectors.toList());
        dtoList.forEach(itemDto -> itemDto.setComments(getComments(itemDto.getId())));
        dtoList.forEach(itemDto -> setBookings(itemDto, userId));
        return dtoList;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
      Optional<Item> optionalItem = itemRepository.findById(itemId);

        if (optionalItem.isEmpty()) {
            throw new NotFoundException("item not found");
        }

        Item item = optionalItem.get();

        if (item.getOwner().getId() != userId.longValue()) {
            throw new NotFoundException("Another owner!");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return toDto(itemRepository.save(item));
    }

    @Override
    public CommentDto addComment(Long id, Long itemId, CommentDto commentDto) {
        Comment comment = new Comment(
                commentDto.getText()
        );
        comment.setAuthor(toObject(userService.getUser(id)));
        comment.setItem((itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("No such item"))));

        if (!bookingRepository.existsByBookerIdAndEndBeforeAndStatus(id, LocalDateTime.now(), Status.APPROVED)) {
            throw new NotAvailableException("You cant comment before use!");
        }

        comment.setCreated(LocalDateTime.now());
        return toDto(commentRepository.save(comment));
    }

    private void setBookings(ItemDto itemDto, Long userId) {
        if (itemDto.getOwner().getId().longValue() == userId.longValue()) {
            itemDto.setLastBooking(bookingRepository.findByItemId(itemDto.getId(), Sort.by(Sort.Direction.DESC, "start")).stream().filter(booking -> booking.getStart().isBefore(LocalDateTime.now())).map(BookingMapper::toItemDto).max(Comparator.comparing(BookingItemDto::getEnd)).orElse(null));
            itemDto.setNextBooking(bookingRepository.findByItemId(itemDto.getId(), Sort.by(Sort.Direction.ASC, "start")).stream().filter(booking -> !booking.getStatus().equals(Status.REJECTED)).map(BookingMapper::toItemDto).filter(booking -> booking.getStart().isAfter(LocalDateTime.now())).findFirst().orElse(null));
        }
    }

    private List<CommentDto> getComments(Long itemId) {
        return commentRepository.findByItemId(itemId).stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }
}
