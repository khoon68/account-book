package aa.account_book.service;

import aa.account_book.domain.Detail;
import aa.account_book.repository.DetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DetailServiceImpl implements DetailService{

    @Autowired
    DetailRepository detailRepository;

    @Override
    public Detail addDetail(Detail detail) {
        int latestDetailBalance = detailRepository.findLatestDetailBalance(detail.getUserId());

        if(detail.getType() == '-') {
            if(detail.getAmount() > latestDetailBalance) {
                throw new RuntimeException("잔액이 취소해야 할 입금액보다 적습니다.");
            }
            detail.setBalance(latestDetailBalance - detail.getAmount());
        } else detail.setBalance(latestDetailBalance + detail.getAmount());

        return detailRepository.insertDetail(detail);
    }


    @Override
    public Detail cancelDetailByIndex(int index) {
        Optional<Detail> foundDetail = detailRepository.readDetailByIndex(index);

        if(foundDetail.isEmpty()) throw new RuntimeException("해당되는 내역이 존재하지 않습니다.");

        Detail detailToBeCanceled = foundDetail.get();
        Detail detailToBeAdded = new Detail();
        int latestDetailBalance = detailRepository.findLatestDetailBalance(detailToBeCanceled.getUserId());

        if(detailToBeCanceled.getType() == '+') {
            if(detailToBeCanceled.getAmount() > latestDetailBalance) {
                throw new RuntimeException("잔액이 취소해야 할 입금액보다 적습니다.");
            }
            detailToBeAdded.setType('-');
            detailToBeAdded.setBalance(latestDetailBalance - detailToBeCanceled.getAmount());
        } else {
            detailToBeAdded.setType('+');
            detailToBeAdded.setBalance(latestDetailBalance + detailToBeCanceled.getAmount());
        }
        
        detailToBeAdded.setUserId(detailToBeCanceled.getUserId());
        detailToBeAdded.setDate(LocalDate.now());
        detailToBeAdded.setTime(LocalTime.now());
        detailToBeAdded.setDetail(detailToBeCanceled.getDate() + " " + detailToBeCanceled.getTime() + "자 내역 취소");
        detailToBeAdded.setAmount(detailToBeCanceled.getAmount());
        return detailRepository.insertDetail(detailToBeAdded);
    }

    @Override
    public List<Detail> findDetailListToday(String userid) {
        return detailRepository.readDetailListToday(userid);
    }

    @Override
    public List<Detail> findDetailListByMonth(LocalDate date, String userId) {
        return detailRepository.readDetailListByMonth(date, userId);
    }
}
