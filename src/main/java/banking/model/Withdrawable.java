package banking.model;

import java.math.BigDecimal;

// Defines the withdrawal operation for eligible account types.
public interface Withdrawable {

    // Classes implementing this interface must provide withdrawal behaviour.
    void withdraw(BigDecimal amount);
}