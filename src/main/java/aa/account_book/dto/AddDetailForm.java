package aa.account_book.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddDetailForm {
    @NotEmpty
    private String userId;
    @NotEmpty
    private char type;
    @NotEmpty
    private String detail;
    @NotEmpty
    private int amount;

    public AddDetailForm(String userId, char type, String detail, int amount) {
        this.userId = userId;
        this.type = type;
        this.detail = detail;
        this.amount = amount;
    }
}
