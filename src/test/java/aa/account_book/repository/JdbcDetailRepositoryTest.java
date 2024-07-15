package aa.account_book.repository;

import aa.account_book.domain.Detail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JdbcDetailRepositoryTest {
    @Autowired
    DetailRepository detailRepository;

    @BeforeEach
    void beforeEach() {
        detailRepository.insertDetail(
                new Detail(
                        0,
                        "test1",
                        LocalDate.parse("2024-06-14"),
                        LocalTime.parse("11:10:11"),
                        '+',
                        "테스트 내역5",
                        2000,
                        detailRepository.findLatestDetailBalance("test1") + 2000
                )
        );
        detailRepository.insertDetail(
                new Detail(
                        0,
                        "test1",
                        LocalDate.parse("2024-07-12"),
                        LocalTime.parse("11:10:11"),
                        '+',
                        "테스트 내역2",
                        5000,
                        detailRepository.findLatestDetailBalance("test1") + 5000
                )
        );
        detailRepository.insertDetail(
                new Detail(
                        0,
                        "test2",
                        LocalDate.parse("2024-07-12"),
                        LocalTime.parse("11:10:11"),
                        '+',
                        "테스트 내역3",
                        5000,
                        detailRepository.findLatestDetailBalance("test2") + 5000
                )
        );
        detailRepository.insertDetail(
                new Detail(
                        0,
                        "test1",
                        LocalDate.parse("2024-07-13"),
                        LocalTime.parse("11:10:10"),
                        '+',
                        "테스트 내역1",
                        10000,
                        detailRepository.findLatestDetailBalance("test1") + 10000
                )
        );
        detailRepository.insertDetail(
                new Detail(
                        0,
                        "test1",
                        LocalDate.parse("2024-07-14"),
                        LocalTime.parse("11:10:11"),
                        '+',
                        "테스트 내역4",
                        1000,
                        detailRepository.findLatestDetailBalance("test1") + 1000
                )
        );
    }

    @Test
    void readDetailByIndexTest() {
        Detail detail = new Detail();
        detail.setUserId("test1");
        detail.setDate(LocalDate.parse("2024-07-14"));
        detail.setTime(LocalTime.parse("12:10:10"));
        detail.setType('+');
        detail.setDetail("테스트 내역");
        detail.setAmount(10000);
        detail.setBalance(detailRepository.findLatestDetailBalance("test1") + 10000);
        Detail insertedDetail = detailRepository.insertDetail(detail);
        Detail readDetail = detailRepository.readDetailByIndex(insertedDetail.getIndex()).get();
        System.out.println(insertedDetail.getIndex());
        System.out.println(readDetail.getIndex());
        Assertions.assertThat(readDetail.getDetail()).isEqualTo(insertedDetail.getDetail());
    }

    @Test
    public void readDetailToday() {
        List<Detail> todayDetailList = detailRepository.readDetailListToday("test1");
        Assertions.assertThat(todayDetailList.size()).isEqualTo(1);
    }

    @Test
    public void readDetailListByMonth() {
        List<Detail> julyDetailList = detailRepository.readDetailListByMonth(LocalDate.parse("2024-06-01"), "test1");
        for(Detail detail: julyDetailList) {
            System.out.println(detail.getDate() + " " + detail.getTime());
        }
        Assertions.assertThat(julyDetailList.size()).isEqualTo(1);
    }
}