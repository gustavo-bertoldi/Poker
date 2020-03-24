import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        LinkedList<Carte> surTable = new LinkedList<>();
        LinkedList<Carte> surMain = new LinkedList<>();
        surMain.add(new Carte(14,'p'));
        surMain.add(new Carte(2,'c'));
        surTable.add(new Carte(4,'d'));
        surTable.add(new Carte(5,'d'));
        surTable.add(new Carte(6,'d'));
        surTable.add(new Carte(7,'t'));
        surTable.add(new Carte(8,'d'));

        Hand h = new Hand();
        h.setHand(surMain,surTable);
        h.calculerValeurHand();
        System.out.println(h.getDescription());


    }

}


