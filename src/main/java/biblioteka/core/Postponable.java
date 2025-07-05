package biblioteka.core;

import java.time.LocalDate;

/**
 * Interface for borrowable library items whose return date can be postponed.
 * Extends the {@link Borrowable} interface, adding a method to set a new
 * return date.
 *
 * @author kapa1135
 * @version 1.0
 * @see Borrowable
 * @see Leidinys
 */
public interface Postponable extends Borrowable{
    void postpone(LocalDate newDate);
}
