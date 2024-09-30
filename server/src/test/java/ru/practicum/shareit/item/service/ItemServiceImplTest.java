package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.item.dto.ItemWithLastAndNextBookingsAndCommentsDto;
import ru.practicum.shareit.item.mapper.CommentMapperImpl;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ItemServiceImpl.class, ItemMapperImpl.class, UserMapperImpl.class, CommentMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Test
    void getAllByOwnerId() {
        // Given
        User user1 = userRepository.save(
                User.builder()
                        .name("user1")
                        .email("email1@email.com")
                        .build()
        );
        User user2 = userRepository.save(
                User.builder()
                        .name("user2")
                        .email("email2@email.com")
                        .build()
        );

        Item item1 = itemRepository.save(
                Item.builder()
                        .name("item1")
                        .description("description1")
                        .available(true)
                        .owner(user1)
                        .build()
        );

        // When
        List<ItemWithLastAndNextBookingsAndCommentsDto> items = itemService.getAllByOwnerId(user1.getId());

        // Then
        assertThat(items)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .isEqualTo(itemMapper.toDto(item1, List.of(), null, null));
    }
}