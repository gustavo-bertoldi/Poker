import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        CircularLinkedList<Joueur> joueurs = new CircularLinkedList<>();
        Joueur baltazar = new Joueur("Baltazar",0);
        Joueur jorge = new Joueur("jorge",0);
        Joueur carlos = new Joueur("carlos",0);
        Joueur damiao = new Joueur("damiao",0);
        Joueur gabriel = new Joueur("gabriel",0);
        Joueur alisson = new Joueur("alisson", 0);
        baltazar.playing=true;

        joueurs.addNode(baltazar);
        joueurs.addNode(jorge);
        joueurs.addNode(carlos);
        joueurs.addNode(damiao);
        joueurs.addNode(gabriel);
        joueurs.addNode(alisson);

        joueurs.display();

        joueurs.remove(joueurs.getDernierAParier().joueur);
        joueurs.display();


        





    }

}


