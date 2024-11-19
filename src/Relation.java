import java.util.*;

public class Relation {
    private String name;
    private ArrayList<Attribut> listColonne;
    private ArrayList<Uplet> nUplet;

    public Relation(String name, Attribut... col) {
        this.name = name;
        this.listColonne = new ArrayList<>();
        this.nUplet = new ArrayList<>();
        appendColumns(col);
    }

    public void appendColumns(Attribut... col) {
        Collections.addAll(listColonne, col);
    }

    public void insert(Uplet... ligne) {
        Collections.addAll(nUplet, ligne);
    }

    public void setListColonne(ArrayList<Attribut> listColonneadd) {
        listColonne = listColonneadd;
    }

    public void setNupplet(ArrayList<Uplet> listUpletsadd) {
        nUplet = listUpletsadd;
    }

    public ArrayList<Uplet> getNupplet() {
        return nUplet;
    }

    public ArrayList<Attribut> getListColonne() {
        return listColonne;
    }

    public Relation projection(String... nomColonne) {
        List<Attribut> newColumns = new ArrayList<>();

        for (String colonne : nomColonne) {
            int index = getIndexOfColumn(colonne);
            newColumns.add(listColonne.get(index));
        }

        Relation newRelation = new Relation(this.name + "_Projection", newColumns.toArray(new Attribut[0]));

        for (Uplet uplet : nUplet) {
            Uplet newUplet = new Uplet(newRelation);

            for (int i = 0; i < nomColonne.length; i++) {
                int index = getIndexOfColumn(nomColonne[i]);
                newUplet.setValeur(i, uplet.getValeur(index));
            }

            newRelation.insert(newUplet);
        }

        return newRelation;
    }

    public Relation selection(String condition) {
        ArrayList<Uplet> selectedUplets = new ArrayList<>();

        // Zaraina par 'or'
        String[] orConditions = condition.split(" or ");

        for (Uplet uplet : nUplet) {
            boolean orMatch = false;

            for (String orCond : orConditions) {
                // System.out.println("orcond : " + orCond);

                // Zaraina par 'and'
                String[] andConditions = orCond.trim().split(" and ");
                boolean andMatch = true;

                for (String andCond : andConditions) {
                    andCond = andCond.trim().replaceAll("[()]", "");

                    String[] parts = andCond.split(" ");

                    String nomColonne = parts[0];
                    String operateur = parts[1];
                    String valeur = parts[2].replaceAll("'", "");

                    if (!applyCondition(uplet, nomColonne, operateur, valeur)) {
                        andMatch = false;
                        break;
                    }
                }

                if (andMatch) {
                    orMatch = true;
                    break;
                }
            }

            if (orMatch) {
                selectedUplets.add(uplet);
            }
        }

        Relation selectedRelation = new Relation(this.name);

        ArrayList<Attribut> selectedListColonne = new ArrayList<>();
        for (Attribut colonne : this.listColonne) {
            Attribut newColonne = new Attribut(colonne.getNomAttribut(), colonne.getDomaine());
            selectedListColonne.add(newColonne);
        }

        selectedRelation.setListColonne(selectedListColonne);
        selectedRelation.setNupplet(selectedUplets);

        return selectedRelation;
    }

