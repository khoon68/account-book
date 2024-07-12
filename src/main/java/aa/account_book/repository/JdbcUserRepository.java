package aa.account_book.repository;

import aa.account_book.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insertUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userid", user.getUserId());
        parameters.put("password", user.getPassword());
        parameters.put("name", user.getName());

        jdbcInsert.execute(parameters);
    }

    @Override
    public Optional<User> readUserById(String userId) {
        List<User> result = jdbcTemplate.query("select * from user where userId = ?", userRowMapper(), userId);
        return result.stream().findFirst();
    }

    @Override
    public List<User> readAllUser() {
        return jdbcTemplate.query("select * from user", userRowMapper());
    }

    @Override
    public void deleteUserByUserId(String userId) {
        jdbcTemplate.update("delete user where userId = ?", userId);
    }

    @Override
    public void updateUser(User user) {
        String sql = "update user set password = ?, name = ? where userId = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getUserId());
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getString("userId"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            return user;
        };
    }
}
