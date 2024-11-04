import java.util.ArrayList;

public class Relation {
    String name;
    ArrayList<Attribut> listColonne;
    ArrayList<Uplet> nUplet;

    // La liste des colonnes a afficher dans display
    String[] listColonneAfficher = new String[0];

    public Relation(String name, Attribut... col) {
        this.name = name;
        this.listColonne = new ArrayList<>();
        this.nUplet = new ArrayList<>();
        appendColumns(col);
    }

    public void appendColumns(Attribut... col) {
        for (Attribut c : col) {
            listColonne.add(c);
        }
    }

    public void insert(Uplet... ligne) {
        for (Uplet uplet : ligne) {
            nUplet.add(uplet);
        }
    }

    public ArrayList<Attribut> getListColonne() {
        return listColonne;
    }

    public ArrayList<Uplet> getNuplets() {
        return nUplet;
    }

    public ArrayList<Uplet> projection(String... nomColonne) {
        ArrayList<Uplet> result = new ArrayList<>();
        ArrayList<Uplet> nUplet = this.getNuplets();

        for (int i = 0; i < nomColonne.length; i++) {
            listColonneAfficher[i] = nomColonne[i];
        }

        if (nomColonne.length == 1 && nomColonne[0].equals("*")) {
            for (Uplet uplet : nUplet) {
                Uplet projectedUplet = new Uplet(this);

                for (int i = 0; i < uplet.getLigne().size(); i++) {
                    projectedUplet.setValeur(i, uplet.getValeur(i));
                }

                result.add(projectedUplet);
            }

            listColonneAfficher = new String[listColonne.size()];

            for (int i = 0; i < listColonne.size(); i++) {
                listColonneAfficher[i] = listColonne.get(i).getNomAttribut();
            }

        } else {
            int i = 0;

            for (Uplet u : nUplet) {
                Uplet projectedUplet = new Uplet(this);

                for (String colonne : nomColonne) {
                    projectedUplet.setValeur(i++, u.getValeur(colonne, this));
                }

                i = 0;

                result.add(projectedUplet);
            }

            listColonneAfficher = new String[nomColonne.length];

            for (int j = 0; j < nomColonne.length; j++) {
                listColonneAfficher[j] = nomColonne[j];
            }
        }

        return result;
    }

    public ArrayList<Uplet> selection(String nomColonne, String Operateur, String key) {
        ArrayList<Uplet> result = new ArrayList<>();
        listColonneAfficher = new String[listColonne.size()];

        for (int i = 0; i < listColonne.size(); i++) {
            listColonneAfficher[i] = listColonne.get(i).getNomAttribut();
        }

        for (Uplet u : nUplet) {
            if (applyOperator(u.getValeur(nomColonne, this), Operateur, key)) {
                result.add(u);
            }
        }

        return result;
    }

    public ArrayList<Uplet> Union(Relation r2) {
        ArrayList<Uplet> result = new ArrayList<>();

        if (!this.listColonne.equals(r2.getListColonne())) {
            throw new IllegalArgumentException("Les relations n'ont pas les mêmes colonnes");
        }

        for (Uplet uplet : this.nUplet) {
            result.add(uplet);
        }

        for (Uplet uplet : r2.getNuplets()) {
            if (!result.contains(uplet)) {
                result.add(uplet);
            }
        }

        return result;
    }

    public ArrayList<Uplet> intersection(Relation r2) {
        ArrayList<Uplet> result = new ArrayList<>();

        if (!this.listColonne.equals(r2.getListColonne())) {
            throw new IllegalArgumentException("Les relations n'ont pas les mêmes colonnes");
        }

        for (Uplet uplet1 : this.nUplet) {
            for (Uplet uplet2 : r2.getNuplets()) {
                if (uplet1.equals(uplet2)) {
                    result.add(uplet1);
                    break;
                }
            }
        }

        return result;
    }

