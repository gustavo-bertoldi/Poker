import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){

        Jeu j = new Jeu (6,0);

        System.out.println(j.joueursGagnants().getFirst().nom);


        System.out.println("Valeurs hand:");
        System.out.println(j.getJoueurs().get(0).nom+" : "+j.getJoueurs().get(0).getHand().getValeurHand());
        System.out.println(j.getJoueurs().get(1).nom+" : "+j.getJoueurs().get(1).getHand().getValeurHand());
        System.out.println(j.getJoueurs().get(2).nom+" : "+j.getJoueurs().get(2).getHand().getValeurHand());
        System.out.println(j.getJoueurs().get(3).nom+" : "+j.getJoueurs().get(3).getHand().getValeurHand());
        System.out.println(j.getJoueurs().get(4).nom+" : "+j.getJoueurs().get(4).getHand().getValeurHand());
        System.out.println(j.getJoueurs().get(5).nom+" : "+j.getJoueurs().get(5).getHand().getValeurHand());


    }

}


