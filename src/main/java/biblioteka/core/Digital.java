package biblioteka.core;

/**
 * Interface for digital library items.
 * Defines methods to get and set the file format and size (in MB)
 * of a digital item.
 *
 * @author kapa1135
 * @version 1.0
 * @see Downloadable
 * @see ElektronineKnyga
 */
public interface Digital {
    String getFailoFormatas();
    void setFailoFormatas(String failoFormatas);
    double getDydisMB();
    void setDydisMB(double dydisMB);
}
