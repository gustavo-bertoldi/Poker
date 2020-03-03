import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;

public class JanelaTeste extends JFrame {

    private Jeu jogo = new Jeu(20,0);


    private JPanel principal = new JPanel(new BorderLayout());
    private JPanel CartesJogadorEsquerda = new JPanel(new FlowLayout());
    private JPanel CartesJogadorDireita = new JPanel(new FlowLayout());
    private JPanel CartesJogadorTopo = new JPanel(new GridLayout(1,3));
    private JPanel baixo = new JPanel(new FlowLayout());
    private JPanel topo1 = new JPanel(new FlowLayout());
    private JPanel topo2 = new JPanel(new FlowLayout());
    private JPanel topo3 = new JPanel(new FlowLayout());
    private JPanel functionsJogadorBaixo = new JPanel(new FlowLayout());
    private JPanel infosJogadorBaixo = new JPanel(new FlowLayout());
    private JPanel CartesJogadorBaixo = new JPanel(new FlowLayout());
    private JPanel mesa = new JPanel(new FlowLayout());
    private JButton flop = new JButton("Flop");
    private JButton turn = new JButton("Turn");
    private JButton river = new JButton("River");
    private JButton restart = new JButton("Restart");
    private JButton fold = new JButton("Fold");
    private JButton call = new JButton("Call");
    private JButton raise = new JButton("Raise");
    private JTextField valeurRaise = new JTextField(6);



    public JanelaTeste(){
        super("Poker");
        this.setSize(1350 ,600);

        for(Carte c: jogo.getCartesMesa()){
            mesa.add(new JButton(c.icon));
        }
        mesa.add(new JLabel("Pot: "+jogo.getMesa().getPot()));
        mesa.add(new JLabel("J ACT: "+jogo.joueurActif));
        mesa.add(new JLabel("Call: "+jogo.getValeurCall()));

        flop.addActionListener(new EcouteurTable(this,'f'));
        turn.addActionListener(new EcouteurTable(this,'t'));
        river.addActionListener(new EcouteurTable(this,'r'));
        restart.addActionListener(new EcouteurTable(this,'x'));
        fold.addActionListener(new EcouteurJoueur(this, 'f'));
        call.addActionListener(new EcouteurJoueur(this, 'c'));
        raise.addActionListener(new EcouteurJoueur(this, 'r'));

        mesa.setBorder(new EmptyBorder(new Insets(120,100,150,100)));
        CartesJogadorEsquerda.setBorder(new EmptyBorder(new Insets(120,0,150,0)));
        CartesJogadorDireita.setBorder(new EmptyBorder(new Insets(120,0,150,0)));

        infosJogadorBaixo.add(new JLabel(jogo.getJoueurs().get(0).nom));
        infosJogadorBaixo.add(new JLabel("Argent: "+jogo.getJoueurs().get(0).getArgent()));
        for(Carte c: jogo.getJoueurs().get(0).getCartesInitiales()){
            CartesJogadorBaixo.add(new JButton(c.icon));
        }

        CartesJogadorEsquerda.add(new JLabel(jogo.getJoueurs().get(1).nom));
        CartesJogadorEsquerda.add(new JLabel("Argent: "+jogo.getJoueurs().get(1).getArgent()));
        for(Carte c: jogo.getJoueurs().get(1).getCartesInitiales()){
            CartesJogadorEsquerda.add(new JButton(c.icon));
        }

        topo1.add(new JLabel(jogo.getJoueurs().get(2).nom));
        topo1.add(new JLabel("Argent: "+jogo.getJoueurs().get(2).getArgent()));
        for(Carte c: jogo.getJoueurs().get(2).getCartesInitiales()){
            topo1.add(new JButton(c.icon));
        }

        topo2.add(new JLabel(jogo.getJoueurs().get(3).nom));
        topo2.add(new JLabel("Argent: "+jogo.getJoueurs().get(3).getArgent()));
        for(Carte c: jogo.getJoueurs().get(3).getCartesInitiales()){
            topo2.add(new JButton(c.icon));
        }

        topo3.add(new JLabel(jogo.getJoueurs().get(4).nom));
        topo3.add(new JLabel("Argent: "+jogo.getJoueurs().get(4).getArgent()));
        for(Carte c: jogo.getJoueurs().get(4).getCartesInitiales()){
            topo3.add(new JButton(c.icon));
        }

        CartesJogadorDireita.add(new JLabel(jogo.getJoueurs().get(5).nom));
        CartesJogadorDireita.add(new JLabel("Argent: "+jogo.getJoueurs().get(5).getArgent()));
        for(Carte c: jogo.getJoueurs().get(5).getCartesInitiales()){
            CartesJogadorDireita.add(new JButton(c.icon));
        }

        functionsJogadorBaixo.add(flop);
        functionsJogadorBaixo.add(turn);
        functionsJogadorBaixo.add(river);
        functionsJogadorBaixo.add(restart);
        functionsJogadorBaixo.add(fold);
        functionsJogadorBaixo.add(call);
        functionsJogadorBaixo.add(raise);
        functionsJogadorBaixo.add(valeurRaise);
        CartesJogadorTopo.add(topo1);
        CartesJogadorTopo.add(topo2);
        CartesJogadorTopo.add(topo3);
        baixo.add(functionsJogadorBaixo);
        baixo.add(infosJogadorBaixo);
        baixo.add(CartesJogadorBaixo);
        principal.add(baixo, BorderLayout.SOUTH);
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
        mettreAJourTable();
    }

