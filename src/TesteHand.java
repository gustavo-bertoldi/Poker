import java.util.LinkedList;

public class TesteHand {

    public static void main(String[] args){
        LinkedList<Carta> cartas = new LinkedList<Carta>();
        cartas.add(new Carta(14,'o'));
        cartas.add(new Carta(12,'o'));
        cartas.add(new Carta(11,'o'));
        cartas.add(new Carta(2,'o'));
        cartas.add(new Carta(5,'o'));
        cartas.add(new Carta(3,'o'));
        cartas.add(new Carta(4,'o'));
/*
        Jogo jogo = new Jogo();
        jogo.distribuirCartas();
        System.out.println(jogo.visualizarBaralho());
        System.out.println("Maos jogadores \n");
        System.out.println(jogo.maosJogadores());
*/
        Hand teste=new Hand(cartas);
        //System.out.println("Cartas mesa \n");
       // System.out.println(jogo.visualizarMesa());

        System.out.println(teste.toString(teste.straight()));
    }
}


