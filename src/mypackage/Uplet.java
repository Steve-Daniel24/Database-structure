package mypackage;

import java.util.*;

public class Uplet {
    private List<Object> valeur;
    private Relation relation;

    public Relation getRelation() {
        return relation;
    }

    public Uplet(Relation relation) {
        this.relation = relation;

        this.valeur = new ArrayList<>();
        for (int i = 0; i < relation.getListColonne().size(); i++) {
            valeur.add(null);
        }
    }

    public void setValeur(int index, Object valeur) {
        if (index >= 0 && index < this.valeur.size()) {
            Attribut attribut = relation.getListColonne().get(index);
            Domaine domaine = attribut.getDomaine();

            if (domaine.estValeurValide(valeur)) {
                this.valeur.set(index, valeur);
            } else {
                throw new IllegalArgumentException(
                        "Valeur '" + valeur + "' invalide pour l'attribut '" + attribut.getNomAttribut() +
                                "'. Valeurs permises : " + domaine.getValeursPermises());
            }
        } else {
            throw new IndexOutOfBoundsException("Index hors limites");
        }
    }

    // Obtenir la ligne complete
    public List<Object> getLigne() {
        return valeur;
    }

    // Obtenir la valeur d'une ligne d'un colonne donnée grace a son nom et le nom
    // de sa relation
    public Object getValeur(String nomColonne, Relation relation) {
        // Verifiena oe ao anatini nle uplet ve aloha ilay nom de colonne

        int indexValeur = 0;
        ArrayList<Attribut> listcolonne = relation.getListColonne();

        for (int i = 0; i < listcolonne.size(); i++) {
            if (listcolonne.get(i).getNomAttribut().equalsIgnoreCase(nomColonne)) {
                indexValeur = i;
                break;
            }
        }

        return valeur.get(indexValeur);
    }

    public Object getValeur(int index) {
        if (index >= 0 && index < valeur.size()) {
            return valeur.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index hors limites");
        }
    }

    // Verifiena ny clanssenle attribut eo amle index anle valeur eo amle uplet raha
    // mitovy
    // public Class<?> getExpectedType(int index) {
    // return relation.getListColonne().get(index).getTypeAttribut();
    // }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Uplet uplet = (Uplet) obj;
        return Objects.equals(valeur, uplet.valeur);
    }

    public Uplet fusion(Uplet autre) {
        // Vérifier que les relations sont compatibles pour une fusion
        List<Attribut> attributsNouvelleRelation = new ArrayList<>(this.relation.getListColonne());
        attributsNouvelleRelation.addAll(autre.getRelation().getListColonne());

        Relation relationFusion = new Relation(
                "Fusion_" + this.relation.getName() + "_" + autre.getRelation().getName(),
                attributsNouvelleRelation.toArray(new Attribut[0]));

        Uplet upletFusion = new Uplet(relationFusion);

        // Ajouter les valeurs de cet uplet
        for (int i = 0; i < this.valeur.size(); i++) {
            upletFusion.setValeur(i, this.getValeur(i));
        }

        // Ajouter les valeurs de l'autre uplet
        for (int j = 0; j < autre.getLigne().size(); j++) {
            upletFusion.setValeur(this.valeur.size() + j, autre.getValeur(j));
        }

        return upletFusion;
    }

}
