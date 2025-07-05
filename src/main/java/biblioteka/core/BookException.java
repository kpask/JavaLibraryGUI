package biblioteka.core;

/**
 * Base custom exception for book-related operations within the library system.
 * Specific book-related exceptions should extend this class.
 *
 * @author kapa1135
 * @version 1.0
 * @see InvalidISBNException
 */

public class BookException extends Exception{
    public BookException(){
    }

    public BookException(String message) {
        super(message);
    }
}
