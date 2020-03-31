import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.LinkedList;

public class FenetreJeuV2 extends JFrame {

    private JPanel cartesTable, principal, joueurGagnant, panelBoutons;

    private HashMap<Joueur, JPanel> cartesJoueurs;

    private HashMap<Joueur, JLabel> infosJoueur;

    private JButton call, raise, fold, commencerJeu, restart, flop, turn, river;

    private FlowLayout layoutCartes;

    private GridBagConstraints gbcPrincipal, gbcCartesJoueurs;

    private int nJoueurs;

    private Jeu jeu;

    private FenetreJeuV2 (int nJoueurs){
        super("Poker V2");
        setSize(1400, 650);

        this.nJoueurs = nJoueurs;
        jeu = new Jeu(nJoueurs, 0);

        gbcPrincipal = new GridBagConstraints();
        gbcCartesJoueurs = new GridBagConstraints();
        principal = new JPanel(new GridBagLayout());
        this.add(principal);
        creerLayout();

        valeursHandTerminal();

        this.setVisible(true);
    }

    private void ajouterCartesTable(){
        cartesTable = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCartesTable = new GridBagConstraints();
        gbcCartesTable.gridy=0;
        gbcCartesTable.insets = new Insets(0,5,0,5);
        int i=0;
        for (Carte c : jeu.getCartesTable()){
            gbcCartesTable.gridx=i;
            cartesTable.add(new JLabel(c.icon), gbcCartesTable);
            i++;
        }
    }

    private void ajouterCartesJoueurs(){
        Node current = jeu.getHead();
        cartesJoueurs = new HashMap<>();

        for(int i=0; i<nJoueurs; i++){
            cartesJoueurs.put(current.joueur,new JPanel(new BorderLayout()));
            current = current.prochainNode;
        }

        current = jeu.getHead();
        layoutCartes = new FlowLayout();
        layoutCartes.setHgap(10);

        for(int i=0; i<nJoueurs; i++) {
            Joueur j = current.joueur;
            JPanel cartes = new JPanel(layoutCartes);
            for (Carte c : j.getCartesSurMain()) {
                c.montrerCarte();
                cartes.add(new JLabel(c.icon));
            }
            cartesJoueurs.get(j).add(cartes, BorderLayout.CENTER);
            current = current.prochainNode;
        }
    }

    /*
    Met à jour les cartes des joueurs et de la table, utilisé pour commencer une nouvelle tournée
     */
    private void mettreAJourCartesJoueurs(){
        Node current = jeu.getHead();
        gbcCartesJoueurs.gridy=0;
        for(int i=0; i<nJoueurs; i++){
            cartesJoueurs.get(current.joueur).removeAll();
            JPanel cartes = new JPanel(layoutCartes);
            for (Carte c : current.joueur.getCartesSurMain()) {
                cartes.add(new JLabel(c.icon));
            }
            cartesJoueurs.get(current.joueur).add(cartes, BorderLayout.CENTER);
            current = current.prochainNode;
        }
        revalidate();
        repaint();
    }

    private void mettreAJourCartesTable(){
        cartesTable.removeAll();
        GridBagConstraints gbcCartesTable = new GridBagConstraints();
        gbcCartesTable.gridy=0;
        gbcCartesTable.insets = new Insets(0,5,0,5);
        int i=0;
        for (Carte c : jeu.getCartesTable()){
            gbcCartesTable.gridx=i;
            cartesTable.add(new JLabel(c.icon), gbcCartesTable);
            i++;
        }
        revalidate();
        repaint();
    }

    private void ajouterInfosJoueurs(){
        infosJoueur = new HashMap<>();
        Node current = jeu.getHead();

        for(int i=0; i<nJoueurs; i++){
            if(current.joueur.isDealer()){
                infosJoueur.put(current.joueur, new JLabel(current.joueur.nom+" || Argent: "+current.joueur.getArgent()+" || Dealer", SwingConstants.CENTER));
            }
            else if(current.joueur.isBigBlind()){
                infosJoueur.put(current.joueur, new JLabel(current.joueur.nom+" || Argent: "+current.joueur.getArgent()+" || BigBlind", SwingConstants.CENTER));
            }
            else if(current.joueur.isSmallBlind()){
                infosJoueur.put(current.joueur, new JLabel(current.joueur.nom+" || Argent: "+current.joueur.getArgent()+" || SmallBlind", SwingConstants.CENTER));
            }
            else {
                infosJoueur.put(current.joueur, new JLabel(current.joueur.nom + " || Argent: " + current.joueur.getArgent(), SwingConstants.CENTER));
            }

            cartesJoueurs.get(current.joueur).add(infosJoueur.get(current.joueur), BorderLayout.SOUTH);

            current = current.prochainNode;

        }


    }

