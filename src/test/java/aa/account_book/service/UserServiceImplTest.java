package aa.account_book.service;

import aa.account_book.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    UserService userService;

    @BeforeEach
    void beforeEach() {
        userService.registerUser( new User("test1", "pw1", "name1", 0));
    }

    @Test
    void registerUserSuccess() {
       userService.registerUser(new User("test2", "pw2", "name2", 0));
        Assertions.assertThat(userService.findUserById("test2").get().getName()).isEqualTo("name2");
    }

    @Test
    void registerUserFail() {
        Assertions.assertThatThrownBy(() -> userService.registerUser(
                new User("test1", "pw2", "name2", 0)
        )).isInstanceOf(IllegalStateException.class);
    }
}