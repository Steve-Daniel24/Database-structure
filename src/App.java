import java.util.*;

public class App {

    public static void main(String[] args) throws Exception {

        Domaine varchar = new Domaine("Varchar", 50);
        Domaine number = new Domaine("Number", 50);

        Attribut IdCours = new Attribut("IdCours", varchar);
        Attribut jours = new Attribut("jours", varchar);
        Attribut heurres = new Attribut("heurres", number);

        // Premiere relation
        Relation CJH = new Relation("CJH", IdCours, jours, heurres);
        Uplet L1CJH = new Uplet(CJH);
        L1CJH.setValeur(0, "Archi");
        L1CJH.setValeur(1, "Lu");
        L1CJH.setValeur(2, 9);

        Uplet L2CJH = new Uplet(CJH);
        L2CJH.setValeur(0, "Algo");
        L2CJH.setValeur(1, "Ma");
        L2CJH.setValeur(2, 9);

        Uplet L3CJH = new Uplet(CJH);
        L3CJH.setValeur(0, "Algo");
        L3CJH.setValeur(1, "Ve");
        L3CJH.setValeur(2, 9);

        Uplet L4CJH = new Uplet(CJH);
        L4CJH.setValeur(0, "Syst");
        L4CJH.setValeur(1, "Ma");
        L4CJH.setValeur(2, 14);

        CJH.insert(L1CJH, L2CJH, L3CJH, L4CJH);

        Attribut IdSalle = new Attribut("IdSalle", varchar);

        // Deuxieme relation
        Relation CS = new Relation("CS", IdCours, IdSalle);

        Uplet L1CS = new Uplet(CS);
        L1CS.setValeur(0, "Archi");
        L1CS.setValeur(1, "S1");

        Uplet L2CS = new Uplet(CS);
        L2CS.setValeur(0, "Algo");
        L2CS.setValeur(1, "S2");

        Uplet L3CS = new Uplet(CS);
        L3CS.setValeur(0, "Syst");
        L3CS.setValeur(1, "S1");

        CS.insert(L1CS, L2CS, L3CS);

        Attribut IdEtudiants = new Attribut("IdEtudiants", number);
        Attribut Nom = new Attribut("Nom", varchar);
        Attribut Adresse = new Attribut("IdSalle", varchar);

        // Troisieme relation
        Relation ENA = new Relation("ENA", IdEtudiants, Nom, Adresse);

        Uplet L1ENA = new Uplet(ENA);
        L1ENA.setValeur(0, 100);
        L1ENA.setValeur(1, "Toto");
        L1ENA.setValeur(2, "Nice");

        Uplet L2ENA = new Uplet(ENA);
        L2ENA.setValeur(0, 200);
        L2ENA.setValeur(1, "Tata");
        L2ENA.setValeur(2, "Paris");

        Uplet L3ENA = new Uplet(ENA);
        L3ENA.setValeur(0, 300);
        L3ENA.setValeur(1, "Tita");
        L3ENA.setValeur(2, "Rome");

        ENA.insert(L1ENA, L2ENA, L3ENA);

        Attribut Note = new Attribut("IdSalle", varchar);
        // Quatrième relation
        Relation CEN = new Relation("CEN", IdCours, IdEtudiants , Note);

        Uplet L1CEN = new Uplet(CEN);
        L1CEN.setValeur(0, "Archi");
        L1CEN.setValeur(1, 100);
        L1CEN.setValeur(2, "A");

        Uplet L2CEN = new Uplet(CEN);
        L2CEN.setValeur(0, "Archi");
        L2CEN.setValeur(1, 300);
        L2CEN.setValeur(2, "A");

        Uplet L3CEN = new Uplet(CEN);
        L3CEN.setValeur(0, "Syst");
        L3CEN.setValeur(1, 100);
        L3CEN.setValeur(2, "B");

        Uplet L4CEN = new Uplet(CEN);
        L4CEN.setValeur(0, "Syst");
        L4CEN.setValeur(1, 200);
        L4CEN.setValeur(2, "A");

        Uplet L5CEN = new Uplet(CEN);
        L5CEN.setValeur(0, "Syst");
        L5CEN.setValeur(1, 300);
        L5CEN.setValeur(2, "B");

        Uplet L6CEN = new Uplet(CEN);
        L6CEN.setValeur(0, "Algo");
        L6CEN.setValeur(1, 100);
        L6CEN.setValeur(2, "C");

        Uplet L7CEN = new Uplet(CEN);
        L7CEN.setValeur(0, "Algo");
        L7CEN.setValeur(1, 200);
        L7CEN.setValeur(2, "A");

        CEN.insert(L1CEN, L2CEN, L3CEN, L4CEN, L5CEN, L6CEN, L7CEN);

        // Relation jointureResult = r1.jointure(r2);
        // ArrayList<Uplet> result = jointureResult.projection(jointureResult.getNuplets(), jointureResult, "X");
        // jointureResult.display(result);
        
        ArrayList<Uplet> selectResult = CS.selection("IdSalle", "=","S1");
        CS.display(selectResult);

        // ArrayList<Uplet> projectResult = CS.projection("IdSalle");
        // CS.display(projectResult);
        
        CEN.display(CEN.getNuplets());
        ArrayList<Uplet> cenSelection = CEN.selection("IdEtudiants", "=", "300");
        CEN.display(cenSelection);

        // CJH.display(CJH.getNuplets());

        // CS.display(CS.getNuplets());

        // CEN.display(CEN.getNuplets());

        // ENA.display(ENA.getNuplets());

        // CJH.display(jointureResult);

    }
}


//Le selection tsy mandeha : Tsy afaka micaste