import java.util.*;

public class RepartTache {
    private ArrayList<Dishe> listeTache = new ArrayList<>();

    public RepartTache( ArrayList<Dishe> listeTache) {
        this.listeTache = listeTache;
    }

    public void addDishe(Dishe dishe) {
        listeTache.add(dishe);
    }

    public void getNombreTache() {
        Map<String, Integer> tasksByColor = new HashMap<>();

        for (Dishe dishe : listeTache) {
            String color = dishe.getColor();
            tasksByColor.put(color, tasksByColor.getOrDefault(color, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : tasksByColor.entrySet()) {
            System.out.println("Couleur: " + entry.getKey() + " - Nombre de tâches: " + entry.getValue());
        }
    }

    
}

