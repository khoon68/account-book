package aa.account_book.service;

import aa.account_book.domain.Detail;
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

    @Test
    void addDetailTest() {
        Detail detail1 = new Detail(0, "test1", LocalDate.now(), LocalTime.now(), '+', "내역 추가 확인용1", 10000, 0);
        Detail detail2 = new Detail(0, "test1", LocalDate.now(), LocalTime.now(), '-', "내역 추가 확인용2", 1000, 0);

        detailService.addDetail(detail1);
        Detail addedDetail = detailService.addDetail(detail2);

        Assertions.assertThat(addedDetail.getDetail()).isEqualTo(detail2.getDetail());
    }
    
    @Test
    void addDetailError() {
        Detail detail = new Detail(0, "test1", LocalDate.now(), LocalTime.now(), '-', "내역 추가 에러 확인용", 1000, 0);
        Assertions.assertThatThrownBy(() -> detailService.addDetail(detail)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("바로 이전에 추가한 내역을 취소하는 경우")
    void cancelDetailByIndexTest1() {
        Detail detail1 = new Detail(0, "test1", LocalDate.now(), LocalTime.now(), '+', "취소될 내역", 1000, 0);
        Detail addedDetail = detailService.addDetail(detail1);
        Detail detail2 = detailService.cancelDetailByIndex(addedDetail.getIndex());
        Assertions.assertThat(detail2.getBalance()).isEqualTo(0);
    }

    @Test
    @DisplayName("여러 개를 추가한 후 제일 먼저 추가한 내역을 취소하는 경우")
    void cancelDetailByIndexTest2() {
        Detail detail1 = new Detail(0, "test1", LocalDate.now(), LocalTime.now(), '+', "취소될 내역", 1000, 0);
        Detail detail2 = new Detail(0, "test1", LocalDate.now(), LocalTime.now(), '+', "취소되지 않을 내역", 2000, 0);
        Detail addedDetail = detailService.addDetail(detail1);
        detailService.addDetail(detail2);
        Detail detail3 = detailService.cancelDetailByIndex(addedDetail.getIndex());
        Assertions.assertThat(detail3.getBalance()).isEqualTo(2000);
    }

    @Test
    @DisplayName("존재하지 않는 인덱스를 입력했을 경우")
    void cancelDetailByIndexErrorTest1() {
        Assertions.assertThatThrownBy(() -> detailService.cancelDetailByIndex(-1)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("잔액이 입금 취소할 금액보다 작은 경우")
    void cancelDetailByIndexErrorTest2() {
        Detail detail1 = new Detail(0, "test1", LocalDate.now(), LocalTime.now(), '+', "취소할 내역", 2000, 0);
        Detail detail2 = new Detail(0, "test1", LocalDate.now(), LocalTime.now(), '-', "잔액 감소", 1000, 0);
        Detail addedDetail = detailService.addDetail(detail1);
        detailService.addDetail(detail2);
        Assertions.assertThatThrownBy(() -> detailService.cancelDetailByIndex(addedDetail.getIndex())).isInstanceOf(RuntimeException.class);
    }
}