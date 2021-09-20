package Matcher.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO {
    private @Getter
    @Setter
    String username;
    private @Getter
    @Setter
    String password;
}
