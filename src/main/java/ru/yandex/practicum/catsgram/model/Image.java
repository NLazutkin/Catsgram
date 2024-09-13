package ru.yandex.practicum.catsgram.model;

import lombok.*;

@Data
@EqualsAndHashCode(of = {"id"})
public class Image {
    Long id;
    String originalFileName;
    String filePath;
    long postId;
}
