package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.enums.SortOrder;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{postId}")
    public Post findPostById(@PathVariable("postId") Long postId) {
        return postService.findPostById(postId).orElseThrow(() -> new NotFoundException("Пост № " + postId + " не найден"));
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(name = "sort", defaultValue = "desc") String sort,
                                    @RequestParam(name = "from", defaultValue = "0") Integer page,
                                    @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {

        SortOrder adjustedSort = SortOrder.from(sort);

        if (sort.isBlank()) {
            throw new ParameterNotValidException("sort", "Неверно указан тип сортировки " + sort +
                    ". Возможные варианты: \"ascending\", \"asc\", \"descending\", \"desc\"");
        }

        if (size <= 0) {
            throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
        }

        if (page < 0) {
            throw new ParameterNotValidException("page", "Начало выборки должно быть положительным числом");
        }

        return postService.findAll(adjustedSort, page, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}
