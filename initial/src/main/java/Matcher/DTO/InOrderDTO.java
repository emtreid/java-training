package Matcher.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InOrderDTO {

    private @Getter
    @Setter
    String username;
    private @Getter
    @Setter
    String action;
    private @Getter
    @Setter
    long volume;
    private @Getter
    @Setter
    long price;

}
