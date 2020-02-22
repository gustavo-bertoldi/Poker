import javax.swing.*;
import java.awt.*;

public class JanelaTeste extends JFrame {

    private Jogo jogo = new Jogo();


    private JPanel principal = new JPanel(new BorderLayout());
    private JPanel cartasJogadorEsquerda = new JPanel(new FlowLayout());
    private JPanel cartasJogadorDireita = new JPanel(new FlowLayout());
    private JPanel cartasJogadorTopo = new JPanel(new GridLayout(1,3));
    private JPanel topo1 = new JPanel(new FlowLayout());
    private JPanel topo2 = new JPanel(new FlowLayout());
    private JPanel topo3 = new JPanel(new FlowLayout());
    private JPanel cartasJogadorBaixo = new JPanel(new FlowLayout());
    private JPanel mesa = new JPanel(new FlowLayout());



    public JanelaTeste(){
        super("Poker");
        this.setSize(1200 ,400);
        jogo.distribuirCartas();

        for(Carta c: jogo.getMesa()){
            mesa.add(new JButton(c.icone));
        }

        for(Carta c:jogo.getJogadores().get(0).getHand()){
            cartasJogadorBaixo.add(new JButton(c.icone));
        }

        for(Carta c:jogo.getJogadores().get(1).getHand()){
            cartasJogadorEsquerda.add(new JButton(c.icone));
        }

        for(Carta c:jogo.getJogadores().get(2).getHand()){
            topo1.add(new JButton(c.icone));
        }

        for(Carta c:jogo.getJogadores().get(3).getHand()){
            topo2.add(new JButton(c.icone));
        }

        for(Carta c:jogo.getJogadores().get(4).getHand()){
            topo3.add(new JButton(c.icone));
        }

        for(Carta c:jogo.getJogadores().get(5).getHand()){
            cartasJogadorDireita.add(new JButton(c.icone));
        }


        this.add(mesa, BorderLayout.CENTER);
        cartasJogadorTopo.add(topo1);
        cartasJogadorTopo.add(topo2);
        cartasJogadorTopo.add(topo3);
        this.add(cartasJogadorBaixo, BorderLayout.SOUTH);
        this.add(cartasJogadorEsquerda, BorderLayout.WEST);
        this.add(cartasJogadorTopo, BorderLayout.NORTH);
        this.add(cartasJogadorDireita, BorderLayout.EAST);
        this.add(mesa, BorderLayout.CENTER);


        this.setVisible(true);

    }

    public static void main(String[] args){
        JanelaTeste j = new JanelaTeste();
    }
}

