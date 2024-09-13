package ru.yandex.practicum.catsgram.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.catsgram.dto.image.ImageDto;
import ru.yandex.practicum.catsgram.dto.image.NewImageRequest;
import ru.yandex.practicum.catsgram.dto.image.UpdateImageRequest;
import ru.yandex.practicum.catsgram.model.Image;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageMapper {

    public static ImageDto mapToImageDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setOriginalFileName(image.getOriginalFileName());
        dto.setFilePath(image.getFilePath());
        dto.setPostId(image.getPostId());
        return dto;
    }

    public static Image mapToImage(NewImageRequest request) {
        Image image = new Image();
        image.setOriginalFileName(request.getOriginalFileName());
        image.setFilePath(request.getFilePath());
        image.setPostId(request.getPostId());

        return image;
    }

    public static Image updateImageFields(Image image, UpdateImageRequest request) {
        if (request.hasOriginalFileName()) {
            image.setOriginalFileName(request.getOriginalFileName());
        }

        if (request.hasFilePath()) {
            image.setFilePath(request.getFilePath());
        }
        return image;
    }
}
