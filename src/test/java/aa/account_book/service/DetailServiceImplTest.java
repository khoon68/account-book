package aa.account_book.service;

import aa.account_book.domain.Detail;
import aa.account_book.domain.User;
import aa.account_book.repository.DetailRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DetailServiceImplTest {

    @Autowired
    DetailService detailService;

    @Autowired
    DetailRepository detailRepository;

    @Autowired UserService userService;

    @BeforeEach()
    void beforeEach() {
        userService.registerUser(new User("test1", "pw1", "name1", 0));
    }

    @Test
    void addDetailTest() {
        Detail detail1 = new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "내역 추가 확인용1", 10000);
        Detail detail2 = new Detail("test1", LocalDate.now(), LocalTime.now(), '-', "내역 추가 확인용2", 1000);

        int index1 = detailService.addDetail(detail1);
        int index2 = detailService.addDetail(detail2);

        Assertions.assertThat(detailRepository.readDetailByIndex(index2).get().getDetail()).isEqualTo(detail2.getDetail());
    }
    
    @Test
    @DisplayName("현재 잔액보다 더 큰 금액을 출금하는 경우")
    void addDetailError() {
        Detail detail = new Detail("test1", LocalDate.now(), LocalTime.now(), '-', "내역 추가 에러 확인용", 1000);
        Assertions.assertThatThrownBy(() -> detailService.addDetail(detail)).isInstanceOf(IllegalStateException.class).hasMessage("잔액이 출금액보다 적습니다.");
    }

    @Test
    @DisplayName("바로 이전에 추가한 내역을 수정하는 경우")
    void cancelDetailByIndexTest1() {
        Detail detail1 = new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "수정될 내역", 1000);
        int index = detailService.addDetail(detail1);

        detailService.editDetail(new Detail(index, detail1.getUserId(), LocalDate.now(), LocalTime.now(), '+', "수정한 내역", 0));
        Assertions.assertThat(detailRepository.readDetailByIndex(index).get().getDetail()).isEqualTo("수정한 내역");
    }



    @Test
    @DisplayName("존재하지 않는 인덱스를 입력했을 경우")
    void updateDetailErrorTest1() {
        Detail detail1 = new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "수정될 내역", 1000);
        int index = detailService.addDetail(detail1);

        Assertions.assertThatThrownBy(() -> detailService.editDetail(new Detail(-1, detail1.getUserId(), LocalDate.now(), LocalTime.now(), '+', "수정한 내역", 0))).isInstanceOf(IllegalStateException.class).hasMessage("해당되는 내역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("잔액이 수정한 내역으로 0보다 작아질 경우")
    void cancelDetailByIndexErrorTest2() {
        Detail detail1 = new Detail("test1", LocalDate.now(), LocalTime.now(), '+', "취소할 내역", 2000);
        Detail detail2 = new Detail("test1", LocalDate.now(), LocalTime.now(), '-', "잔액 감소", 1000);

        int index = detailService.addDetail(detail1);
        detailService.addDetail(detail2);

        Assertions.assertThatThrownBy(
                () -> detailService.editDetail(new Detail(index, "test1", LocalDate.now(), LocalTime.now(), '+', "수정한 내역", 500))
        ).isInstanceOf(IllegalStateException.class).hasMessage("잔액이 0보다 작아져 수정할 수 없습니다.");
    }
}