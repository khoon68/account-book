package aa.account_book.domain;

import lombok.Data;

@Data
public class User {
    private String userId;
    private String password;
    private String name;
    private int balance;

    public User() { }

    public User(String userId, String password, String name, int balance) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.balance = balance;
    }
}
