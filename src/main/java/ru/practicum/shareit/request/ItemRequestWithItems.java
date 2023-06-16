package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemForRequest;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestWithItems {
    private Long id;
    private String description;
    private Timestamp created;
    private List<ItemForRequest> items;

    public ItemRequestWithItems(Long id, String description, Timestamp created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }
}
