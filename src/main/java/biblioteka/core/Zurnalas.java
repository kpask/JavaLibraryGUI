package biblioteka.core;

/**
 * Represents a physical magazine or journal in the library.
 * This class extends {@link Leidinys} and adds magazine-specific details such as
 * issue number, editorial office, publication month, ISSN, and theme.
 *
 * @author kapa1135
 * @version 1.0
 * @see Leidinys
 */

public class Zurnalas extends Leidinys {
    private int numeris;
    private String redakcija;
    private String menuo;
    private String issn;
    private String tema;

    public Zurnalas(){
        this.numeris = 0;
        this.menuo = "";
        this.issn = "";
        this.tema = "";
        this.redakcija = "";
    }

    public Zurnalas(String pavadinimas, int metai, int sk, String redakcija, int numeris, String menuo, String issn, String tema) {
        super(pavadinimas, metai, sk);
        this.redakcija = redakcija;
        this.numeris = numeris;
        this.menuo = menuo;
        setIssn(issn);
        this.tema = tema;
    }

    public int getNumeris() {
        return numeris;
    }

    public void setNumeris(int numeris) {
        this.numeris = numeris;
    }

    public String getMenuo() {
        return menuo;
    }

    public void setMenuo(String menuo) {
        this.menuo = menuo;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        if(isValidISSN(issn)){
            this.issn = issn;
        } else{
            throw new IllegalArgumentException("Invalid ISSN: " + issn);
        }
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getRedakcija() {
        return redakcija;
    }

    public void setRedakcija(String redakcija) {
        this.redakcija = redakcija;
    }

    public boolean isValidISSN(String issn) {
        issn = issn.replaceAll("[^0-9X]", "").trim(); // Remove invalid characters
        if (issn.length() != 8) return false;

        int sum = 0;
        for (int i = 0; i < 7; i++) {
            if (!Character.isDigit(issn.charAt(i))) return false;
            sum += (issn.charAt(i) - '0') * (8 - i);
        }

        int checkDigit = (issn.charAt(7) == 'X') ? 10 : (issn.charAt(7) - '0');
        return (sum + checkDigit) % 11 == 0;
    }

    public void modify(String pavadinimas, int metai, int pSk, String redakcija, int numeris, String menuo, String issn, String tema){
        super.modify(pavadinimas, metai, pSk);
        this.redakcija = redakcija;
        this.numeris = numeris;
        this.menuo = menuo;
        setIssn(issn);
        this.tema = tema;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && isValidISSN(issn) && numeris > 0 & !tema.isEmpty() && !menuo.isEmpty();
    }


    @Override
    public String toString() {
        return super.toString() + "Zurnalas{" +
                "numeris=" + numeris +
                ", redakcija='" + redakcija + '\'' +
                ", menuo='" + menuo + '\'' +
                ", issn='" + issn + '\'' +
                ", tema='" + tema + '\'' +
                '}';
    }
}
