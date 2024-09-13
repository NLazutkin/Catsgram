package ru.yandex.practicum.catsgram.dto.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String originalFileName;
    private String filePath;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long postId;
}

