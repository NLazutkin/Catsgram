package ru.yandex.practicum.catsgram.dto.user;

import io.micrometer.common.util.StringUtils;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String email;
    private String password;

    public boolean hasEmail() {
        return !StringUtils.isBlank(email);
    }

    public boolean hasPassword() {
        return !StringUtils.isBlank(password);
    }

    public boolean hasUsername() {
        return !StringUtils.isBlank(username);
    }
}
