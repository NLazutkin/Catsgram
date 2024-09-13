package ru.yandex.practicum.catsgram.dto.image;

import lombok.Data;

@Data
public class NewImageRequest {
    private String originalFileName;
    private String filePath;
    private long postId;
}
