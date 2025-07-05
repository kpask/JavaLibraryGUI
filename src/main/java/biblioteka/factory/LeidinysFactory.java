package biblioteka.factory;


import biblioteka.core.Leidinys;;

public abstract class LeidinysFactory {
    public abstract Leidinys createLeidinys(
            String pavadinimas,
            int metai,
            int sk,
            String autorius,
            String zanras,
            String kalba,
            String leidykla,
            String isbn,
            String redakcija,
            int numeris,
            String menuo,
            String issn,
            String tema
    );
}
