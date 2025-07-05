package biblioteka.core;

import java.time.LocalDate;

/**
 * Interface for library items that can be borrowed.
 * Defines methods to check if an item is currently taken and to mark it as
 * taken by a borrower until a specific date.
 *
 * @author kapa1135
 * @version 1.0
 * @see Leidinys
 * @see Postponable
 */

public interface Borrowable {
    boolean isTaken();
    void take(String borrowerID, LocalDate takenUntil);
}
