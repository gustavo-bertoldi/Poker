import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        CircularLinkedList<Joueur> joueurs = new CircularLinkedList<>();
        Joueur baltazar = new Joueur("Baltazar",0);
        Joueur jorge = new Joueur("jorge",0);
        Joueur carlos = new Joueur("carlos",0);
        Joueur damiao = new Joueur("damiao",0);

        joueurs.addNode(baltazar);
        joueurs.addNode(jorge);
        joueurs.addNode(carlos);
        joueurs.addNode(damiao);

        joueurs.display();

        joueurs.remove(carlos);
        joueurs.display();

        joueurs.remove(baltazar);
        joueurs.remove(damiao);
        joueurs.display();

        joueurs.remove(jorge);
        joueurs.display();

        





    }

}


