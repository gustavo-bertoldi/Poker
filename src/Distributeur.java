import java.util.LinkedList;

public class Distributeur {

    public static void distribuirCartesJogadores(Paquet p, LinkedList<Joueur> jogadores) {
        for (Joueur j : jogadores) {
            LinkedList<Carte> hand = new LinkedList<>();
            int i = (int) (p.paquet.size() * Math.random());
            hand.add(p.paquet.get(i));
            p.paquet.remove(i);
            i = (int) (p.paquet.size() * Math.random());
            hand.add(p.paquet.get(i));
            p.paquet.remove(i);
            j.setHand(hand);
        }
    }

    public static void distribuirCartesMesa(Paquet p, LinkedList<Carte> mesa){
        for(int i=0;i<5;i++){
            int m = (int) (p.paquet.size() * Math.random());
            mesa.add(p.paquet.get(m));
            p.paquet.remove(m);
        }
    }

    public static void distributeurDArgentDebut(LinkedList<Joueur> joueurs, int q){
        for(Joueur j:joueurs){
            j.setArgent(q);
        }
    }
}
