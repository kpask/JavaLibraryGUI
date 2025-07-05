package biblioteka.core;

/**
 * Interface for digital library items that can be downloaded.
 * Extends the {@link Digital} interface, adding a method to signify
 * a download action, which might be used for tracking purposes.
 *
 * @author kapa1135
 * @version 1.0
 * @see Digital
 * @see ElektronineKnyga
 */
public interface Downloadable extends Digital{
    void download();
}
