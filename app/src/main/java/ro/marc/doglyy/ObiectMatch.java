package ro.marc.doglyy;

public class ObiectMatch {
    private String nume_caine, varsta, rasa;

    ObiectMatch(String nume_caine, String varsta, String rasa) {
        this.nume_caine = nume_caine;
        this.varsta = varsta;
        this.rasa = rasa;
    }

    public String iaDate() {
        return nume_caine + ", " + varsta + " ani, " + rasa;
    }
}
