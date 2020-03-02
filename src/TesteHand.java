import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        LinkedList<Carta> cartas = new LinkedList<Carta>();
        cartas.add(new Carta(14,'o'));
        cartas.add(new Carta(4,'o'));
        cartas.add(new Carta(11,'o'));
        cartas.add(new Carta(3,'o'));
        cartas.add(new Carta(9,'o'));
        cartas.add(new Carta(5,'o'));
        cartas.add(new Carta(6,'o'));

        Hand h = new Hand(cartas);


        System.out.println();
    }
}


