package aa.account_book.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;

@Data
public class RegisterForm {

    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;
}
