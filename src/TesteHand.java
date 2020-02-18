import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){

        Jogo jogo = new Jogo();
        jogo.distribuirCartas();
        System.out.println(jogo.visualizarBaralho());
        System.out.println("Maos jogadores \n");
        System.out.println(jogo.maosJogadores());

        System.out.println("Cartas mesa \n");
        System.out.println(jogo.visualizarMesa());

    }
}
