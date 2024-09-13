package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dto.post.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.post.PostDto;
import ru.yandex.practicum.catsgram.dto.post.UpdatePostRequest;
import ru.yandex.practicum.catsgram.enums.SortOrder;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/{postId}")
    public PostDto findPostById(@PathVariable("postId") Long postId) {
        return postService.findPostById(postId);
    }

    @GetMapping
    public Collection<PostDto> findAll(@RequestParam(name = "sort", defaultValue = "desc") String sort,
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
    public PostDto create(@RequestBody NewPostRequest post) {
        return postService.createPost(post);
    }

    @PutMapping("/{postId}")
    public PostDto update(@PathVariable("postId") Long postId, @RequestBody UpdatePostRequest post) {
        return postService.updatePost(postId, post);
    }

    @DeleteMapping("/{postId}")
    public PostDto deletePost(@PathVariable("postId") long postId) {
        return postService.deletePost(postId);
    }
}
