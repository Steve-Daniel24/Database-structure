package mypackage;
import java.util.*;

public class Attribut {
    private String nomAttribut;
    private Domaine domaineAttribut;

    public Attribut(String nomAttribut, Domaine domaineAttribut) {
        this.nomAttribut = nomAttribut;
        this.domaineAttribut = domaineAttribut;
    }

    public String getNomAttribut() {
        return this.nomAttribut;
    }

    public Domaine getDomaine() {
        return this.domaineAttribut;
    }

    public boolean estValeurValide(Object valeur) {
        return domaineAttribut.estValeurValide(valeur);
    }
}
