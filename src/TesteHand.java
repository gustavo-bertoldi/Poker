import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        LinkedList<Carte> main = new LinkedList<Carte>();
        main.add(new Carte(10,'d'));
        main.add(new Carte(11,'d'));
        LinkedList<Carte> table = new LinkedList<Carte>();
        table.add(new Carte(12,'d'));
        table.add(new Carte(13,'d'));
        table.add(new Carte(14,'d'));
        table.add(new Carte(2,'t'));
        table.add(new Carte(5,'c'));


        Hand h = new Hand(main,table);
        h.calculerValeurHand();
        System.out.println(h.getDescription());
        System.out.println(h.getValeurHand());


    }

}


