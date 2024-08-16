package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean isUserWithEmailExist(String eMail) {
        return users.values().stream().anyMatch(userFromMemory -> userFromMemory.getEmail().equals(eMail));
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("E-mail должен быть указан");
        }

        if (isUserWithEmailExist(user.getEmail())) {
            throw new DuplicatedDataException("Этот E-mail уже используется");
        }

        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            if (!oldUser.getEmail().equals(newUser.getEmail()) && isUserWithEmailExist(newUser.getEmail())) {
                throw new DuplicatedDataException("Этот E-mail уже используется");
            }

            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
                oldUser.setEmail(newUser.getEmail());
            }

            if (newUser.getUsername() != null && !newUser.getUsername().isBlank()) {
                oldUser.setUsername(newUser.getUsername());
            }

            if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
                oldUser.setPassword(newUser.getPassword());
            }

            return oldUser;
        }

        throw new NotFoundException("Пост с id = " + newUser.getId() + " не найден");
    }
}
