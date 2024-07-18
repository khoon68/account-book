package aa.account_book.service;

import aa.account_book.domain.User;
import aa.account_book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public void registerUser(User user) {
        findUserById(user.getUserId()).ifPresent(u -> {
            throw new IllegalStateException("이 아이디로 생성된 계정이 존재합니다.");
        });
        userRepository.insertUser(user);
    }

    @Override
    public Optional<User> findUserById(String userId) {
        return userRepository.readUserById(userId);
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.readAllUser();
    }

    @Override
    public User login(String userId, String password) {
        return userRepository
                .readUserById(userId)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }
}
