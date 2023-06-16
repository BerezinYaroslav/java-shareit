package ru.practicum.shareit.comment;

public class CommentMapper {
    public static Comment toObject(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getCreated()
        );
    }

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getItem().getId(),
                comment.getCreated()
        );
    }
}
