import java.util.*;

// public class App {

//     public static void main(String[] args) throws Exception {

//         Domaine varchar = new Domaine("Varchar", 50);
//         Domaine number = new Domaine("Number", 50);

//         Attribut IdCours = new Attribut("IdCours", varchar);
//         Attribut jours = new Attribut("jours", varchar);
//         Attribut heurres = new Attribut("heurres", number);

//         // Premiere relation
//         Relation CJH = new Relation("CJH", IdCours, jours, heurres);
//         Uplet L1CJH = new Uplet(CJH);
//         L1CJH.setValeur(0, "Archi");
//         L1CJH.setValeur(1, "Lu");
//         L1CJH.setValeur(2, 9);

//         Uplet L2CJH = new Uplet(CJH);
//         L2CJH.setValeur(0, "Algo");
//         L2CJH.setValeur(1, "Ma");
//         L2CJH.setValeur(2, 9);

//         Uplet L3CJH = new Uplet(CJH);
//         L3CJH.setValeur(0, "Algo");
//         L3CJH.setValeur(1, "Ve");
//         L3CJH.setValeur(2, 9);

//         Uplet L4CJH = new Uplet(CJH);
//         L4CJH.setValeur(0, "Syst");
//         L4CJH.setValeur(1, "Ma");
//         L4CJH.setValeur(2, 14);

//         CJH.insert(L1CJH, L2CJH, L3CJH, L4CJH);

//         Attribut IdSalle = new Attribut("IdSalle", varchar);

//         // Deuxieme relation
//         Relation CS = new Relation("CS", IdCours, IdSalle);

//         Uplet L1CS = new Uplet(CS);
//         L1CS.setValeur(0, "Archi");
//         L1CS.setValeur(1, "S1");

//         Uplet L2CS = new Uplet(CS);
//         L2CS.setValeur(0, "Algo");
//         L2CS.setValeur(1, "S2");

//         Uplet L3CS = new Uplet(CS);
//         L3CS.setValeur(0, "Syst");
//         L3CS.setValeur(1, "S1");

//         CS.insert(L1CS, L2CS, L3CS);

//         Attribut IdEtudiants = new Attribut("IdEtudiants", number);
//         Attribut Nom = new Attribut("Nom", varchar);
//         Attribut Adresse = new Attribut("IdSalle", varchar);

//         // Troisieme relation
//         Relation ENA = new Relation("ENA", IdEtudiants, Nom, Adresse);

//         Uplet L1ENA = new Uplet(ENA);
//         L1ENA.setValeur(0, 100);
//         L1ENA.setValeur(1, "Toto");
//         L1ENA.setValeur(2, "Nice");

//         Uplet L2ENA = new Uplet(ENA);
//         L2ENA.setValeur(0, 200);
//         L2ENA.setValeur(1, "Tata");
//         L2ENA.setValeur(2, "Paris");

//         Uplet L3ENA = new Uplet(ENA);
//         L3ENA.setValeur(0, 300);
//         L3ENA.setValeur(1, "Tita");
//         L3ENA.setValeur(2, "Rome");

//         ENA.insert(L1ENA, L2ENA, L3ENA);

//         Attribut Note = new Attribut("IdSalle", varchar);
//         // Quatrième relation
//         Relation CEN = new Relation("CEN", IdCours, IdEtudiants, Note);

//         Uplet L1CEN = new Uplet(CEN);
//         L1CEN.setValeur(0, "Archi");
//         L1CEN.setValeur(1, 100);
//         L1CEN.setValeur(2, "A");

//         Uplet L2CEN = new Uplet(CEN);
//         L2CEN.setValeur(0, "Archi");
//         L2CEN.setValeur(1, 300);
//         L2CEN.setValeur(2, "A");

//         Uplet L3CEN = new Uplet(CEN);
//         L3CEN.setValeur(0, "Syst");
//         L3CEN.setValeur(1, 100);
//         L3CEN.setValeur(2, "B");

//         Uplet L4CEN = new Uplet(CEN);
//         L4CEN.setValeur(0, "Syst");
//         L4CEN.setValeur(1, 200);
//         L4CEN.setValeur(2, "A");

//         Uplet L5CEN = new Uplet(CEN);
//         L5CEN.setValeur(0, "Syst");
//         L5CEN.setValeur(1, 300);
//         L5CEN.setValeur(2, "B");

//         Uplet L6CEN = new Uplet(CEN);
//         L6CEN.setValeur(0, "Algo");
//         L6CEN.setValeur(1, 100);
//         L6CEN.setValeur(2, "C");

