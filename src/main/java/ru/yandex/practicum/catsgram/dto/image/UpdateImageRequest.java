package ru.yandex.practicum.catsgram.dto.image;

import io.micrometer.common.util.StringUtils;
import lombok.Data;

@Data
public class UpdateImageRequest {
    private String originalFileName;
    private String filePath;

    public boolean hasOriginalFileName() {
        return !StringUtils.isBlank(originalFileName);
    }

    public boolean hasFilePath() {
        return !StringUtils.isBlank(filePath);
    }
}
