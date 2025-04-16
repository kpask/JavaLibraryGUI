package biblioteka.core;

import java.time.LocalDate;

public abstract class Leidinys implements Postponable{
    private static int knyguSk = 0;
    private static final int maxKnygos = 999;

    private String pavadinimas;
    private int metai;
    private int pSk;

    String borrowerID = "";
    LocalDate takenUntil = null;
    boolean isTaken = false;

    public Leidinys() {
        this("", 0, 0);
    }

    public Leidinys(String pavadinimas, int metai, int sk) {
        if (knyguSk >= maxKnygos) {
            throw new IllegalStateException("Negalima sukurti daugiau knygų, pasiektas limitas: " + maxKnygos);
        }

        this.pavadinimas = pavadinimas;
        this.metai = metai;
        this.pSk = sk;
        knyguSk++;
    }

    public void postpone(LocalDate date){
        this.takenUntil = date;
    }

    public void take(String borrowerID, LocalDate takenUntil){
        this.borrowerID = borrowerID;
        this.takenUntil = takenUntil;
        this.isTaken = true;
    }


    public void modify(String pavadinimas, int metai, int pSk) {
        this.pavadinimas = pavadinimas;
        this.metai = metai;
        this.pSk = pSk;
    }


    public final int amzius(){
        return java.time.Year.now().getValue() - this.metai;
    }

    public final int century(){
        int simtMet = this.metai / 100;

        if(this.metai - simtMet*100 != 0){
            simtMet++;
        }
        return(simtMet);
    }

    public boolean isValid() {
        return pavadinimas != null && !pavadinimas.isEmpty() && pSk > 0 && metai > 0;
    }

    public boolean isTaken(){
        return this.isTaken;
    }

    @Override
    public String toString() {
        return "Leidinys{" +
                "pavadinimas='" + pavadinimas + '\'' +
                ", metai=" + metai +
                ", pSk=" + pSk +
                '}';
    }

    public String getBorrowerID(){
        return this.borrowerID;
    }

    public String getPavadinimas() {
        return this.pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    public int getMetai() {
        return this.metai;
    }

    public void setMetai(int metai){
        this.metai = metai;
    }

    public int getSk(){
        return this.pSk;
    }

    public void setSk(int pSk){
        this.pSk = pSk;
    }

    public final static int getKnyguSk() {
        return knyguSk;
    }

}