    /*
    Met à jour les informations du joueurs (Nom, argent, Dealer, BigBlind, SmallBlind)
     */
    private void mettreAJourInfosJoueurs(){
        Node current = jeu.getHead();

        for(int i=0; i<nJoueurs; i++){
            if(current.joueur.isDealer()){
                infosJoueur.get(current.joueur).setText(current.joueur.nom+" || Argent: "+current.joueur.getArgent()+" || Dealer");
            }
            else if (current.joueur.isBigBlind()){
                infosJoueur.get(current.joueur).setText(current.joueur.nom+" || Argent: "+current.joueur.getArgent()+" || BigBlind");
            }
            else if (current.joueur.isSmallBlind()){
                infosJoueur.get(current.joueur).setText(current.joueur.nom+" || Argent: "+current.joueur.getArgent()+" || SmallBlind");
            }
            else {
                infosJoueur.get(current.joueur).setText(current.joueur.nom + " || Argent: " + current.joueur.getArgent());
            }
            current = current.prochainNode;
        }
        revalidate();
        repaint();
    }

    public void creerLayout(){
        Node current = jeu.getHead();
        ajouterCartesJoueurs();
        ajouterCartesTable();
        ajouterInfosJoueurs();
        gbcPrincipal = new GridBagConstraints();

        if(nJoueurs==6) {
            gbcPrincipal.weighty=3;
            gbcPrincipal.weightx=3;

            //JOUEUR 0 - PRINCIPAL (HUMAIN) SUD
            gbcPrincipal.gridy = 2;
            gbcPrincipal.gridx = 1;
            gbcPrincipal.anchor=GridBagConstraints.SOUTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 0);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 1 - OUEST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.anchor=GridBagConstraints.WEST;
            gbcPrincipal.insets = new Insets(0, 5, 0, 0);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;

            //JOUEUR 2 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(0, 30, 5, 0);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 3 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 1;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 0);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 4 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 2;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 50);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 5 - EST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 2;
            gbcPrincipal.anchor=GridBagConstraints.EAST;
            gbcPrincipal.insets = new Insets(0, 0, 0, 5);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //TABLE - CENTRE
            gbcPrincipal.gridx=1;
            gbcPrincipal.gridy=1;
            gbcPrincipal.anchor=GridBagConstraints.CENTER;
            gbcPrincipal.insets = new Insets(80, 100, 80, 100);
            principal.add(cartesTable, gbcPrincipal);
            current = current.prochainNode;

            //JOUEUR GAGNANT
            afficherHandGagnante();
            gbcPrincipal.gridx=0;
            gbcPrincipal.gridy=2;
            gbcPrincipal.anchor=GridBagConstraints.CENTER;
            gbcPrincipal.insets = new Insets(0,0,0,0);
            principal.add(joueurGagnant, gbcPrincipal);
            joueurGagnant.setVisible(false);

            //BOUTONS
            panelBoutons=new JPanel(new GridLayout(4,2));
            restart = new JButton("Restart");
            restart.addActionListener(new EcouteurV2(this, "restart"));
            call = new JButton("Call");
            call.addActionListener(new EcouteurV2(this,"call"));
            raise = new JButton("Raise");
            raise.addActionListener(new EcouteurV2(this,"raise"));
            fold = new JButton("Fold");
            fold.addActionListener(new EcouteurV2(this,"fold"));
            commencerJeu = new JButton("Commencer");
            commencerJeu.addActionListener(new EcouteurV2(this, "commencer"));
            flop = new JButton("Flop");
            flop.addActionListener(new EcouteurV2(this, "flop"));
            turn = new JButton("Turn");
            turn.addActionListener(new EcouteurV2(this,"turn"));
            river = new JButton("River");
            river.addActionListener(new EcouteurV2(this,"river"));
            panelBoutons.add(commencerJeu);
            panelBoutons.add(restart);
            panelBoutons.add(call);
            panelBoutons.add(raise);
            panelBoutons.add(fold);
            panelBoutons.add(flop);
            panelBoutons.add(turn);
            panelBoutons.add(river);
            gbcPrincipal.gridx=2;
            gbcPrincipal.gridy=2;
            gbcPrincipal.anchor = GridBagConstraints.EAST;
            gbcPrincipal.insets = new Insets(0,0,20,20);
            principal.add(panelBoutons,gbcPrincipal);


        }

