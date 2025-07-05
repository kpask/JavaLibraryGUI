
package biblioteka.core;

/**
 * Exception thrown when an invalid ISBN (International Standard Book Number)
 * is encountered or provided. This class extends {@link BookException} and
 * stores the invalid ISBN string that caused the exception.
 *
 * @author kapa1135
 * @version 1.0
 * @see BookException
 * @see Knyga#setISBN(String)
 * @see Knyga#isValidISBN(String)
 */

public class InvalidISBNException extends BookException {
    private final String invalidISBN;

    public InvalidISBNException(String invalidISBN) {
        super("Neteisingas ISBN: " + invalidISBN);
        this.invalidISBN = invalidISBN;
    }

    public String getInvalidISBN() {
        return invalidISBN;
    }
}
