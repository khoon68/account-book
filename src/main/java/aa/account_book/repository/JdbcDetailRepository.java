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
    public Detail insertDetail(Detail detail) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("detail").usingGeneratedKeyColumns("index");

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("userId", detail.getUserId());
        parameters.put("date", detail.getDate());
        parameters.put("time", detail.getTime());
        parameters.put("type", detail.getType());
        parameters.put("detail", detail.getDetail());
        parameters.put("amount", detail.getAmount());
        parameters.put("balance", detail.getBalance());

        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        int index = key.intValue();
        detail.setIndex(index);

        return detail;
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
    public List<Detail> readDetailListByMonth(LocalDate date, String userId) {
        LocalDate[] firstDayAndLastDayOfMonth = getFirstDayAndLastDayOfMonth(date);
        return jdbcTemplate.query("select * from detail where userId = ? and date Between ? and  ?", detailRowMapper(),userId, firstDayAndLastDayOfMonth[0], firstDayAndLastDayOfMonth[1]);
    }

    private LocalDate[] getFirstDayAndLastDayOfMonth(LocalDate date) {
        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonth());
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        return new LocalDate[] {firstDay, lastDay};
    }

    public int findLatestDetailBalance(String userId) {
        String sql = "select * from detail where userId = ? order by index DESC";
        List<Detail> result = jdbcTemplate.query(sql, detailRowMapper(), userId);
        Optional<Detail> latestDetail = result.stream().findFirst();

        return latestDetail.map(Detail::getBalance).orElse(0);
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
            detail.setBalance(rs.getInt("balance"));

            return detail;
        };
    }
}
