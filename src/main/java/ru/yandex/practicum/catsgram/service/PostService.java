package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.PostRepository;
import ru.yandex.practicum.catsgram.dal.UserRepository;
import ru.yandex.practicum.catsgram.dto.post.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.post.PostDto;
import ru.yandex.practicum.catsgram.dto.post.UpdatePostRequest;
import ru.yandex.practicum.catsgram.enums.SortOrder;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.PostMapper;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public PostDto findPostById(long postId) {
        return postRepository.findById(postId)
                .map(PostMapper::mapToPostDto)
                .orElseThrow(() -> new NotFoundException("Пост с ID: " + postId + " не найден"));
    }

    public Collection<PostDto> findAll(SortOrder sort, Integer from, Integer size) {
        final Comparator<Post> comparator;

        if (sort.equals(SortOrder.ASCENDING)) {
            comparator = Comparator.comparing(Post::getPostDate, Comparator.naturalOrder());
        } else {
            comparator = Comparator.comparing(Post::getPostDate, Comparator.reverseOrder());
        }

        return postRepository.findAll()
                .stream()
                .sorted(comparator)
                .map(PostMapper::mapToPostDto)
                .toList()
                .subList(from, Integer.min(from + size, postRepository.stringsCount()));
    }

    public PostDto createPost(NewPostRequest request) {
        Optional<User> userExist = userRepository.findById(request.getAuthorId());
        if (userExist.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + request.getAuthorId());
        }

        Post post = PostMapper.mapToPost(request);
        post = postRepository.save(post);

        return PostMapper.mapToPostDto(post);
    }

    public PostDto updatePost(long postId, UpdatePostRequest request) {
        Post updatedPost = postRepository.findById(postId)
                .map(post -> PostMapper.updatePostFields(post, request))
                .orElseThrow(() -> new NotFoundException("Пост не найден"));
        updatedPost = postRepository.update(updatedPost);
        return PostMapper.mapToPostDto(updatedPost);
    }

    public PostDto deletePost(long postId) {
        Post deletedPost = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Пост не найден"));
        postRepository.delete(postId);
        return PostMapper.mapToPostDto(deletedPost);
    }
}