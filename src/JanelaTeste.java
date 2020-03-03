import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;

public class JanelaTeste extends JFrame {

    private Jeu jogo = new Jeu();


    private JPanel principal = new JPanel(new BorderLayout());
    private JPanel CartesJogadorEsquerda = new JPanel(new FlowLayout());
    private JPanel CartesJogadorDireita = new JPanel(new FlowLayout());
    private JPanel CartesJogadorTopo = new JPanel(new GridLayout(1,3));
    private JPanel topo1 = new JPanel(new FlowLayout());
    private JPanel topo2 = new JPanel(new FlowLayout());
    private JPanel topo3 = new JPanel(new FlowLayout());
    private JPanel CartesJogadorBaixo = new JPanel(new FlowLayout());
    private JPanel mesa = new JPanel(new FlowLayout());
    private JButton flop = new JButton("Flop");
    private JButton turn = new JButton("Turn");
    private JButton river = new JButton("River");
    private JButton restart = new JButton("Restart");



    public JanelaTeste(){
        super("Poker");
        this.setSize(1350 ,600);

        for(Carte c: jogo.getCartesMesa()){
            mesa.add(new JButton(c.icon));
        }

        CartesJogadorBaixo.add(new JLabel(jogo.getJogadores().get(0).nome));
        for(Carte c:jogo.getJogadores().get(0).getHand()){
            CartesJogadorBaixo.add(new JButton(c.icon));
        }
        CartesJogadorBaixo.add(new JLabel("Argent: "+jogo.getJogadores().get(0).getDinheiro()));

        CartesJogadorEsquerda.add(new JLabel(jogo.getJogadores().get(1).nome));
        for(Carte c:jogo.getJogadores().get(1).getHand()){
            CartesJogadorEsquerda.add(new JButton(c.icon));
        }
        CartesJogadorEsquerda.add(new JLabel("Argent: "+jogo.getJogadores().get(1).getDinheiro()));

        topo1.add(new JLabel(jogo.getJogadores().get(2).nome));
        for(Carte c:jogo.getJogadores().get(2).getHand()){
            topo1.add(new JButton(c.icon));
        }
        topo1.add(new JLabel("Argent: "+jogo.getJogadores().get(2).getDinheiro()));

        topo2.add(new JLabel(jogo.getJogadores().get(3).nome));
        for(Carte c:jogo.getJogadores().get(3).getHand()){
            topo2.add(new JButton(c.icon));
        }
        topo2.add(new JLabel("Argent: "+jogo.getJogadores().get(3).getDinheiro()));

        topo3.add(new JLabel(jogo.getJogadores().get(4).nome));
        for(Carte c:jogo.getJogadores().get(4).getHand()){
            topo3.add(new JButton(c.icon));
        }
        topo3.add(new JLabel("Argent: "+jogo.getJogadores().get(4).getDinheiro()));

        CartesJogadorDireita.add(new JLabel(jogo.getJogadores().get(5).nome));
        for(Carte c:jogo.getJogadores().get(5).getHand()){
            CartesJogadorDireita.add(new JButton(c.icon));
        }
        CartesJogadorDireita.add(new JLabel("Argent: "+jogo.getJogadores().get(5).getDinheiro()));



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
        CartesJogadorEsquerda.setBorder(new EmptyBorder(new Insets(120,0,150,0)));
        CartesJogadorDireita.setBorder(new EmptyBorder(new Insets(120,0,150,0)));


        CartesJogadorBaixo.add(flop);
        CartesJogadorBaixo.add(turn);
        CartesJogadorBaixo.add(river);
        CartesJogadorBaixo.add(restart);
        CartesJogadorTopo.add(topo1);
        CartesJogadorTopo.add(topo2);
        CartesJogadorTopo.add(topo3);
        principal.add(CartesJogadorBaixo, BorderLayout.SOUTH);
        principal.add(CartesJogadorEsquerda, BorderLayout.WEST);
        principal.add(CartesJogadorTopo, BorderLayout.NORTH);
        principal.add(CartesJogadorDireita, BorderLayout.EAST);
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
        for(Carte c: jogo.getCartesMesa()){
            mesa.add(new JButton(c.icon));
        }
    }

    public void restart(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        new JanelaTeste();
    }
}

