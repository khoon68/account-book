package aa.account_book.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DetailForm {
    @NotEmpty
    private String userId;
    @NotEmpty
    private char type;
    @NotEmpty
    private String detail;
    @NotEmpty
    private int amount;
}
