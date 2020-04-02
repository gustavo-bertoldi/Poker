import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){

        Hand h1= new Hand();

        LinkedList<Carte> surTable=new LinkedList<>();
        surTable.add(new Carte(12,'c'));
        surTable.add(new Carte(13,'c'));
        surTable.add(new Carte(14,'c'));
        surTable.add(new Carte(11,'c'));
        surTable.add(new Carte(6,'c'));

        LinkedList<Carte> surMain = new LinkedList<>();
        surMain.add(new Carte(10,'c'));
        surMain.add(new Carte(6,'c'));

        h1.setHand(surMain,surTable);
        System.out.println(h1.getDescription());

        





    }

}


