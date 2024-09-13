package ru.yandex.practicum.catsgram.dto.post;

import io.micrometer.common.util.StringUtils;
import lombok.Data;

@Data
public class UpdatePostRequest {
    private String description;

    public boolean hasDescription() {
        return !StringUtils.isBlank(description);
    }
}
