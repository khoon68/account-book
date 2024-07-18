package aa.account_book.repository;

import aa.account_book.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void insertUser(User user);
    Optional<User> readUserById(String userId);
    List<User> readAllUser();
    void updateUserBalance(String userId, int balance);
}
