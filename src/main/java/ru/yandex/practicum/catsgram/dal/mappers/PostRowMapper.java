package ru.yandex.practicum.catsgram.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.catsgram.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Post user = new Post();
        user.setId(resultSet.getLong("id"));
        user.setAuthorId(resultSet.getLong("author_id"));
        user.setDescription(resultSet.getString("description"));

        Timestamp postDate = resultSet.getTimestamp("post_date");
        user.setPostDate(postDate.toInstant());

        return user;
    }
}
