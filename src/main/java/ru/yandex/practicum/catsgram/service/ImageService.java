package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.dal.ImageRepository;
import ru.yandex.practicum.catsgram.dto.image.ImageDto;
import ru.yandex.practicum.catsgram.dto.image.NewImageRequest;
import ru.yandex.practicum.catsgram.dto.post.PostDto;
import ru.yandex.practicum.catsgram.exception.ImageFileException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.ImageMapper;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.ImageData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private final PostService postService;
    private final ImageRepository imageRepository;
    @Value("${catsgram.image-directory}")
    private String imageDirectory;

    @Autowired
    public ImageService(PostService postService, ImageRepository imageRepository) {
        this.postService = postService;
        this.imageRepository = imageRepository;
    }

    public ImageDto findImageById(long imageId) {
        return imageRepository.findById(imageId)
                .map(ImageMapper::mapToImageDto)
                .orElseThrow(() -> new NotFoundException("Изображение с ID: " + imageId + " не найдено"));
    }

    public List<ImageDto> getPostImages(long postId) {
        PostDto post = postService.findPostById(postId);
        return imageRepository.findByPostId(post.getId())
                .stream()
                .map(ImageMapper::mapToImageDto)
                .toList();
    }

    // сохранение файла изображения
    private Path saveFile(MultipartFile file, PostDto post) {
        try {
            // формирование уникального названия файла на основе текущего времени и расширения оригинального файла
            String uniqueFileName = String.format("%d.%s", Instant.now().toEpochMilli(),
                    StringUtils.getFilenameExtension(file.getOriginalFilename()));

            // формирование пути для сохранения файла с учётом идентификаторов автора и поста
            Path uploadPath = Paths.get(imageDirectory, String.valueOf(post.getAuthorId()), String.valueOf(post.getId()));
            Path filePath = uploadPath.resolve(uniqueFileName);

            // создаём директории, если они ещё не созданы
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // сохраняем файл по сформированному пути
            file.transferTo(filePath);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // сохранение отдельного изображения, связанного с указанным постом
    private ImageDto saveImage(long postId, MultipartFile file) {
        PostDto post = postService.findPostById(postId);

        // сохраняем изображение на диск и возвращаем путь к файлу
        Path filePath = saveFile(file, post);

        // создаём объект для хранения данных изображения
        // создание объекта изображения и заполнение его данными
        NewImageRequest request = new NewImageRequest();
        // запоминаем название файла, которое было при его передаче
        request.setOriginalFileName(file.getOriginalFilename());
        request.setFilePath(filePath.toString());
        request.setPostId(postId);

        Image image = ImageMapper.mapToImage(request);
        image = imageRepository.save(image);

        return ImageMapper.mapToImageDto(image);
    }

    // сохранение списка изображений, связанных с указанным постом
    public List<ImageDto> saveImages(long postId, List<MultipartFile> files) {
        return files.stream().map(file -> saveImage(postId, file)).collect(Collectors.toList());
    }

    // загружаем данные указанного изображения с диска
    public ImageData getImageData(long imageId) {
        ImageDto image = findImageById(imageId);
        // загрузка файла с диска
        byte[] data = loadFile(image);

        return new ImageData(data, image.getOriginalFileName());
    }

    private byte[] loadFile(ImageDto image) {
        Path path = Paths.get(image.getFilePath());
        if (Files.exists(path)) {
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new ImageFileException("Ошибка чтения файла.  Id: " + image.getId()
                        + ", name: " + image.getOriginalFileName(), e);
            }
        } else {
            throw new ImageFileException("Файл не найден. Id: " + image.getId()
                    + ", name: " + image.getOriginalFileName());
        }
    }
}