    public void turn(){
        jogo.getMesa().turn();
        mettreAJourTable();
    }

    public void river(){
        jogo.getMesa().river();
        mettreAJourTable();
    }

    public void mettreAJourTable(){
        mesa.removeAll();
        for(Carte c: jogo.getCartesMesa()){
            mesa.add(new JButton(c.icon));
        }
        mesa.add(new JLabel("Pot: "+jogo.getMesa().getPot()));
        mesa.add(new JLabel("J ACT: "+jogo.joueurActif));
    }

    public void mettreAJourCartesJoueur(int indice){
        CartesJogadorBaixo.removeAll();
        for(Carte c:jogo.getJoueurs().get(indice).getCartesInitiales()){
            CartesJogadorBaixo.add(new JButton(c.icon));
        }
    }

    public void mettreAJourInfosJoueur(int indice){
        infosJogadorBaixo.removeAll();
        infosJogadorBaixo.add(new JLabel(jogo.getJoueurs().get(indice).nom));
        infosJogadorBaixo.add(new JLabel("Argent: "+jogo.getJoueurs().get(indice).getArgent()));
    }

    public void restart(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        new JanelaTeste();
    }

    public void call(){
        jogo.parier(jogo.getValeurCall(),0);
        mettreAJourInfosJoueur(0);
        mettreAJourTable();
        getJouerActif().dejaJoue=true;
        prochainJoueur();
    }

    public void fold(){
        jogo.getJoueurs().get(0).fold();
        mettreAJourCartesJoueur(0);
        mettreAJourTable();
        getJouerActif().dejaJoue=true;
        prochainJoueur();
    }

    public void raise(){
        jogo.parier(getRaise(),0);
        mettreAJourInfosJoueur(0);
        mettreAJourTable();
        getJouerActif().dejaJoue=true;
    }

    public Joueur getJouerActif(){
        return jogo.getJoueurs().get(jogo.joueurActif);
    }

    public int getRaise(){
        int ret=0;
        boolean ok=true;
        try {
            ret = Integer.parseInt(valeurRaise.getText());
        }
        catch(Exception e){
            ok=false;
            ret=0;
        }
        if(ok){
            prochainJoueur();
        }
        return ret;
    }

    public void prochainJoueur(){
        functionsJogadorBaixo.setVisible(false);
        jogo.prochainJoueur();
        mettreAJourTable();
        revalidate();
        repaint();
    }

    public void faireJoueurActif(){
        if(jogo.joueurActif==0){
            functionsJogadorBaixo.setVisible(true);
        }
        jogo.getJoueurs().get(jogo.joueurActif).jouer();
    }
}

