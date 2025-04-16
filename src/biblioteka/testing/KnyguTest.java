package biblioteka.testing;


import biblioteka.core.Knyga;
import biblioteka.core.Postponable;

import java.time.LocalDate;

public class KnyguTest {
    public static void main(String[] args) {
        Postponable postponableObject;

        postponableObject = new Knyga();
        System.out.println(postponableObject.isTaken());

        postponableObject.take("1231234", LocalDate.parse("2025-06-30"));
        System.out.println(postponableObject.isTaken());
    }
}