import java.util.LinkedList;

public class Distributeur {

    /*
    Distribue deux cartes de manière aleatoire a tous les joueurs dans la LL joueurs
    Affecte la LL cartesInitiales de chaque joueur
    @param - Paquet p - le paquet utilise dans le jeu
    @param - LinkedList<Joueur> joueurs - LL avec les joueurs a distribuer les carte initiales
     */
    public static void distribuirCartesJogadores(Paquet p, LinkedList<Joueur> joueurs) {
        for (Joueur j : joueurs) {
            LinkedList<Carte> hand = new LinkedList<>();
            int i = (int) ((p.paquet.size()) * Math.random());
            hand.add(p.paquet.get(i));
            p.paquet.remove(i);
            i = (int) ((p.paquet.size()) * Math.random());
            hand.add(p.paquet.get(i));
            p.paquet.remove(i);
            j.setCartesSurMain(hand);
        }
    }
    /*
    Distribue cinq cartes de manière aleatoire a la table
    Affecte la LL table de la table
    @param - Paquet p - le paquet utilise dans le jeu
    @param - LinkedList<Carte> mesa - LL de cartes de la table
     */
    public static void distribuirCartesMesa(Paquet p, LinkedList<Carte> mesa){
        for(int i=0;i<5;i++){
            int m = (int) ((p.paquet.size()) * Math.random());
            mesa.add(p.paquet.get(m));
            p.paquet.remove(m);
        }
    }
    /*
    Distribue une quantite q d'argent a tous les joueurs dans la ll fournie
    @param LinkedList<Joueur> joueurs - ll des jouers a distribuer
    @param int q - quantite d'argent a distribuer a chaque jouer
     */
    public static void distributeurDArgentDebut(LinkedList<Joueur> joueurs, int q){
        for(Joueur j:joueurs){
            j.setArgent(q);
        }
    }
}
