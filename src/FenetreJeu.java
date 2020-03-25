import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.LinkedList;

public class FenetreJeu extends JFrame {

    private int nJoueurs;

    protected Jeu jeu;

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
    private JButton jouer; // tester changement de joueur actif

    private JButton flop;
    private JButton turn;
    private JButton river;
    private JButton restart;
    private JButton dealer;
    private JButton gagnant;

    private JLabel dealerLabel;
    private JLabel smallLabel;
    private JLabel bigLabel;
    private JLabel playingLabel;
    private JLabel nomJoueur;


    private JLabel valeurActuelleSlider;
    private JSlider raiseSlider;

    private LinkedList<JPanel> joueursOrdinateursCartes;
    private LinkedList<JPanel> joueursCartesPanels;
    private LinkedList<JPanel> joueursOrdinateurs;
    private LinkedList<JPanel> joueursPanels; //par defaut le joueurPanels(0) est le joueur humain

    public FenetreJeu(Jeu j, int nJoueurs){
        super("Poker");
        this.setSize(1350 ,750);

        this.nJoueurs = nJoueurs;
        jeu = j; // changer pour creer Circular LL


        joueursOrdinateursCartes = new LinkedList<>();
        joueursOrdinateurs = new LinkedList<>();
        jPrincipalCartes = new JPanel();
        tableCartes = new JPanel();
        principal = new JPanel(new BorderLayout());

        dealerLabel = new JLabel("DEALER", SwingConstants.LEFT);
        smallLabel = new JLabel("SMALL",SwingConstants.LEFT);
        bigLabel = new JLabel("BIG",SwingConstants.LEFT);
        playingLabel = new JLabel("ACTIF",SwingConstants.LEFT);
        nomJoueur = new JLabel(jeu.nomJoueur);

        call = new JButton("Call");
        raise = new JButton("Raise");
        fold = new JButton("Fold");
        jouer = new JButton("Jouer");
        jouer.addActionListener(new EcouteurTable(this, 'j'));

        flop = new JButton("Flop");
        flop.addActionListener(new EcouteurTable(this,'f'));
        turn = new JButton("Turn");
        turn.addActionListener(new EcouteurTable(this, 't'));
        river = new JButton("River");
        river.addActionListener(new EcouteurTable(this,'r'));
        restart = new JButton("Restart");
        restart.addActionListener(new EcouteurTable(this,'x'));
        dealer = new JButton("Dealer");
        dealer.addActionListener(new EcouteurTable(this, 'd'));
        gagnant = new JButton("Gagnant");
        gagnant.addActionListener(new EcouteurTable(this,'g'));


        //CONFIGURATION DU JSLIDER
       // 1 // raiseSlider = new JSlider(2*jeu.getBigBlind(), jeu.getJoueurs().getFirst().getArgent(), 2*jeu.getBigBlind()); // pas de GetFirst()
        raiseSlider = new JSlider(2*jeu.getBigBlind(), jeu.getHeadJoueur().getArgent(), 2*jeu.getBigBlind());

        valeurActuelleSlider = new JLabel();
        raiseSlider.setMinorTickSpacing(10);
        raiseSlider.setSnapToTicks(true);
        Hashtable<Integer, JLabel> valeursSlider = new Hashtable<Integer, JLabel>();
        valeursSlider.put(jeu.getBigBlind(), new JLabel(""+jeu.getBigBlind()));
       // 2 // valeursSlider.put(jeu.getJoueurs().getFirst().getArgent(), new JLabel(""+jeu.getJoueurs().getFirst().getArgent()));
        valeursSlider.put(jeu.getHeadJoueur().getArgent(), new JLabel(""+jeu.getHeadJoueur().getArgent()));

        raiseSlider.setLabelTable(valeursSlider);
        raiseSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
             // 3 //   if (raiseSlider.getValue()==jeu.getJoueurs().getFirst().getArgent()){
                if (raiseSlider.getValue()==jeu.getHeadJoueur().getArgent()){
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
        jPrincipalFonctions.add(dealer);
        jPrincipalFonctions.add(jouer);
        jPrincipalFonctions.add(gagnant);

       // joueursOrdinateurs.addFirst(jPrincipalFonctions);
        //joueursOrdinateursCartes.addFirst(jPrincipalCartes);
        //Affichage des cartes de cahque joueur aindi comme celles de la table

        for(int i=2; i<=nJoueurs;i++){
            joueursOrdinateursCartes.add(new JPanel(new FlowLayout()));
            joueursOrdinateurs.add(new JPanel(new BorderLayout()));
        }
        ajouterNomsJoueurs();
        ajouterCartesJoueurs();
        ajouterPositionsJoueurs();
        ajouterCartesTable();
        creerLayout();


        this.add(principal);

        this.setVisible(true);
    }

    /*
    Recupere l'icon des cartes des joueurs et celles de la table pour leur afficher dans la fenetre
     */
    private void ajouterCartesJoueurs(){
       // 4 // for (Carte c : jeu.getJoueurs().get(0).getCartesSurMain()){ // possiblement pas besoin de for (sont tjrs deux cartes)
        for (Carte c : jeu.getHeadJoueur().getCartesSurMain()){
            c.montrerCarte();
            jPrincipalCartes.add(new JLabel(c.icon));
        }
        Node current = jeu.getHead().prochainNode;
        int posJoueur = 0;
        do{
             for(JPanel j : joueursOrdinateurs){
                 posJoueur = joueursOrdinateurs.indexOf(j);
                 for (Carte c : current.joueur.getCartesSurMain()){
                     c.montrerCarte();
                     joueursOrdinateursCartes.get(posJoueur).add(new JLabel(c.icon));
                 }
                 j.add(joueursOrdinateursCartes.get(posJoueur), BorderLayout.CENTER);
                 j.add(new JLabel("Argent: "+current.joueur.getArgent(), SwingConstants.CENTER), BorderLayout.SOUTH);
                 current=current.prochainNode;
             }

        }while(!current.joueur.equals(jeu.getHeadJoueur())); //tant que le prochain joueur n'est pas le joueur reel
        /* BOUCLE CHANGEE
        for (int i=1; i<nJoueurs ; i++){
            for (Carte c : jeu.getJoueurs().get(i).getCartesSurMain()){
                joueursOrdinateursCartes.get(i-1).add(new JLabel(c.icon));
            }
            joueursOrdinateurs.get(i-1).add(joueursOrdinateursCartes.get(i-1), BorderLayout.CENTER);
            joueursOrdinateurs.get(i-1).add(new JLabel(jeu.getJoueurs().get(i).nom, SwingConstants.CENTER), BorderLayout.NORTH);
            joueursOrdinateurs.get(i-1).add(new JLabel("Argent: "+jeu.getJoueurs().get(i).getArgent(), SwingConstants.CENTER), BorderLayout.SOUTH);
        }
         */
    }
    public void ajouterNomsJoueurs(){ //création du jeu
        Node current = jeu.getHead();
        int posLinkedList = 0;
        jPrincipalCartes.add(nomJoueur);
        do{
            current = current.prochainNode;
            joueursOrdinateurs.get(posLinkedList).add(new JLabel(current.joueur.nom, SwingConstants.CENTER), BorderLayout.NORTH);
            posLinkedList++;
        }while(!current.prochainNode.equals(jeu.getHead())); //tant que le prochain joueur n'est pas le joueur reel
    }
     public void ajouterPositionsJoueurs(){ // creation du jeu, changerDealer() et avancerJeu()
        Node current = jeu.getHead();
        int posLinkedList = 0;
            do{
            current = current.prochainNode;
            if(current.joueur.dealer){
                joueursOrdinateurs.get(posLinkedList).add(dealerLabel, BorderLayout.WEST);
            }
            if(current.joueur.smallBlind){
                joueursOrdinateurs.get(posLinkedList).add(smallLabel, BorderLayout.WEST);
            }
            if(current.joueur.bigBlind){
                joueursOrdinateurs.get(posLinkedList).add(bigLabel, BorderLayout.WEST);
            }
            if(current.joueur.playing) {
                joueursOrdinateurs.get(posLinkedList).add(playingLabel, BorderLayout.EAST);
            }

            posLinkedList++;
        }while(!current.prochainNode.equals(jeu.getHead())); //tant que le prochain joueur n'est pas le joueur reel
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
            centre.setBorder(new EmptyBorder(new Insets(150, 100, 0, 100)));
            centre.add(tableCartes);

            //OUEST
            ouest = new JPanel(new BorderLayout());
            ouest.setBorder(new EmptyBorder(new Insets(80, 0, 150, 0)));
            ouest.add(joueursOrdinateurs.get(0), BorderLayout.CENTER);

            //NORD
            nord = new JPanel(new GridLayout(1,3,100,0));
            nord.add(joueursOrdinateurs.get(1));
            nord.add(joueursOrdinateurs.get(2));
            nord.add(joueursOrdinateurs.get(3));

            //EST
            est = new JPanel(new BorderLayout());
            est.setBorder(new EmptyBorder(new Insets(80,0,150,0)));
            est.add(joueursOrdinateurs.get(4), BorderLayout.CENTER);

            //SUD
            sud = new JPanel(new BorderLayout());
            // 5 // sud.add(new JLabel(jeu.getJoueurs().getFirst().nom, SwingConstants.CENTER));
            sud.add(new JLabel(jeu.getHeadJoueur().nom, SwingConstants.CENTER));
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
            centre.setBorder(new EmptyBorder(new Insets(150, 100, 0, 100)));
            centre.add(tableCartes);

            //OUEST
            ouest = new JPanel(new BorderLayout());
            ouest.setBorder(new EmptyBorder(new Insets(40, 0, 0, 0)));
            ouest.add(joueursOrdinateurs.get(0), BorderLayout.SOUTH);
            ouest.add(joueursOrdinateurs.get(1), BorderLayout.NORTH);

            //NORD
            nord = new JPanel(new GridLayout(1,4,60,0));
            nord.add(joueursOrdinateurs.get(2));
            nord.add(joueursOrdinateurs.get(3));
            nord.add(joueursOrdinateurs.get(4));
            nord.add(joueursOrdinateurs.get(5));

            //EST
            est = new JPanel(new BorderLayout());
            est.setBorder(new EmptyBorder(new Insets(40,0,0,0)));
            est.add(joueursOrdinateurs.get(6), BorderLayout.NORTH);
            est.add(joueursOrdinateurs.get(7), BorderLayout.SOUTH);

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

    protected void results(){
        // 6 //  jPrincipalCartes.add(new JLabel("Hand: "+jeu.getJoueurs().getFirst().getHand().getDescription()));
        jPrincipalCartes.add(new JLabel("Hand: "+jeu.getHeadJoueur().getHand().getDescription()));
        // 7 //for(Carte c : jeu.getJoueurs().getFirst().getHand().hand){
        for(Carte c : jeu.getHeadJoueur().getHand().getCartesHand()){
            jPrincipalCartes.add(new JLabel(c.icon));
        }
        mettreAJourTable();
    }

    protected void restart(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        jeu.reinitialiser();
        new FenetreJeu(jeu, nJoueurs);
    }
    protected void dealer(){
        jeu.changerDealer();
        restart();
    }

    protected void avancerJeu(){
        jeu.avancerJeu();
        ajouterNomsJoueurs();
        ajouterPositionsJoueurs();
        mettreAJourTable();
    }

    protected void gagnant(){
        if(jeu.joueursGagnants().size()==1){
            jPrincipalCartes.add(new JLabel(jeu.joueursGagnants().getFirst().nom));
            for(Carte c: jeu.joueursGagnants().getFirst().getHand().getCartesHand()){
                jPrincipalCartes.add(new JLabel(c.icon));
            }
        }
        jPrincipalCartes.add(new JLabel(jeu.joueursGagnants().getFirst().getHand().getDescription()));
        revalidate();
        repaint();
    }

    private void mettreAJourTable(){
        tableCartes.removeAll();
        ajouterCartesTable();
        revalidate();
        repaint();
    }

    public static void main(String[] args){

    }
}
