package biblioteka.core;

import java.time.LocalDate;

public interface Postponable extends Borrowable{
    void postpone(LocalDate newDate);
}
