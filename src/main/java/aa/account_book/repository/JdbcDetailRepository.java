package aa.account_book.repository;

import aa.account_book.domain.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcDetailRepository implements DetailRepository{
    JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcDetailRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int insertDetail(Detail detail) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("detail").usingGeneratedKeyColumns("index");

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("userId", detail.getUserId());
        parameters.put("date", detail.getDate());
        parameters.put("time", detail.getTime());
        parameters.put("type", detail.getType());
        parameters.put("detail", detail.getDetail());
        parameters.put("amount", detail.getAmount());


        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        int index = key.intValue();
        detail.setIndex(index);

        return index;
    }

    @Override
    public Optional<Detail> readDetailByIndex(int index) {
        return jdbcTemplate.query("select * from detail where index = ?", detailRowMapper(), index).stream().findAny();
    }

    @Override
    public List<Detail> readDetailListToday(String userId) {
        System.out.println(LocalDate.now());
        return jdbcTemplate.query("select * from detail where userId = ? and date = ?", detailRowMapper(),  userId, LocalDate.now());
    }

    @Override
    public List<Detail> readDetailListByMonth(LocalDate firstDate, String userId) {
        LocalDate lastDate = getLastDayOfMonth(firstDate);
        return jdbcTemplate.query("select * from detail where userId = ? and date Between ? and  ?", detailRowMapper(),userId, firstDate, lastDate);
    }

    @Override
    public int updateDetail(Detail detail) {
        jdbcTemplate.update("update DETAIL set type = ?, detail = ?, amount = ? where index = ?", detail.getType(), detail.getDetail(), detail.getAmount(), detail.getIndex());

        return detail.getIndex();
    }

    private LocalDate getLastDayOfMonth(LocalDate firstDate) {
        YearMonth yearMonth = YearMonth.of(firstDate.getYear(), firstDate.getMonth());
        return yearMonth.atEndOfMonth();
    }
    private RowMapper<Detail> detailRowMapper() {
        return (rs, rowNum) -> {
            Detail detail = new Detail();
            detail.setIndex(rs.getInt("index"));
            detail.setUserId(rs.getString("userId"));
            detail.setDate(rs.getObject("date", LocalDate.class));
            detail.setTime(rs.getObject("time", LocalTime.class));
            detail.setType(rs.getString("type").charAt(0));
            detail.setDetail(rs.getString("detail"));
            detail.setAmount(rs.getInt("amount"));

            return detail;
        };
    }
}
