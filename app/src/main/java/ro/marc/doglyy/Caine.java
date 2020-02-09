package ro.marc.doglyy;

import android.util.Log;

public class Caine {
    private String nume, rasa, varsta, desc;
    private String uid;

    Caine(String nume, String rasa, String varsta, String desc, String uid) {
        this.nume = nume;
        this.rasa = rasa;
        this.varsta = varsta;
        this.uid = uid;
        this.desc = desc;
    }

    public String iaNume() {
        return nume;
    }

    public String iaRasa() {
        return rasa;
    }

    public String iaVarsta() {
        return varsta;
    }

    public String iaDesc() {
        return desc;
    }

    public String iaUid() {
        return uid;
    }

    public void setNume(String s) {
        nume = s;
    }

    public void setNumes(String s) {
        nume = s;
    }

    public void setRasa(String s) {
        nume = s;
    }

    public void setVarsta(String s) {
        nume = s;
    }

    public void afis() {
        Log.w("caine marc", nume + " " + rasa + " " + varsta + " " + desc + " " + uid);
    }
}
