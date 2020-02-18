import java.util.LinkedList;

public class Baralho {

    protected LinkedList<Carta> baralho;

    public Baralho() {
        baralho = new LinkedList<Carta>();
        for (int i = 2; i <= 14; i++) {
            baralho.add(new Carta(i, 'p'));
            baralho.add(new Carta(i, 'd'));
            baralho.add(new Carta(i, 'c'));
            baralho.add(new Carta(i, 'e'));
        }
    }


    public String toString() {
        String s = "";
        for (Carta c : baralho) {
            s = s + c.toString() + "\n";
        }
        return s;
    }

    public static void main(String[] args) {
        Baralho b = new Baralho();
        System.out.println(b.toString());
    }
}
