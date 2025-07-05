package biblioteka.core;

import java.time.LocalDate;

/**
 * Represents an electronic book (e-book) in the library.
 * This class extends {@link Knyga} and implements the {@link Downloadable} interface,
 * providing details specific to digital formats, such as file format, size in MB,
 * and a download counter. E-books typically cannot be borrowed in the traditional sense.
 *
 * @author kapa1135
 * @version 1.1
 * @see Knyga
 * @see Downloadable
 * @see Digital
 * @see InvalidISBNException
 */

// public abstract class Leidinys implements Postponable, Serializable { ... }
public class ElektronineKnyga extends Knyga implements Downloadable {
        private String failoFormatas;
        private double dydisMB;
        private int downloadCount;

        public ElektronineKnyga() {
            super();
            this.failoFormatas = "";
            this.dydisMB = 0;
            this.downloadCount = 0;
        }

        public ElektronineKnyga(String pavadinimas, int metai, int sk, String autorius, String zanras, String kalba, String leidykla, String isbn, double dydisMB, String failoFormatas) throws InvalidISBNException {
            super(pavadinimas, metai, sk, autorius, zanras, kalba, leidykla, isbn);
            this.setFailoFormatas(failoFormatas);
            this.setDydisMB(dydisMB);
            this.downloadCount = 0;
        }



    public void modify(String pavadinimas, int metai, int sk, String autorius, String zanras, String kalba, String leidykla, String isbn, String failoFormatas, double dydisMB) throws InvalidISBNException {
        super.modify(pavadinimas,  metai, sk, autorius, zanras, kalba, leidykla, isbn); // Modify fields from Knyga
        this.failoFormatas = failoFormatas; // Update ElektronineKnyga-specific fields
        this.dydisMB = dydisMB;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && !failoFormatas.isEmpty() && dydisMB > 0 && downloadCount >= 0;
    }

    public void download() {
        this.downloadCount++;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public double getDydisMB() {
        return dydisMB;
    }

    public void setDydisMB(double dydisMB) {
            if (dydisMB > 0){
                this.dydisMB = dydisMB;
            }
    }

    @Override
    public String getFailoFormatas() {
        return failoFormatas;
    }

    public void setFailoFormatas(String failoFormatas) {
        if (failoFormatas != null && !failoFormatas.isEmpty() &&
                (failoFormatas.equalsIgnoreCase("pdf") ||
                        failoFormatas.equalsIgnoreCase("epub") ||
                        failoFormatas.equalsIgnoreCase("mobi"))) {
            this.failoFormatas = failoFormatas;
        } else {
            throw new IllegalArgumentException("Netinkamas failo formatas. Galimi formatai: PDF, EPUB, MOBI.");
        }
    }


    @Override
    public String toString() {
        return super.toString()+"ElektronineKnyga{" +
                "failoFormatas='" + failoFormatas + '\'' +
                ", dydisMB=" + dydisMB +
                '}';
    }

    @Override
    public void take(String borrowerID, LocalDate takenUntil) {
        throw new UnsupportedOperationException("Elektronine knyga negali būti skolinama.");
    }

    @Override
    public boolean isTaken() {
        return false;
    }

}