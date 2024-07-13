//package aa.account_book.config;
//
//import aa.account_book.controller.UserController;
//import aa.account_book.repository.JdbcUserRepository;
//import aa.account_book.service.UserService;
//import aa.account_book.service.UserServiceImpl;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import aa.account_book.repository.UserRepository;
//
//import javax.sql.DataSource;
//
//
//public class UserConfig {
//    private final DataSource dataSource;
//
//    public UserConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Bean
//    public UserRepository userRepository() {
//        return new JdbcUserRepository(dataSource);
//    }
//
//    @Bean
//    public UserService userService() {
//        return new UserServiceImpl(userRepository());
//    }
//}