//         Uplet L7CEN = new Uplet(CEN);
//         L7CEN.setValeur(0, "Algo");
//         L7CEN.setValeur(1, 200);
//         L7CEN.setValeur(2, "A");

//         CEN.insert(L1CEN, L2CEN, L3CEN, L4CEN, L5CEN, L6CEN, L7CEN);

//         // ArrayList<Uplet> jointure = CJH.jointure("idCours", "=", CS, "idCours");
//         // CJH.display(jointure);

//         // ArrayList<Uplet> selectResult = CS.selection("(idCours = 1) and (semester =
//         // 'S2')");
//         // CS.display(selectResult);

//         // ArrayList<Uplet> projectResult = CS.projection("IdSalle");
//         // CS.display(projectResult);

//         // CEN.display(CEN.getNuplets());
//         // ArrayList<Uplet> cenSelection = CEN.selection("IdEtudiants", "=", "300");
//         // CEN.display(cenSelection);

//         // CJH.display(CJH.getNuplets());

//         // CS.display(CS.getNuplets());

//         // CEN.display(CEN.getNuplets());

//         // ENA.display(ENA.getNuplets());

//         //////////////////////////

//         // Créer des attributs
//         Attribut a1 = new Attribut("ID", varchar);
//         Attribut a2 = new Attribut("Nom", varchar);
//         Attribut a3 = new Attribut("Age", varchar);

//         // Créer une relation
//         Relation relation1 = new Relation("Personnes", a1, a2, a3);

//         // Insérer des données (uplets)
//         Uplet uplet1 = new Uplet(relation1);
//         uplet1.setValeur(0, "1");
//         uplet1.setValeur(1, "2");
//         uplet1.setValeur(2, "3");

//         Uplet uplet2 = new Uplet(relation1);
//         uplet2.setValeur(0, "4");
//         uplet2.setValeur(1, "5");
//         uplet2.setValeur(2, "6");

//         relation1.insert(uplet1, uplet2);

//         Relation relation2 = new Relation("Personnes", a1, a2, a3);
//         Uplet uplet3 = new Uplet(relation2);
//         uplet3.setValeur(0, "7");
//         uplet3.setValeur(1, "8");
//         uplet3.setValeur(2, "9");
//         relation2.insert(uplet3);

//         Uplet uplet4 = new Uplet(relation2);
//         uplet4.setValeur(0, "4");
//         uplet4.setValeur(1, "5");
//         uplet4.setValeur(2, "6");
//         relation2.insert(uplet4);

//         System.out.println("Union des deux relations :");
//         ArrayList<Uplet> unionResult = relation1.intersection(relation2);

//         ArrayList<Uplet> differenceResult = relation1.intersection(relation2);

//         ArrayList<Uplet> intersectionResult = relation1.intersection(relation2);

//         relation1.display(unionResult);
//     }
// }

public class App {
    public static void main(String[] args) {
        RepartTache repartTache = new RepartTache();

        Dishe task1 = new Dishe("C", "B", "B");
        Dishe task2 = new Dishe("C", "W", "B");
        Dishe task3 = new Dishe("S", "W", "B");
        Dishe task4 = new Dishe("S", "B", "B");
        Dishe task5 = new Dishe("S", "B", "B");
        Dishe task6 = new Dishe("C", "U", "B");
        Dishe task7 = new Dishe("C", "W", "B");
        Dishe task8 = new Dishe("C", "O", "B");
        Dishe task9 = new Dishe("S", "W", "B");
        Dishe task10 = new Dishe("S", "P", "B");

        repartTache.addDishe(task1);
        repartTache.addDishe(task2);
        repartTache.addDishe(task3);
        repartTache.addDishe(task4);
        repartTache.addDishe(task5);
        repartTache.addDishe(task6);
        repartTache.addDishe(task7);
        repartTache.addDishe(task8);
        repartTache.addDishe(task9);
        repartTache.addDishe(task10);

        repartTache.getNombreTache();
    }
}

// ArrayList<Uplet> cenSelection = CEN.selection("IdEtudiants", "=", "300")
// ArrayList<Uplet> cenSelection = CEN.selection("(IdEtudiants = 300) and
// (semester = 'S1')");

// Domaine domaine1 = new Domaine("12 true rasoa", 50); = Ny valeur an'ny
// colonne dia tsy maintsy iray amin'ny "12 true rasoa" (number, bool, string)

// Raha manao union entre domaine dia oh domaine colone 1 :"12 true rasoa" et
// domaine col 2 table hafa "3 false rabe " dia manao union entre izy roa ny
// domaine col vaovao