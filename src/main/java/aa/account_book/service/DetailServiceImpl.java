package aa.account_book.service;

import aa.account_book.domain.Detail;
import aa.account_book.domain.User;
import aa.account_book.repository.DetailRepository;
import aa.account_book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DetailServiceImpl implements DetailService{

    @Autowired
    DetailRepository detailRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public int addDetail(Detail detail) {
        User user = getUser(detail);
        int latestDetailBalance = user.getBalance();

        if(detail.getType() == '-') {
            if(detail.getAmount() > latestDetailBalance) {
                throw new IllegalStateException("잔액이 출금액보다 적습니다.");
            }
            userRepository.updateUserBalance(user.getUserId(), latestDetailBalance - detail.getAmount());
        } else userRepository.updateUserBalance(user.getUserId(), latestDetailBalance + detail.getAmount());


        return detailRepository.insertDetail(detail);
    }

    @Transactional
    @Override
    public int editDetail(Detail updatedDetail) {
        Optional<Detail> foundDetail = detailRepository.readDetailByIndex(updatedDetail.getIndex());

        if(foundDetail.isEmpty()) throw new IllegalStateException("해당되는 내역이 존재하지 않습니다.");
        Detail originalDetail = foundDetail.get();

        User user = getUser(updatedDetail);
        int latestDetailBalance = user.getBalance();

        int originalDetailAmount = originalDetail.getAmount();
        int updatedDetailAmount = updatedDetail.getAmount();

        if(originalDetail.getType() == '+') originalDetailAmount *= -1;
        if(updatedDetail.getType() == '-') updatedDetailAmount *= -1;

        int updatedDetailBalance = latestDetailBalance + originalDetailAmount + updatedDetailAmount;

        if(updatedDetailBalance < 0)
            throw new IllegalStateException("잔액이 0보다 작아져 수정할 수 없습니다.");

        int index = detailRepository.updateDetail(updatedDetail);
        userRepository.updateUserBalance(updatedDetail.getUserId(), updatedDetailBalance);

        return index;
    }

    @Override
    public List<Detail> findDetailListToday(String userid) {
        return detailRepository.readDetailListToday(userid);
    }

    @Override
    public List<Detail> findDetailListByMonth(LocalDate date, String userId) {
        return detailRepository.readDetailListByMonth(date, userId);
    }

    private User getUser(Detail detail) {
        Optional<User> optionalUser = userRepository.readUserById(detail.getUserId());
        if(optionalUser.isEmpty()) throw new IllegalStateException("해당 내역의 유저가 존재하지 않습니다.");

        User user = optionalUser.get();
        return user;
    }
}
