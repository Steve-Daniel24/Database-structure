package mypackage;
import java.util.*;

public class Domaine {
    private List<Object> valeursPermises;

    public Domaine() {
        this.valeursPermises = new ArrayList<>();
    }

    public Domaine(List<Object> valeursPermises) {
        this.valeursPermises = new ArrayList<>(valeursPermises);
    }

    public List<Object> getValeursPermises() {
        return valeursPermises;
    }

    public void ajouterValeurPermise(Object valeur) {
        if (!valeursPermises.contains(valeur)) {
            valeursPermises.add(valeur);
        }
    }

    public void supprimerValeurPermise(Object valeur) {
        valeursPermises.remove(valeur);
    }

    public boolean estValeurValide(Object valeur) {
        return valeursPermises.contains(valeur);
    }
}
