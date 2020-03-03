import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        LinkedList<Carte> Cartes = new LinkedList<Carte>();
        Cartes.add(new Carte(11,'d'));
        Cartes.add(new Carte(4,'d'));
        Cartes.add(new Carte(14,'d'));
        Cartes.add(new Carte(11,'d'));
        Cartes.add(new Carte(6,'d'));
        Cartes.add(new Carte(11,'d'));
        Cartes.add(new Carte(11,'d'));

        Hand h = new Hand(Cartes);



                System.out.println(h.highCard().toString());


    }
}


