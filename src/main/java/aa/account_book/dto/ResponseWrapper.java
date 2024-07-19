package aa.account_book.dto;

import lombok.Data;

@Data
public class ResponseWrapper<T> {
    private String message;
    private T data;
}
