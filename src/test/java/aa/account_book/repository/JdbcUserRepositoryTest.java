package aa.account_book.repository;

import aa.account_book.domain.User;
import aa.account_book.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class JdbcUserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.insertUser(new User("test1", "pw1", "name1", 0));
        userRepository.insertUser(new User("test2", "pw2", "name2", 0));
    }

    @Test
    void readUserById() {
        User test1User = userRepository.readUserById("test1").get();
        Assertions.assertThat(test1User.getName()).isEqualTo("name1");
    }

    @Test
    void readAllUser() {
        Assertions.assertThat(userRepository.readAllUser().size()).isEqualTo(2);
    }

    @Test
    void updateUserBalance() {
        userRepository.updateUserBalance("test1", 1000);
        Assertions.assertThat(userRepository.readUserById("test1").get().getBalance()).isEqualTo(1000);
    }
}