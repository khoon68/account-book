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
    public void withdrawUser(String userId, String password) {
        if(findUserById(userId).isPresent()) {
            if (findUserById(userId).get().getPassword().equals(password)) userRepository.deleteUserByUserId(userId);
            else throw new IllegalStateException("비밀번호가 일치하지 않습니다");
        }
    }

    @Override
    public void editUserInfo(User user) {

    }

    @Override
    public User login(String userId, String password) {
        return userRepository
                .readUserById(userId)
                .filter(user -> user.getPassword().equals(password))
                .orElse(new User("", "", ""));
    }
}
