import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        LinkedList<Carte> main = new LinkedList<Carte>();
        main.add(new Carte(13,'t'));
        main.add(new Carte(11,'t'));
        LinkedList<Carte> table = new LinkedList<Carte>();
        table.add(new Carte(8,'d'));
        table.add(new Carte(9,'t'));
        table.add(new Carte(10,'t'));
        table.add(new Carte(12,'t'));
        table.add(new Carte(12,'t'));

        LinkedList<Carte> hand = new LinkedList<>();
        hand.addAll(main);
        hand.addAll(table);



        Hand h = new Hand();
        h.setHand(hand);
        h.calculerValeurHand();
        System.out.println(h.getDescription());
        System.out.println(h.getValeurHand());



    }

}


