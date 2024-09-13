package ru.yandex.practicum.catsgram.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.model.Image;

import java.util.List;
import java.util.Optional;

@Repository
public class ImageRepository extends BaseRepository<Image> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM image_storage";
    private static final String FIND_BY_POST_QUERY = "SELECT * FROM image_storage WHERE post_id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM image_storage WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO image_storage(original_name, file_path, post_id)" +
            "VALUES (?, ?, ?) returning id";
    private static final String UPDATE_QUERY = "UPDATE image_storage SET originalFileName = ?, filePath = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM image_storage WHERE id = ?";
    private static final String STRINGS_COUNT_QUERY = "SELECT COUNT(id) FROM image_storage";

    public ImageRepository(JdbcTemplate jdbc, RowMapper<Image> mapper) {
        super(jdbc, mapper);
    }

    public List<Image> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Image> findById(long imageId) {
        return findOne(FIND_BY_ID_QUERY, imageId);
    }

    public List<Image> findByPostId(long postId) {
        return findMany(FIND_BY_POST_QUERY, postId);
    }

    public Image save(Image image) {
        long id = insert(INSERT_QUERY, image.getOriginalFileName(), image.getFilePath(),image.getPostId());
        image.setId(id);
        return image;
    }

    public Image update(Image image) {
        update(UPDATE_QUERY, image.getOriginalFileName(), image.getFilePath(), image.getId());
        return image;
    }

    public boolean delete(long id) {
        return delete(DELETE_QUERY, id);
    }

    public Integer stringsCount() {
        return stringsCount(STRINGS_COUNT_QUERY);
    }
}
