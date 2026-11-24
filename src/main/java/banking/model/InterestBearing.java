package banking.model;

import java.math.BigDecimal;

// Defines the interest calculation behaviour.
public interface InterestBearing {

    // Each account type calculates its own monthly interest.
    BigDecimal calculateMonthlyInterest();
}