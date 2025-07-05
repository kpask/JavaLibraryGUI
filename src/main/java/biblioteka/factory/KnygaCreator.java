package biblioteka.factory;

import biblioteka.core.InvalidISBNException;
import biblioteka.core.Knyga;
import biblioteka.core.Leidinys;

public class KnygaCreator extends LeidinysFactory{
    public Knyga createLeidinys(String pavadinimas, int metai, int sk,
                                   String autorius, String zanras, String kalba,
                                   String leidykla, String isbn,
                                   String redakcija, int numeris, String menuo,
                                   String issn, String tema) {
        try {
            return new Knyga(pavadinimas, metai, sk, autorius, zanras, kalba, leidykla, isbn);
        } catch (InvalidISBNException e) {
            e.printStackTrace();
            return null;
        }
    }
}
