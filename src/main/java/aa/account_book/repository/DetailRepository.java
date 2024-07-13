package aa.account_book.repository;

import aa.account_book.domain.Detail;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DetailRepository {
    Detail insertDetail(Detail detail);
    Optional<Detail> readDetailByIndex(int index);
    List<Detail> readDetailListToday(String userId);
    List<Detail> readDetailListByMonth(LocalDate date, String userId);
}
