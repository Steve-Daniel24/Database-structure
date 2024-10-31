import java.util.*;

public class Attribut {
    private String nomAttribut;
    private Domaine domaineAttribut;

    public Attribut(String nomattribuString, Domaine Type) {
        this.nomAttribut = nomattribuString;
        this.domaineAttribut = Type;
    }

    public Class<?> getTypeAttribut() {
        String type = domaineAttribut.getType();

        if (type.toLowerCase().equals("varchar")) {
            return String.class;
        } else if (type.toLowerCase().equals("number")) {
            return Integer.class;
        } else if (type.toLowerCase().equals("date")) {
            return Date.class;
        } else {
            return null;
        }
    }

    public String getNomAttribut(Integer index) {
        return this.nomAttribut;
    }

    public String getNomAttribut() {
        return this.nomAttribut;
    }

}
