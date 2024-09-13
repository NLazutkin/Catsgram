package ru.yandex.practicum.catsgram.dto.post;

import lombok.Data;

import java.time.Instant;

@Data
public class NewPostRequest {
    private long authorId;
    private String description;
    private Instant postDate;
}
