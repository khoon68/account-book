package aa.account_book.service;

import aa.account_book.domain.Detail;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DetailService {
    Detail addDetail(Detail detail);
    Detail cancelDetailByIndex(int index);
    List<Detail> findDetailListToday(String userid);
    List<Detail> findDetailListByMonth(LocalDate date, String userId);
}
