package aa.account_book.repository;

import aa.account_book.domain.Detail;
import aa.account_book.domain.User;
import aa.account_book.dto.AddDetailForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@Transactional
class JdbcDetailRepositoryTest {
    @Autowired
    DetailRepository detailRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.insertUser(new User("test1", "pw1", "name1", 0));
        userRepository.insertUser(new User("test2", "pw2", "name2", 0));
    }

    @Test
    void readDetailByIndexTest() {
        int index = detailRepository.insertDetail(new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "테스트", 10000));

        Assertions.assertThat(detailRepository.readDetailByIndex(index).get().getDetail()).isEqualTo("테스트");
    }

    @Test
    public void readDetailToday() {
         detailRepository.insertDetail(new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "테스트", 10000));

         detailRepository.insertDetail(new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "테스트2", 20000));

        List<Detail> todayDetailList = detailRepository.readDetailListToday("test1");
        Assertions.assertThat(todayDetailList.size()).isEqualTo(2);
    }

    @Test
    public void readDetailListByMonth() {
        detailRepository.insertDetail(new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "테스트", 10000));

        detailRepository.insertDetail(new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "테스트2", 20000));

        List<Detail> julyDetailList = detailRepository.readDetailListByMonth(LocalDate.parse("2024-07-01"), "test1");
        for(Detail detail: julyDetailList) {
            System.out.println(detail.getDate() + " " + detail.getTime());
        }
        Assertions.assertThat(julyDetailList.size()).isEqualTo(2);
    }

    @Test
    void updateDetailTest() {
        int index = detailRepository.insertDetail(new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "테스트", 10000));

        detailRepository.updateDetail(new Detail(index, "test1", LocalDate.now(), LocalTime.now(), '+', "변경된 내역", 20000));

        Assertions.assertThat(detailRepository.readDetailByIndex(index).get().getDetail()).isEqualTo("변경된 내역");
    }
}