        else if (nJoueurs==9){
            gbcPrincipal.weighty=4;
            gbcPrincipal.weightx=4;

            //JOUEUR 0 - PRINCIPAL (HUMAIN) SUD
            gbcPrincipal.gridy = 3;
            gbcPrincipal.gridx = 1;
            gbcPrincipal.gridwidth=2;
            gbcPrincipal.anchor=GridBagConstraints.SOUTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 0);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;


            //JOUEUR 1 - OUEST
            gbcPrincipal.gridy = 2;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.gridwidth=1;
            gbcPrincipal.anchor=GridBagConstraints.WEST;
            gbcPrincipal.insets = new Insets(20, 5, 20, 0);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 2 - OUEST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.anchor=GridBagConstraints.WEST;
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 3 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(5, 20, 0, 20);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 4 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 1;
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 5 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 2;
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 6 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 3;
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 7 - EST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 3;
            gbcPrincipal.anchor=GridBagConstraints.EAST;
            gbcPrincipal.insets = new Insets(20, 0, 20, 5);
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 8 - EST
            gbcPrincipal.gridy = 2;
            gbcPrincipal.gridx = 3;
            principal.add(cartesJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;


            //CARTES TABLE
            gbcPrincipal.gridx=1;
            gbcPrincipal.gridy=1;
            gbcPrincipal.gridwidth=2;
            gbcPrincipal.gridheight=2;
            gbcPrincipal.anchor=GridBagConstraints.CENTER;
            gbcPrincipal.insets = new Insets(0,0,0,0);
            principal.add(cartesTable, gbcPrincipal);

            //JOUEUR GAGNANT
            afficherHandGagnante();
            gbcPrincipal.gridx=0;
            gbcPrincipal.gridy=3;
            gbcPrincipal.gridwidth=1;
            gbcPrincipal.gridheight=1;
            gbcPrincipal.anchor=GridBagConstraints.WEST;
            gbcPrincipal.insets = new Insets(0,10,10,0);
            principal.add(joueurGagnant, gbcPrincipal);

        }

    }

    protected void afficherHandGagnante(){
        joueurGagnant = new JPanel(new BorderLayout());

        if(jeu.joueursGagnants().size()==1){
            JPanel handGagnante = new JPanel(new FlowLayout());
            Joueur gangant = jeu.joueursGagnants().getFirst();
            for(Carte c : gangant.getHand().getCartesHand()){
                c.montrerCarte();
                handGagnante.add(new JLabel(c.icon));
                c.tournerCarte();
            }

            joueurGagnant.add(handGagnante, BorderLayout.CENTER);
            joueurGagnant.add(new JLabel(gangant.nom+" || "+gangant.getHand().getDescription(),SwingConstants.CENTER),BorderLayout.SOUTH);
        }

        else{
            String noms="<html>|| ";
            for(Joueur j : jeu.joueursGagnants()){
                noms+=j.nom+" || ";
            }
            noms+="<br/>"+jeu.joueursGagnants().getFirst().getHand().getDescription()+"</html>";
            joueurGagnant.add(new JLabel(noms, SwingConstants.CENTER), BorderLayout.CENTER);
        }

        joueurGagnant.add(new JLabel("Joueur gagnant:",SwingConstants.CENTER),BorderLayout.NORTH);

    }

    protected void flop(){
        jeu.flop();
        mettreAJourCartesTable();
    }

    protected void turn(){
        jeu.turn();
        mettreAJourCartesTable();
    }

    protected void river(){
        jeu.river();
        joueurGagnant.setVisible(true);
        mettreAJourCartesTable();
    }

    public void restart(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        new FenetreJeuV2(nJoueurs);
    }

    protected void call(){
        jeu.getHeadJoueur().parier(jeu.valeurCall);
        mettreAJourInfosJoueurs();
    }

    protected void raise(int q){
        jeu.getHeadJoueur().parier(q);
        mettreAJourInfosJoueurs();
    }

    protected void fold(){
       jeu.getHeadJoueur().fold();
       mettreAJourCartesJoueurs();
    }


    private void valeursHandTerminal(){
        Node current = jeu.getHead();
        for(int i=0; i<nJoueurs; i++){
            System.out.println(current.joueur.nom+" : "+current.joueur.getHand().getValeurHand());
            current=current.prochainNode;
        }
    }


    public static void main(String[] args){
        new FenetreJeuV2(6);
    }
}


