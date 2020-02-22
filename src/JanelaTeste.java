import javax.swing.*;
import java.awt.*;

public class JanelaTeste extends JFrame {

    private Jogo jogo = new Jogo();


    private JPanel principal = new JPanel(new BorderLayout());
    private JPanel cartasJogador = new JPanel(new FlowLayout());
    private JPanel mesa = new JPanel(new FlowLayout());



    public JanelaTeste(){
        super("Poker");
        this.setSize(1200 ,1200);
        jogo.distribuirCartas();

        for(Carta c: jogo.getMesa()){
            mesa.add(new JButton(c.icone));
        }

        for(Carta c:jogo.getJogadores().get(0).getHand()){
            cartasJogador.add(new JButton(c.icone));
        }

        this.add(cartasJogador, BorderLayout.SOUTH);
        this.add(mesa, BorderLayout.CENTER);


        this.setVisible(true);

    }

    public static void main(String[] args){
        JanelaTeste j = new JanelaTeste();
    }
}

