import java.util.LinkedList;

public class Paquet {

    protected LinkedList<Carte> paquet;

    public Paquet() {
        paquet = new LinkedList<>();
        for (int i = 2; i <= 14; i++) {
            paquet.add(new Carte(i, 't'));
            paquet.add(new Carte(i, 'c'));
            paquet.add(new Carte(i, 'p'));
            paquet.add(new Carte(i, 'd'));
        }
    }


    public String toString() {
        String s = "";
        for (Carte c : paquet) {
            s = s + c.toString() + "\n";
        }
        return s;
    }

    public static void main(String[] args) {
        Paquet b = new Paquet();
        System.out.println(b.toString());
    }

    public int size(){
        return paquet.size();
    }

    public Carte get(int i){
        return paquet.get(i);
    }



    public void remove(Carte c){
        paquet.remove(c);
    }
}
