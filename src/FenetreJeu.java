import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.LinkedList;

public class FenetreJeu extends JFrame {

    private int nJoueurs;

    private Jeu jeu;

    private JPanel principal;
    private JPanel jPrincipalFonctions;
    private JPanel jPrincipalCartes;
    private JPanel tableCartes;

    private JPanel sud;
    private JPanel ouest;
    private JPanel est;
    private JPanel nord;
    private JPanel centre;

    private JButton call;
    private JButton raise;
    private JButton fold;

    private JButton flop;
    private JButton turn;
    private JButton river;
    private JButton restart;

    private JLabel valeurActuelleSlider;
    private JSlider raiseSlider;

    private LinkedList<JPanel> joueursOrdinateursCartes;

    public FenetreJeu(int nJoueurs){
        super("Poker");
        this.setSize(1350 ,750);

        this.nJoueurs = nJoueurs;
        jeu = new Jeu(nJoueurs,0);


        joueursOrdinateursCartes = new LinkedList<>();
        jPrincipalCartes = new JPanel();
        tableCartes = new JPanel();
        principal = new JPanel(new BorderLayout());

        call = new JButton("Call");
        raise = new JButton("Raise");
        fold = new JButton("Fold");

        flop = new JButton("Flop");
        flop.addActionListener(new EcouteurTable(this,'f'));
        turn = new JButton("Turn");
        turn.addActionListener(new EcouteurTable(this, 't'));
        river = new JButton("River");
        river.addActionListener(new EcouteurTable(this,'r'));
        restart = new JButton("Restart");
        restart.addActionListener(new EcouteurTable(this,'x'));

        //CONFIGURATION DU JSLIDER
        raiseSlider = new JSlider(2*jeu.getBigBlind(), jeu.getJoueurs().getFirst().getArgent(), 2*jeu.getBigBlind());
        valeurActuelleSlider = new JLabel();
        raiseSlider.setMinorTickSpacing(10);
        raiseSlider.setSnapToTicks(true);
        Hashtable<Integer, JLabel> valeursSlider = new Hashtable<Integer, JLabel>();
        valeursSlider.put(jeu.getBigBlind(), new JLabel(""+jeu.getBigBlind()));
        valeursSlider.put(jeu.getJoueurs().getFirst().getArgent(), new JLabel(""+jeu.getJoueurs().getFirst().getArgent()));
        raiseSlider.setLabelTable(valeursSlider);
        raiseSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (raiseSlider.getValue()==jeu.getJoueurs().getFirst().getArgent()){
                    valeurActuelleSlider.setText("All in: "+raiseSlider.getValue());
                }
                else {
                    valeurActuelleSlider.setText("Raise: " + raiseSlider.getValue());
                }
            }
        });
        raiseSlider.setPaintLabels(true);


        jPrincipalFonctions = new JPanel(new FlowLayout());
        jPrincipalFonctions.add(fold);
        jPrincipalFonctions.add(call);
        jPrincipalFonctions.add(raise);
        jPrincipalFonctions.add(raiseSlider);
        jPrincipalFonctions.add(valeurActuelleSlider);
        jPrincipalFonctions.add(flop);
        jPrincipalFonctions.add(turn);
        jPrincipalFonctions.add(river);
        jPrincipalFonctions.add(restart);

        //Affichage des cartes de cahque joueur aindi comme celles de la table

        for(int i=2; i<=nJoueurs;i++){
            joueursOrdinateursCartes.add(new JPanel());
        }
        ajouterCartesJoueurs();
        ajouterCartesTable();
        creerLayout();

        this.add(principal);

        this.setVisible(true);
    }

    /*
    Recupere l'icon des cartes des joueurs et celles de la table pour leur afficher dans la fenetre
     */
    private void ajouterCartesJoueurs(){
        for (Carte c : jeu.getJoueurs().get(0).getCartesSurMain()){
            c.montrerCarte();
            jPrincipalCartes.add(new JLabel(c.icon));
        }

        for (int i=1; i<nJoueurs ; i++){
            for (Carte c : jeu.getJoueurs().get(i).getCartesSurMain()){
                joueursOrdinateursCartes.get(i-1).add(new JLabel(c.icon));
            }
        }
    }

    private void ajouterCartesTable(){
        for (Carte c : jeu.getCartesTable()){
            tableCartes.add(new JLabel(c.icon));
        }
    }

    /*
    Cree le layout du jeu en disposant les jouerus de maniere optimale selon le nombre de joueurs
     */
    private void creerLayout(){
        //Organisation des panels en fonction du nombre de joueurs
        if(nJoueurs==6) {
            //CENTRE
            centre = new JPanel();
            centre.setBorder(new EmptyBorder(new Insets(180, 100, 0, 100)));
            centre.add(tableCartes);

            //OUEST
            ouest = new JPanel(new BorderLayout());
            ouest.setBorder(new EmptyBorder(new Insets(120, 0, 150, 0)));
            ouest.add(joueursOrdinateursCartes.get(0), BorderLayout.CENTER);

            //NORD
            nord = new JPanel(new GridLayout(1,3,100,0));
            nord.add(joueursOrdinateursCartes.get(1));
            nord.add(joueursOrdinateursCartes.get(2));
            nord.add(joueursOrdinateursCartes.get(3));

            //EST
            est = new JPanel(new BorderLayout());
            est.setBorder(new EmptyBorder(new Insets(120,0,150,0)));
            est.add(joueursOrdinateursCartes.get(4), BorderLayout.CENTER);

            //SUD
            sud = new JPanel(new BorderLayout());
            sud.add(jPrincipalFonctions, BorderLayout.SOUTH);
            sud.add(jPrincipalCartes, BorderLayout.CENTER);

            principal.add(sud, BorderLayout.SOUTH);
            principal.add(ouest, BorderLayout.WEST);
            principal.add(nord, BorderLayout.NORTH);
            principal.add(est, BorderLayout.EAST);
            principal.add(centre, BorderLayout.CENTER);

        }

        else if (nJoueurs == 9){
            //CENTRE
            centre = new JPanel();
            centre.setBorder(new EmptyBorder(new Insets(180, 100, 0, 100)));
            centre.add(tableCartes);

            //OUEST
            ouest = new JPanel(new BorderLayout());
            ouest.setBorder(new EmptyBorder(new Insets(100, 0, 0, 0)));
            ouest.add(joueursOrdinateursCartes.get(0), BorderLayout.SOUTH);
            ouest.add(joueursOrdinateursCartes.get(1), BorderLayout.NORTH);

            //NORD
            nord = new JPanel(new GridLayout(1,4,100,0));
            nord.add(joueursOrdinateursCartes.get(2));
            nord.add(joueursOrdinateursCartes.get(3));
            nord.add(joueursOrdinateursCartes.get(4));
            nord.add(joueursOrdinateursCartes.get(5));

            //EST
            est = new JPanel(new BorderLayout());
            est.setBorder(new EmptyBorder(new Insets(100,0,0,0)));
            est.add(joueursOrdinateursCartes.get(6), BorderLayout.NORTH);
            est.add(joueursOrdinateursCartes.get(7), BorderLayout.SOUTH);

            //SUD
            sud = new JPanel(new BorderLayout());
            sud.add(jPrincipalFonctions,BorderLayout.SOUTH);
            sud.add(jPrincipalCartes,BorderLayout.CENTER);

            principal.add(sud, BorderLayout.SOUTH);
            principal.add(ouest, BorderLayout.WEST);
            principal.add(nord, BorderLayout.NORTH);
            principal.add(est, BorderLayout.EAST);
            principal.add(centre, BorderLayout.CENTER);

        }
    }

    protected void flop(){
        jeu.getCartesTable().get(0).montrerCarte();
        jeu.getCartesTable().get(1).montrerCarte();
        jeu.getCartesTable().get(2).montrerCarte();
        mettreAJourTable();
    }

    protected void turn(){
        jeu.getCartesTable().get(3).montrerCarte();
        mettreAJourTable();
    }

    protected void river(){
        jeu.getCartesTable().get(4).montrerCarte();
        mettreAJourTable();
    }

    protected void restart(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        new FenetreJeu(nJoueurs);
    }

    private void mettreAJourTable(){
        tableCartes.removeAll();
        ajouterCartesTable();
        revalidate();
        repaint();
    }

    public static void main(String[] args){
         FenetreJeu fj = new FenetreJeu(9);
    }
}