    private boolean applyCondition(Uplet uplet, String nomColonne, String operateur, String valeur) {

        int index = getIndexOfColumn(nomColonne);

        Object upletValue = uplet.getValeur(index);

        switch (operateur) {
            case "=":
                return upletValue.toString().equals(valeur);
            case "!=":
                return !upletValue.toString().equals(valeur);
            case "<":
                return Integer.parseInt(upletValue.toString()) < Integer.parseInt(valeur);
            case ">":
                return Integer.parseInt(upletValue.toString()) > Integer.parseInt(valeur);
            case "<=":
                return Integer.parseInt(upletValue.toString()) <= Integer.parseInt(valeur);
            case ">=":
                return Integer.parseInt(upletValue.toString()) >= Integer.parseInt(valeur);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operateur);
        }
    }

    private int getIndexOfColumn(String nomColonne) {
        for (int i = 0; i < listColonne.size(); i++) {
            if (listColonne.get(i).getNomAttribut().equalsIgnoreCase(nomColonne)) {
                return i;
            }
        }

        throw new IllegalArgumentException("Colonne non trouvée : " + nomColonne);
    }

    public Relation union(Relation r2) {
        if (this.listColonne.size() != r2.getListColonne().size()) {
            throw new IllegalArgumentException("Les relations n'ont pas les mêmes colonnes.");
        }

        ArrayList<Attribut> newColumns = new ArrayList<>();

        for (int i = 0; i < listColonne.size(); i++) {
            Attribut col1 = this.listColonne.get(i);
            Attribut col2 = r2.getListColonne().get(i);

            Domaine newDomaine = new Domaine(new ArrayList<>(col1.getDomaine().getValeursPermises()));

            for (Object value : col2.getDomaine().getValeursPermises()) {
                if (!newDomaine.getValeursPermises().contains(value)) {
                    newDomaine.ajouterValeurPermise(value);
                }
            }

            Attribut newCol = new Attribut(col1.getNomAttribut(), newDomaine);
            newColumns.add(newCol);
        }

        Relation result = new Relation("Union", newColumns.toArray(new Attribut[0]));

        for (Uplet uplet1 : this.nUplet) {
            Uplet newUplet = new Uplet(result);
            for (int i = 0; i < uplet1.getLigne().size(); i++) {
                newUplet.setValeur(i, uplet1.getValeur(i));
            }
            result.insert(newUplet);
        }

        for (Uplet uplet2 : r2.getNuplets()) {
            Uplet newUplet = new Uplet(result);
            for (int i = 0; i < uplet2.getLigne().size(); i++) {
                newUplet.setValeur(i, uplet2.getValeur(i));
            }
            result.insert(newUplet);
        }

        return result;
    }

    public Relation intersection(Relation r2) {
        if (this.listColonne.size() != r2.getListColonne().size()) {
            throw new IllegalArgumentException("Les relations n'ont pas les mêmes colonnes.");
        }

        ArrayList<Attribut> newColumns = new ArrayList<>();
        for (int i = 0; i < listColonne.size(); i++) {
            Attribut col1 = this.listColonne.get(i);
            Attribut col2 = r2.getListColonne().get(i);

            Domaine newDomaine = new Domaine(new ArrayList<>());
            for (Object value : col1.getDomaine().getValeursPermises()) {
                if (col2.getDomaine().getValeursPermises().contains(value)) {
                    newDomaine.ajouterValeurPermise(value);
                }
            }

            Attribut newCol = new Attribut(col1.getNomAttribut(), newDomaine);
            newColumns.add(newCol);
        }

        Relation result = new Relation("Intersection", newColumns.toArray(new Attribut[0]));

        for (Uplet uplet1 : this.nUplet) {
            for (Uplet uplet2 : r2.getNuplets()) {
                if (uplet1.equals(uplet2)) {
                    Uplet newUplet = new Uplet(result);
                    for (int i = 0; i < uplet1.getLigne().size(); i++) {
                        newUplet.setValeur(i, uplet1.getValeur(i));
                    }
                    result.insert(newUplet);
                }
            }
        }

        return result;
    }

    public Relation difference(Relation r2) {
        if (this.listColonne.size() != r2.getListColonne().size()) {
            throw new IllegalArgumentException("Les relations n'ont pas les mêmes colonnes.");
        }

        ArrayList<Attribut> newColumns = new ArrayList<>(this.listColonne);

        Relation result = new Relation("Difference", newColumns.toArray(new Attribut[0]));

        for (Uplet uplet1 : this.nUplet) {
            boolean existsInSecond = false;
            for (Uplet uplet2 : r2.getNuplets()) {
                if (uplet1.equals(uplet2)) {
                    existsInSecond = true;
                    break;
                }
            }
            if (!existsInSecond) {
                Uplet newUplet = new Uplet(result);
                for (int i = 0; i < uplet1.getLigne().size(); i++) {
                    newUplet.setValeur(i, uplet1.getValeur(i));
                }
                result.insert(newUplet);
            }
        }

        return result;
    }

    public Relation produitCartesien(Relation r2) {
        ArrayList<Attribut> newColumns = new ArrayList<>(this.listColonne);
        newColumns.addAll(r2.getListColonne());

        Relation newRelation = new Relation(this.name + "_ProduitCartesien", newColumns.toArray(new Attribut[0]));

        for (Uplet uplet1 : this.nUplet) {
            for (Uplet uplet2 : r2.getNuplets()) {
                Uplet combinedUplet = new Uplet(newRelation);

                int index = 0;
                for (Object value : uplet1.getLigne()) {
                    combinedUplet.setValeur(index++, value);
                }
                for (Object value : uplet2.getLigne()) {
                    combinedUplet.setValeur(index++, value);
                }

                newRelation.insert(combinedUplet);
            }
        }

        return newRelation;
    }

    public Relation jointure(Relation autreRelation, String colonne1, String operateur, String colonne2) {
        int indexColonne1 = this.getIndexOfColumn(colonne1);
        int indexColonne2 = autreRelation.getIndexOfColumn(colonne2);
    
        ArrayList<Attribut> nouvellesColonnes = new ArrayList<>(this.listColonne);
        for (Attribut attr : autreRelation.getListColonne()) {
            if (!nouvellesColonnes.contains(attr)) {
                nouvellesColonnes.add(attr);
            }
        }
    
        Relation nouvelleRelation = new Relation(this.name + "_join_" + autreRelation.name, 
                                                 nouvellesColonnes.toArray(new Attribut[0]));
    
        for (Uplet uplet1 : this.nUplet) {
            for (Uplet uplet2 : autreRelation.getNuplets()) {
                if (applyConditionDirect(uplet1.getValeur(indexColonne1), 
                                          operateur, 
                                          uplet2.getValeur(indexColonne2))) {
                    Uplet nouveauUplet = combinerUplets(uplet1, uplet2, indexColonne2, nouvelleRelation);
                    nouvelleRelation.insert(nouveauUplet);
                }
            }
        }
    
        return nouvelleRelation;
    }
    
    private boolean applyConditionDirect(Object valeur1, String operateur, Object valeur2) {
        switch (operateur) {
            case "=":
                return valeur1.equals(valeur2);
            case "!=":
                return !valeur1.equals(valeur2);
            case "<":
                return Integer.parseInt(valeur1.toString()) < Integer.parseInt(valeur2.toString());
            case ">":
                return Integer.parseInt(valeur1.toString()) > Integer.parseInt(valeur2.toString());
            case "<=":
                return Integer.parseInt(valeur1.toString()) <= Integer.parseInt(valeur2.toString());
            case ">=":
                return Integer.parseInt(valeur1.toString()) >= Integer.parseInt(valeur2.toString());
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operateur);
        }
    }
    
    private Uplet combinerUplets(Uplet uplet1, Uplet uplet2, int colonneIgnorée, Relation relation) {
        Uplet nouveauUplet = new Uplet(relation);
        int index = 0;
    
        for (Object val : uplet1.getLigne()) {
            nouveauUplet.setValeur(index++, val);
        }
    
        for (int i = 0; i < uplet2.getLigne().size(); i++) {
            if (i != colonneIgnorée) {
                nouveauUplet.setValeur(index++, uplet2.getValeur(i));
            }
        }
    
        return nouveauUplet;
    }
    

    public ArrayList<Uplet> getNuplets() {
        return nUplet;
    }

    public void display() {
        for (Attribut col : listColonne) {
            System.out.printf("| %-12s ", col.getNomAttribut());
        }
        System.out.println("|");

        for (Uplet uplet : nUplet) {
            for (Object value : uplet.getLigne()) {
                System.out.printf("| %-12s ", value == null ? "NULL" : value.toString());
            }
            System.out.println("|");
        }
    }
}

// public Relation intersection(Relation r2) {
// if (!this.listColonne.equals(r2.getListColonne())) {
// throw new IllegalArgumentException("Les relations n'ont pas les mêmes
// colonnes.");
// }

// Relation newRelation = new Relation(this.name + "_Intersection",
// listColonne.toArray(new Attribut[0]));

// for (Uplet uplet : nUplet) {
// if (r2.getNuplets().contains(uplet)) {
// newRelation.insert(uplet);
// }
// }

// return newRelation;
// }

// // Différence : retourne une nouvelle relation
// public Relation difference(Relation r2) {
// if (!this.listColonne.equals(r2.getListColonne())) {
// throw new IllegalArgumentException("Les relations n'ont pas les mêmes
// colonnes.");
// }

// Relation newRelation = new Relation(this.name + "_Difference",
// listColonne.toArray(new Attribut[0]));

// for (Uplet uplet : nUplet) {
// if (!r2.getNuplets().contains(uplet)) {
// newRelation.insert(uplet);
// }
// }

// return newRelation;
// }
