import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){

        Jeu j = new Jeu(9,0);

        j.getJoueurs().display();

        Joueur test = j.getTailJoueur();

        System.out.println("Enlevé: "+test.nom);

        j.sortirJoueur(test);

        j.getJoueurs().display();





    }

}


