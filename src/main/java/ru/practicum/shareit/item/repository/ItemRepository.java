package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(User owner);

    @Query("Select i from Item as i where i.available" +
            " and (UPPER(i.name) like UPPER(?1) Or UPPER(i.description) like UPPER(?1))")
    List<Item> findBySearchText(String text);
}