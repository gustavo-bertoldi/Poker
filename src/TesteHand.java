import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        LinkedList<Carta> cartas = new LinkedList<Carta>();
        cartas.add(new Carta(14,'o'));
        cartas.add(new Carta(14,'o'));
        cartas.add(new Carta(2,'o'));
        cartas.add(new Carta(3,'o'));
        cartas.add(new Carta(4,'o'));
        cartas.add(new Carta(5,'o'));
        cartas.add(new Carta(6,'o'));


        Hand teste=new Hand(cartas);

        System.out.println(teste.toString(teste.straight()));
    }
}
