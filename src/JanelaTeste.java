import kotlin.reflect.jvm.internal.impl.resolve.scopes.MemberScope;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;

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
    private JButton flop = new JButton("Flop");
    private JButton turn = new JButton("Turn");
    private JButton river = new JButton("River");
    private JButton restart = new JButton("Restart");



    public JanelaTeste(){
        super("Poker");
        this.setSize(1350 ,600);

        for(Carta c: jogo.getCartasMesa()){
            mesa.add(new JButton(c.icone));
        }

        cartasJogadorBaixo.add(new JLabel(jogo.getJogadores().get(0).nome));
        for(Carta c:jogo.getJogadores().get(0).getHand()){
            cartasJogadorBaixo.add(new JButton(c.icone));
        }
        cartasJogadorBaixo.add(new JLabel("Argent: "+jogo.getJogadores().get(0).getDinheiro()));

        cartasJogadorEsquerda.add(new JLabel(jogo.getJogadores().get(1).nome));
        for(Carta c:jogo.getJogadores().get(1).getHand()){
            cartasJogadorEsquerda.add(new JButton(c.icone));
        }
        cartasJogadorEsquerda.add(new JLabel("Argent: "+jogo.getJogadores().get(1).getDinheiro()));

        topo1.add(new JLabel(jogo.getJogadores().get(2).nome));
        for(Carta c:jogo.getJogadores().get(2).getHand()){
            topo1.add(new JButton(c.icone));
        }
        topo1.add(new JLabel("Argent: "+jogo.getJogadores().get(2).getDinheiro()));

        topo2.add(new JLabel(jogo.getJogadores().get(3).nome));
        for(Carta c:jogo.getJogadores().get(3).getHand()){
            topo2.add(new JButton(c.icone));
        }
        topo2.add(new JLabel("Argent: "+jogo.getJogadores().get(3).getDinheiro()));

        topo3.add(new JLabel(jogo.getJogadores().get(4).nome));
        for(Carta c:jogo.getJogadores().get(4).getHand()){
            topo3.add(new JButton(c.icone));
        }
        topo3.add(new JLabel("Argent: "+jogo.getJogadores().get(4).getDinheiro()));

        cartasJogadorDireita.add(new JLabel(jogo.getJogadores().get(5).nome));
        for(Carta c:jogo.getJogadores().get(5).getHand()){
            cartasJogadorDireita.add(new JButton(c.icone));
        }
        cartasJogadorDireita.add(new JLabel("Argent: "+jogo.getJogadores().get(5).getDinheiro()));



        flop.addActionListener(new EcouteurTable(this,'f'));
        turn.addActionListener(new EcouteurTable(this,'t'));
        river.addActionListener(new EcouteurTable(this,'r'));
        restart.addActionListener(new EcouteurTable(this,'x'));

        /*
        BoxLayout boxCentro = new BoxLayout(centro,BoxLayout.Y_AXIS);
        BoxLayout boxDireita = new BoxLayout(direita,BoxLayout.Y_AXIS);
        BoxLayout boxEsquerda = new BoxLayout(direita,BoxLayout.Y_AXIS);
        direita.setLayout(boxDireita);
        direita.setBorder(new EmptyBorder(new Insets(180, 0, 150, 0)));
        centro.setLayout(boxCentro);
        centro.setBorder(new EmptyBorder(new Insets(120, 100, 150, 100)));
        esquerda.setLayout(boxEsquerda);
        esquerda.setBorder(new EmptyBorder(new Insets(120, 100, 150, 100)));
    */

        mesa.setBorder(new EmptyBorder(new Insets(120,100,150,100)));
        cartasJogadorEsquerda.setBorder(new EmptyBorder(new Insets(120,0,150,0)));
        cartasJogadorDireita.setBorder(new EmptyBorder(new Insets(120,0,150,0)));


        cartasJogadorBaixo.add(flop);
        cartasJogadorBaixo.add(turn);
        cartasJogadorBaixo.add(river);
        cartasJogadorBaixo.add(restart);
        cartasJogadorTopo.add(topo1);
        cartasJogadorTopo.add(topo2);
        cartasJogadorTopo.add(topo3);
        principal.add(cartasJogadorBaixo, BorderLayout.SOUTH);
        principal.add(cartasJogadorEsquerda, BorderLayout.WEST);
        principal.add(cartasJogadorTopo, BorderLayout.NORTH);
        principal.add(cartasJogadorDireita, BorderLayout.EAST);
        principal.add(mesa, BorderLayout.CENTER);

        this.add(principal);


        this.setVisible(true);

    }

    public static void main(String[] args){
        JanelaTeste j = new JanelaTeste();
    }

    public void flop(){
        jogo.getMesa().flop();
        mettreAJourCartesTable();
        revalidate();
        repaint();
    }

    public void turn(){
        jogo.getMesa().turn();
        mettreAJourCartesTable();
        revalidate();
        repaint();
    }

    public void river(){
        jogo.getMesa().river();
        mettreAJourCartesTable();
        revalidate();
        repaint();
    }

    public void mettreAJourCartesTable(){
        mesa.removeAll();
        for(Carta c: jogo.getCartasMesa()){
            mesa.add(new JButton(c.icone));
        }
    }

    public void restart(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        new JanelaTeste();
    }
}

