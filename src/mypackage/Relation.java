package mypackage;

import java.util.*;

public class Relation {
    private String name;
    private ArrayList<Attribut> listColonne;
    private ArrayList<Uplet> nUplet;

    public String getName() {
        return name;
    }

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

    public ArrayList<Uplet> getNuplets() {
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
                    // System.out.println("And cond : " + andCond);

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

    private int getIndexOfColumn(String nomColonne) {
        
        for (int i = 0; i < this.listColonne.size(); i++) {
            if (listColonne.get(i).getNomAttribut().equalsIgnoreCase(nomColonne)) {
                // System.out.println(listColonne.get(i).getNomAttribut() + " = " + nomColonne); 
                
                return i;
            }
        }

        // System.out.println("");

        throw new IllegalArgumentException("Colonne non trouvée : " + nomColonne);
    }

    public Relation union(Relation r2) {
        if (this.listColonne.size() != r2.getListColonne().size()) {
            throw new IllegalArgumentException("Les relations n'ont pas les mêmes colonnes.");
        }

        ArrayList<Attribut> newColumns = new ArrayList<>();

        // Domaine vaovao ho any union
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
            boolean alreadyExists = false;

            for (Uplet existingUplet : result.getNuplets()) {
                if (existingUplet.equals(uplet2)) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                Uplet newUplet = new Uplet(result);

                for (int i = 0; i < uplet2.getLigne().size(); i++) {
                    newUplet.setValeur(i, uplet2.getValeur(i));
                }

                result.insert(newUplet);
            }
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

    public Relation jointureRelation(Relation relation2, String colonne1, String operateur, String colonne2) {
        Relation produitCartesien = this.produitCartesien(relation2);

        int indexColonne1 = this.getIndexOfColumn(colonne1);
        int indexColonne2 = relation2.getIndexOfColumn(colonne2) + this.getNuplets().size();

        Relation jointureRelationResultat = new Relation(this.name + "_join_" + relation2.name,
                produitCartesien.getListColonne().toArray(new Attribut[0]));

        for (Uplet uplet : produitCartesien.getNuplets()) {
            Object valeur1 = uplet.getValeur(indexColonne1);
            Object valeur2 = uplet.getValeur(indexColonne2);

            // System.out.println(valeur1 + " " + valeur2);
            if (applyConditionDirect(valeur1, operateur, valeur2)) {
                jointureRelationResultat.insert(uplet);
            }
        }

        return jointureRelationResultat;
    }

    public Relation jointureCondition(Relation relation2, String condition) {
        Relation produitCartesien = produitCartesien(relation2);

        String[] orConditions = condition.split(" or ");
        ArrayList<Uplet> selectedUplets = new ArrayList<>();

        for (Uplet uplet : produitCartesien.getNuplets()) {
            boolean orMatch = false;

            for (String orCond : orConditions) {
                String[] andConditions = orCond.trim().split(" and ");
                boolean andMatch = true;

                for (String andCond : andConditions) {
                    andCond = andCond.trim().replaceAll("[()]", "");
                    String[] parts = andCond.split(" ");

                    String PartieGauche = parts[0];
                    String operateur = parts[1];
                    String Partiedroite = parts[2].replaceAll("'", "");

                    String PartieDroitevaleur[] = Partiedroite.split("\\.");
                    String TestCondition = " ";

                    // Jerena raha misy '.' ao anatinylay Valeur na tsia
                    if (PartieDroitevaleur.length == 1) {
                        TestCondition = Partiedroite;
                    } else {
                        TestCondition = PartieDroitevaleur[1];
                    }

                    // Conditions colonne-valeur
                    if (!isColumn(TestCondition)) {
                        String[] partieOneSplited = PartieGauche.split("\\.");

                        if (partieOneSplited[0].equals("this")) {
                            if (!applyCondition(uplet, partieOneSplited[1], operateur, Partiedroite)) {
                                andMatch = false;
                                break;
                            }
                        } else {
                            if (!relation2.applyConditionToAnotherRelation(uplet, partieOneSplited[1], operateur, Partiedroite)) {
                                andMatch = false;
                                break;
                            } else {
                                // System.out.println("hita");
                            }
                        }
                    } else { // Colonne-colonne
                        String[] gaucheSplited = PartieGauche.split("\\.");
                        String[] droiteSplited = Partiedroite.split("\\.");

                        int indexColonne1 = gaucheSplited[0].equals("this")
                                ? this.getIndexOfColumn(gaucheSplited[1])
                                : relation2.getIndexOfColumn(gaucheSplited[1]) + this.getNuplets().size();

                        int indexColonne2 = droiteSplited[0].equals("this")
                                ? this.getIndexOfColumn(droiteSplited[1])
                                : relation2.getIndexOfColumn(droiteSplited[1]) + this.getNuplets().size();

                        Object valeur1 = uplet.getValeur(indexColonne1);
                        Object valeur2 = uplet.getValeur(indexColonne2);

                        if (!applyConditionDirect(valeur1, operateur, valeur2)) {
                            andMatch = false;
                            break;
                        }
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

        // Création de la nouvelle relation résultante
        Relation selectedRelation = new Relation(this.name, produitCartesien.getListColonne().toArray(new Attribut[0]));
        selectedRelation.setNupplet(selectedUplets);

        return selectedRelation;
    }

    public Relation jointureExterneRelation(Relation relation2, String colonne1, String operateur, String colonne2) {
        Relation gaucheRelation = this.jointureExterneGaucheRelation(relation2, colonne1, operateur, colonne2);

        Relation droiteRelation = this.jointureExterneDroiteRelation(relation2, colonne1, operateur, colonne2);

        Relation jointureRelationResultat = gaucheRelation.union(droiteRelation);

        return jointureRelationResultat;
    }

    public Relation jointureExterneGaucheRelation(Relation relation2, String colonne1, String operateur,
            String colonne2) {
        Relation produitCartesien = this.produitCartesien(relation2);

        int indexColonne1 = this.getIndexOfColumn(colonne1);
        int indexColonne2 = relation2.getIndexOfColumn(colonne2) + this.getNuplets().size();

        Relation jointureGaucheRelation = new Relation(this.name + "_join_" + relation2.name,
                produitCartesien.getListColonne().toArray(new Attribut[0]));

        // Left Outer Join Logic (pour la première relation)
        for (Uplet uplet : this.getNuplets()) {
            boolean isMatchFound = false;

            for (Uplet uplet2 : relation2.getNuplets()) {
                Object valeur1 = uplet.getValeur(indexColonne1);
                Object valeur2 = uplet2.getValeur(indexColonne2 - uplet.getLigne().size());

                if (applyConditionDirect(valeur1, operateur, valeur2)) {
                    jointureGaucheRelation.insert(combinerUplets(uplet, uplet2, -1, jointureGaucheRelation));
                    isMatchFound = true;
                }
            }

            // Si aucun match n'est trouvé, ajouter un uplet avec des nulls pour relation2
            if (!isMatchFound) {
                jointureGaucheRelation.insert(combinerUplets(uplet, null, -1, jointureGaucheRelation));
            }
        }

        return jointureGaucheRelation;
    }

    public Relation jointureExterneDroiteRelation(Relation relation2, String colonne1, String operateur,
            String colonne2) {
        Relation produitCartesien = this.produitCartesien(relation2);

        int indexColonne1 = this.getIndexOfColumn(colonne1);
        int indexColonne2 = relation2.getIndexOfColumn(colonne2) + this.getNuplets().size();

        Relation jointureDroiteRelationResultat = new Relation(this.name + "_join_" + relation2.name,
                produitCartesien.getListColonne().toArray(new Attribut[0]));

        // Right Outer Join Logic (pour la deuxième relation)
        for (Uplet uplet2 : relation2.getNuplets()) {
            boolean isMatchFound = false;
            for (Uplet uplet : this.getNuplets()) {
                Object valeur1 = uplet.getValeur(indexColonne1);
                Object valeur2 = uplet2.getValeur(indexColonne2 - uplet.getLigne().size());

                if (applyConditionDirect(valeur1, operateur, valeur2)) {
                    jointureDroiteRelationResultat
                            .insert(combinerUplets(uplet, uplet2, -1, jointureDroiteRelationResultat));
                    isMatchFound = true;
                }
            }

            // Si aucun match n'est trouvé, ajouter un uplet avec des nulls pour relation1
            if (!isMatchFound) {
                jointureDroiteRelationResultat.insert(combinerUplets(null, uplet2, -1, jointureDroiteRelationResultat));
            }
        }

        return jointureDroiteRelationResultat;
    }

    public Relation Division(Relation r2) {
        List<String> colonnesR1SansR2 = new ArrayList<>();
        for(Attribut attributR1 : this.listColonne) {
            boolean foundInR2 = false;

            for (Attribut attributR2 : r2.getListColonne()) {
                if (attributR1.getNomAttribut().equalsIgnoreCase(attributR2.getNomAttribut())) {
                    foundInR2 = true;
                    break;
                }

                if(!foundInR2) {
                    colonnesR1SansR2.add(attributR1.getNomAttribut());
                }
            }
        }

        Relation T1 = this.projection(colonnesR1SansR2.toArray(new String[0]));
        
        Relation T2 = T1.produitCartesien(r2);

        Relation T3 = T2.difference(this);

        Relation R3 = T1.difference(T3.projection(colonnesR1SansR2.toArray(new String[0])));

        return R3;
    }

    private boolean isColumn(String str) {
        for (Attribut attribut : this.getListColonne()) {
            if (str.equalsIgnoreCase(attribut.getNomAttribut())) {
                return true;
            }
        }
        return false;
    }

    private Uplet combinerUplets(Uplet uplet1, Uplet uplet2, int colonneIgnoree, Relation relation) {
        Uplet nouveauUplet = new Uplet(relation);
        int index = 0;

        if (uplet1 != null) {
            for (Object val : uplet1.getLigne()) {
                nouveauUplet.setValeur(index++, val);
            }
        } else {
            for (int i = 0; i < relation.getListColonne().size() / 2; i++) {
                nouveauUplet.setValeur(index++, null);
            }
        }

        if (uplet2 != null) {
            for (Object val : uplet2.getLigne()) {
                if (index != colonneIgnoree) {
                    nouveauUplet.setValeur(index++, val);
                }
            }
        } else {
            for (int i = 0; i < relation.getListColonne().size() / 2; i++) {
                nouveauUplet.setValeur(index++, null);
            }
        }

        return nouveauUplet;
    }

    private boolean applyConditionToAnotherRelation(Uplet uplet, String nomColonne, String operateur, String valeur) {
        // System.out.println("apply to another");

        // for (int i = 0; i < uplet.getLigne().size(); i++) {
        //     System.out.println(uplet.getValeur(i));    
        // }

        int index = getIndexOfColumn(nomColonne);
        Object upletValue = uplet.getValeur(index + this.listColonne.size());

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

        System.out.println();
    }
}
