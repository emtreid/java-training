package Matcher.components.Account;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UserData {
    private @Getter
    final
    long GBP;
    private @Getter
    long BTC;

    public UserData(Account account) {
        GBP = account.getGbp(); //new BigDecimal(account.getBalanceGBP()).divide(new BigDecimal(10000), RoundingMode.HALF_UP);
        BTC = account.getBtc(); //new BigDecimal(account.getBalanceBTC()).divide(new BigDecimal(100), RoundingMode.HALF_UP);
    }
}
