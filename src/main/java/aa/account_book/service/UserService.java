package aa.account_book.service;

import aa.account_book.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(User user);
    Optional<User> findUserById(String userId);
    List<User> findAllUser();
    public User login(String userId, String password);
}
