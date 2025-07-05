package biblioteka.core;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Represents a book in the library.
 * This class provides details about a book such as title, author, ISBN, etc.
 *
 * @author kapa1135
 * @version 1.0
 */

public class Knyga extends Leidinys implements Cloneable, Serializable {
    private String zanras;
    private String autorius;
    private String isbn;
    private String leidykla;
    private String kalba;

    public Knyga() {
        super();
        this.zanras = "";
        this.autorius = "";
        this.isbn = "";
        this.leidykla = "";
        this.kalba = "";
        this.isTaken = false;
        this.takenUntil = null;
        this.borrowerID = "";
    }

    // Constructor that initializes everything, including inherited fields
    public Knyga(String pavadinimas, int metai, int sk, String autorius, String zanras, String kalba, String leidykla, String isbn) throws InvalidISBNException {
        super(pavadinimas, metai, sk); // Pass values to Leidinys
        this.autorius = autorius;
        this.zanras = zanras;
        this.kalba = kalba;
        this.leidykla = leidykla;
        setISBN(isbn);
    }



    @Override
    public String toString() {
        return super.toString() + " Knyga{" +
                    "zanras='" + zanras + '\'' +
                    ", autorius='" + autorius + '\'' +
                    ", isbn='" + isbn + '\'' +
                    ", leidykla='" + leidykla + '\'' +
                    ", kalba='" + kalba + '\'' +
                    '}';
    }

    public void modify(String pavadinimas, int metai, int sk, String autorius, String zanras, String kalba, String leidykla, String isbn) throws InvalidISBNException {
        super.modify(pavadinimas, metai, sk);
        this.autorius = autorius;
        this.zanras = zanras;
        this.kalba = kalba;
        this.leidykla = leidykla;
        setISBN(isbn);
    }


    @Override
    public boolean isValid() {
        return super.isValid() && !zanras.isEmpty() && !autorius.isEmpty() && isValidISBN(isbn);
    }

    public boolean isValidISBN(String isbn) {
        int sum = 0;
        switch (isbn.length()){
            case 10:
                for(int i = 0; i < 10; i++){
                    if(isbn.charAt(i) == 'X'){
                        if(i!=9){
                            return false;
                        }
                        sum+= 10 * (10 - i);
                    } else if(isbn.charAt(i) >= '0' && isbn.charAt(i) <= '9'){
                        sum += (isbn.charAt(i) -'0') * (10 - i);
                    } else {
                        return false;
                    }
                }
                return sum % 11 == 0;

            case 13:
                int sum2 = 0;
                for(int i = 0; i < 12; i++){
                    if (isbn.charAt(i) >= '0' && isbn.charAt(i) <= '9'){
                        if(i % 2 == 0){ // Odd-positioned (1st, 3rd, etc.) should be *1
                            sum2 += (isbn.charAt(i) - '0');
                        }
                        else{ // Even-positioned (2nd, 4th, etc.) should be *3
                            sum2 += (isbn.charAt(i) - '0') * 3;
                        }
                    } else {
                        return false; // Non-digit character found
                    }
                }

                int checkDigit = (10 - (sum2 % 10)) % 10;
                return (isbn.charAt(12) - '0') == checkDigit;

            default:
                return false;
        }
    }

    @Override
    public Knyga clone() throws CloneNotSupportedException {
        Knyga copy = (Knyga) super.clone();
		// Gilus klonavimas, kopijuojami komentarai kurie yra saugomi ArrayList tipe (mutable) tipe
        copy.comments = new ArrayList<>(comments);
        return copy;
    }

    public void returnFromBorrow() {
        this.borrowerID = "";
        this.isTaken = false;
        this.takenUntil = null;
    }

    public String getBorrowerID(){ // Already exists
        return this.borrowerID;
    }


    public Knyga translate(String newLanguage) throws CloneNotSupportedException {
        Knyga translated = this.clone();
        translated.setKalba(newLanguage);
        return translated;
    }


    public String getISBN() {
        return isbn;
    }


    public void setISBN(String isbn) throws InvalidISBNException {
        String cleanISBN = isbn.replace("-", "");
        if(isValidISBN(cleanISBN)) {
            this.isbn = cleanISBN;
        } else{
            throw new InvalidISBNException(isbn);
        }
    }

    public String getLeidykla() {
        return leidykla;
    }

    public void setLeidykla(String leidykla) {
        this.leidykla = leidykla;
    }

    public String getKalba() {
        return kalba;
    }

    public void setKalba(String kalba) {
        this.kalba = kalba;
    }

    public String getZanras() {
        return zanras;
    }

    public void setZanras(String zanras) {
        if(!zanras.isEmpty()){
            this.zanras = zanras;
        }
    }

    public String getAutorius() {
        return autorius;
    }

    public void setAutorius(String autorius) {
        this.autorius = autorius;
    }

}