    public ArrayList<Uplet> difference(Relation r2) {
        ArrayList<Uplet> result = new ArrayList<>();

        if (!this.listColonne.equals(r2.getListColonne())) {
            throw new IllegalArgumentException("Les relations n'ont pas les mêmes colonnes");
        }

        for (Uplet uplet1 : this.nUplet) {
            boolean found = false;
            
            for (Uplet uplet2 : r2.getNuplets()) {
                if (uplet1.equals(uplet2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result.add(uplet1);
            }
        }

        return result;
    }

    public Relation produitCartesien(Relation r2) {
        // Initialiser les colonnes de la nouvelle relation
        Relation resultRelation = new Relation("resultcartesian", this.listColonne.toArray(new Attribut[0]));
        resultRelation.appendColumns(r2.getListColonne().toArray(new Attribut[0]));

        for (Uplet uplet1 : this.nUplet) {
            for (Uplet uplet2 : r2.getNuplets()) {
                Uplet combinedUplet = new Uplet(resultRelation);
                for (int i = 0; i < uplet1.getLigne().size(); i++) {
                    combinedUplet.setValeur(i, uplet1.getValeur(i));
                }
                for (int i = 0; i < uplet2.getLigne().size(); i++) {
                    combinedUplet.setValeur(i + uplet1.getLigne().size(), uplet2.getValeur(i));
                }
                resultRelation.insert(combinedUplet);
            }
        }

        return resultRelation;
    }

    // Le nom de la colonne a appliquer la jointure, l'operateur, la deuxieme relation et le nom de la colonne
    public ArrayList<Uplet> jointure(String nomColonneUn, String Operateur, Relation r2, String nomColonneDeux) {
        Relation pCartesien = this.produitCartesien(r2);
        ArrayList<Uplet> result = new ArrayList<>();

        int indexColonneUn = pCartesien.getIndexOfColumn(nomColonneUn);
        int indexColonneDeux = pCartesien.getIndexOfColumn(nomColonneDeux) + this.listColonne.size();

        for (Uplet uplet : pCartesien.getNuplets()) {
            Object valeurUn = uplet.getValeur(indexColonneUn);
            Object valeurDeux = uplet.getValeur(indexColonneDeux);

            if (applyOperator(valeurUn, Operateur, valeurDeux.toString())) {
                result.add(uplet);
            }
        }

        listColonneAfficher = new String[pCartesien.getListColonne().size()];
        int index = 0;

        for (Attribut attribut : pCartesien.getListColonne()) {
            listColonneAfficher[index] = attribut.getNomAttribut();
            index++;
        }

        index = 0;

        return result;
    }

    public int getIndexOfColumn(String nomColonne) {
        for (int i = 0; i < listColonne.size(); i++) {
            if (listColonne.get(i).getNomAttribut().equalsIgnoreCase(nomColonne)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Colonne non trouvée : " + nomColonne);
    }

    private boolean applyOperator(Object value, String Operateur, String key) {

        switch (Operateur) {
            case "=":
                return value.toString().equals(key);
            case "!=":
                return !value.toString().equals(key);
            case "<":
                int i = Integer.parseInt(key);
                return (int) value < i;
            case ">":
                int n = Integer.parseInt(key);
                return (int) value > n;
            case "<=":
                int t = Integer.parseInt(key);
                int k = (int) value;
                return k <= t;
            case ">=":
                int K = Integer.parseInt(key);
                return (int) value >= K;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + Operateur);
        }
    }

    public void display(ArrayList<Uplet> nUplet) {

        if (listColonneAfficher.length == 0) {
            for (Attribut colonne : listColonne) {
                String nomColonne = colonne.getNomAttribut();
                System.out.printf("| %-12s ", nomColonne);
            }
            System.out.print("|");
        } else {
            for (int i = 0; i < listColonneAfficher.length; i++) {
                System.out.printf("| %-12s ", listColonneAfficher[i]);
            }

            System.out.print("|");
        }

        System.out.println("");

        for (Uplet u : nUplet) {

            for (int i = 0; i < u.getLigne().size(); i++) {
                if (u.getValeur(i) == null) {
                    continue;
                } else {
                    System.out.printf("| %-12s ", u.getValeur(i));
                }
            }

            System.out.println("|");
        }

        System.out.println("");
    }

}

// public ArrayList<Uplet> projetion(String NomColonne, String key) {

// // }
// }

// Nom anle colonne ho comparena, ny comparateur de le hicomparena azy

// return result;
// }

// private int compare(Object value, String key) {
// if (value instanceof Comparable) {
// return ((Comparable) value).compareTo(key);
// } else {
// throw new IllegalArgumentException("Value is not comparable: " + value);
// }
// }

// System.out.printf("| %-19s | %-10s | %-10s | %-10s |%n", "Empno", "Ename",
// "Montant", "Date");

// for (Object[] empData : employeeData) {
// System.out.printf("| %-19d | %-10s | %-10.1f | %-10s |%n",
// empData[0], empData[1], empData[2], empData[3]);

// System.out.printf("| %-19d | %-10s | %-10.1f | %-10s |%n",
// empData[0], empData[1], empData[4], empData[3]);

// System.out.printf("| %-19d | %-10s | %-10.1f | %-10s |%n",
// empData[0], empData[1], (double) empData[2] * 0.01, empData[3]);

// System.err.println("");
// }