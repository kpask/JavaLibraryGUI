package biblioteka.core;

import java.time.LocalDate;

public interface Borrowable {
    boolean isTaken();
    void take(String borrowerID, LocalDate takenUntil);
}
