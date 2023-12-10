package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbGenreStorageTests {

    @Autowired
    private JdbcTemplate template;

//    @Test
    public void firstTest() {
        DbGenreStorage storage = new DbGenreStorage(template);
        storage.getById(15);
    }
}