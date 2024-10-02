package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface CommentMapper {
    @Mapping(target = "authorName", source = "author.name")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDto(List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    Comment toComment(NewCommentDto newCommentDto, Item item, User author, LocalDateTime created);
}