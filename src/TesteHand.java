import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        Jeu j = new Jeu(9,0);

        Joueur j1 = j.getJoueurs().get(8);

        LinkedList<Carte> teste = new LinkedList<>();
        if(teste==null){
            System.out.println("boaa");
        }

    }

}


