import java.util.LinkedList;

public class Distribuidor {

    public static void distribuirCartasJogadores(Baralho b, LinkedList<Jogador> jogadores) {
        for (Jogador j : jogadores) {
            LinkedList<Carta> hand = new LinkedList<>();
            int i = (int) (b.baralho.size() * Math.random());
            hand.add(b.baralho.get(i));
            b.baralho.remove(i);
            i = (int) (b.baralho.size() * Math.random());
            hand.add(b.baralho.get(i));
            b.baralho.remove(i);
            j.setHand(hand);
        }
    }

    public static void distribuirCartasMesa(Baralho b, LinkedList<Carta> mesa){
        for(int i=0;i<5;i++){
            int m = (int) (b.baralho.size() * Math.random());
            mesa.add(b.baralho.get(m));
            b.baralho.remove(m);
        }
    }
}
