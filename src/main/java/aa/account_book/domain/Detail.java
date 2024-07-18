package aa.account_book.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Detail {
    int index;
    String userId;
    LocalDate date;
    LocalTime time;
    char type;
    String detail;
    int amount;

    public Detail() {}

    public Detail(int index, String userId, LocalDate date, LocalTime time, char type, String detail, int amount) {
        this.index = index;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.type = type;
        this.detail = detail;
        this.amount = amount;
    }

    public Detail(String userId, LocalDate date, LocalTime time, char type, String detail, int amount) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.type = type;
        this.detail = detail;
        this.amount = amount;
    }
}
