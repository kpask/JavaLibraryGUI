package biblioteka.factory;

import biblioteka.core.InvalidISBNException;
import biblioteka.core.Knyga;
import biblioteka.core.Leidinys;
import biblioteka.core.Zurnalas;

public class ZurnalasCreator extends LeidinysFactory {
        public Zurnalas createLeidinys(String pavadinimas, int metai, int sk,
                                       String autorius, String zanras, String kalba,
                                       String leidykla, String isbn,
                                       String redakcija, int numeris, String menuo,
                                       String issn, String tema) {
            return new Zurnalas(pavadinimas, metai, sk, redakcija, numeris, menuo, issn, tema);
        }
    }

