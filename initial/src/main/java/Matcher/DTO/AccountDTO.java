package Matcher.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountDTO {
    private String username;
    private int gbp;
    private int btc;
}
