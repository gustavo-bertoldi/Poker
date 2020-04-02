import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.LinkedList;

public class FenetreJeuV2 extends JFrame {

    private ImageIcon carteTournée;

    private JPanel table, principal, joueurGagnant, panelBoutons, cartesTourneesJoueur;

    private HashMap<Joueur, JPanel> cartesEtInfosJoueurs, cartesJoueurs;

    private HashMap<Joueur, JLabel> infosJoueur, coupsJoueur;

    private LinkedList<ImageIcon> cartesTable; //LinkedList avec l'icon de chacune des cartes dans la table

    private JButton call, raise, fold, commencerJeu, restart, flop, turn, river;

    private FlowLayout layoutCartes;

    private GridBagConstraints gbcPrincipal, gbcCartesJoueurs, gbcTable;

    private int nJoueurs;

    private Jeu jeu;

    private FenetreJeuV2 (int nJoueurs){
        super("Poker V2");
        setSize(1400, 650);

        this.nJoueurs = nJoueurs;
        jeu = new Jeu(nJoueurs, 0);

        layoutCartes = new FlowLayout();
        layoutCartes.setHgap(10);

        //Création du panel avec les cartes d'un joueur tournées
        carteTournée = new ImageIcon("src/res/back.png");
        carteTournée = Carte.redimensioner(84,112,carteTournée);
        cartesTourneesJoueur = new JPanel(layoutCartes);
        cartesTourneesJoueur.add(new JLabel(carteTournée));
        cartesTourneesJoueur.add(new JLabel(carteTournée));


        gbcPrincipal = new GridBagConstraints();
        gbcCartesJoueurs = new GridBagConstraints();
        principal = new JPanel(new GridBagLayout());
        this.add(principal);
        creerLayout();

        valeursHandTerminal();

        this.setVisible(true);
    }

    private void ajouterTable(){
        table = new JPanel(new GridBagLayout());
        cartesTable = new LinkedList<>();
        gbcTable = new GridBagConstraints();
        gbcTable.gridy=0;
        gbcTable.insets = new Insets(0,5,0,5);
        int i=0;
        for (int j=0; j<5; j++){
            gbcTable.gridx=i;
            table.add(new JLabel(carteTournée), gbcTable);
            cartesTable.add(jeu.getCartesTable().get(i).icon);
            i++;
        }
    }

    private void ajouterCoupsJoueurs(){
        Node current = jeu.getHead();
        coupsJoueur=new HashMap<>();

        gbcTable.gridx=0;
        gbcTable.gridy=1;
        gbcTable.gridwidth=5;
        table.add(new JLabel("Coups : "), gbcTable);

        int gbcGridY=2;

        for (int i=0;i<nJoueurs;i++){
            gbcTable.gridy=gbcGridY;
            coupsJoueur.put(current.joueur,new JLabel(current.joueur.nom+" || "+current.joueur.coup));
            table.add(coupsJoueur.get(current.joueur));
            current=current.prochainNode;
            gbcGridY++;
        }

    }

    private void ajouterCartesEtInfosJoueurs(){
        Node current = jeu.getHead();
        cartesJoueurs = new HashMap<>();
        infosJoueur = new HashMap<>();
        cartesEtInfosJoueurs = new HashMap<>();

        for(int i=0; i<nJoueurs; i++){
            cartesEtInfosJoueurs.put(current.joueur,new JPanel(new BorderLayout()));
            cartesJoueurs.put(current.joueur, new JPanel(layoutCartes));

            for(Carte c : current.joueur.getCartesSurMain()){
                cartesJoueurs.get(current.joueur).add(new JLabel(c.icon));
            }

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

            cartesEtInfosJoueurs.get(current.joueur).add(cartesJoueurs.get(current.joueur),BorderLayout.CENTER);
            cartesEtInfosJoueurs.get(current.joueur).add(infosJoueur.get(current.joueur), BorderLayout.SOUTH);

            current = current.prochainNode;
        }
    }

    private void mettreAJourInfosJoueurs(){
        Node current = jeu.getHead();
        for(int i=0;i<nJoueurs;i++){
            if(current.joueur.isDealer()){
                infosJoueur.get(current.joueur).setText(current.joueur.nom+" || Argent: "+current.joueur.getArgent()+" || Dealer");
            }
            else if(current.joueur.isBigBlind()){
                infosJoueur.get(current.joueur).setText(current.joueur.nom+" || Argent: "+current.joueur.getArgent()+" || BigBlind");
            }
            else if(current.joueur.isSmallBlind()){
                infosJoueur.get(current.joueur).setText(current.joueur.nom+" || Argent: "+current.joueur.getArgent()+" || SmallBlind");
            }
            else {
                infosJoueur.get(current.joueur).setText(current.joueur.nom + " || Argent: " + current.joueur.getArgent());
            }
            current=current.prochainNode;
        }

        revalidate();
        repaint();
    }

    public void creerLayout(){
        Node current = jeu.getHead();
        ajouterCartesEtInfosJoueurs();
        ajouterTable();
        gbcPrincipal = new GridBagConstraints();

        if(nJoueurs==6) {
            gbcPrincipal.weighty=3;
            gbcPrincipal.weightx=3;

            //JOUEUR 0 - PRINCIPAL (HUMAIN) SUD
            gbcPrincipal.gridy = 2;
            gbcPrincipal.gridx = 1;
            gbcPrincipal.anchor=GridBagConstraints.SOUTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 0);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 1 - OUEST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.anchor=GridBagConstraints.WEST;
            gbcPrincipal.insets = new Insets(0, 5, 0, 0);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;

            //JOUEUR 2 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(0, 30, 5, 0);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 3 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 1;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 0);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 4 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 2;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 50);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 5 - EST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 2;
            gbcPrincipal.anchor=GridBagConstraints.EAST;
            gbcPrincipal.insets = new Insets(0, 0, 0, 5);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //TABLE - CENTRE
            gbcPrincipal.gridx=1;
            gbcPrincipal.gridy=1;
            gbcPrincipal.anchor=GridBagConstraints.CENTER;
            gbcPrincipal.insets = new Insets(0, 0, 0, 0);
            principal.add(table, gbcPrincipal);
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
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;


            //JOUEUR 1 - OUEST
            gbcPrincipal.gridy = 2;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.gridwidth=1;
            gbcPrincipal.anchor=GridBagConstraints.WEST;
            gbcPrincipal.insets = new Insets(20, 5, 20, 0);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 2 - OUEST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.anchor=GridBagConstraints.WEST;
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 3 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(5, 20, 0, 20);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 4 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 1;
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 5 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 2;
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 6 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 3;
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 7 - EST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 3;
            gbcPrincipal.anchor=GridBagConstraints.EAST;
            gbcPrincipal.insets = new Insets(20, 0, 20, 5);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;

            //JOUEUR 8 - EST
            gbcPrincipal.gridy = 2;
            gbcPrincipal.gridx = 3;
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);

            current = current.prochainNode;


            //CARTES TABLE
            gbcPrincipal.gridx=1;
            gbcPrincipal.gridy=1;
            gbcPrincipal.gridwidth=2;
            gbcPrincipal.gridheight=2;
            gbcPrincipal.anchor=GridBagConstraints.CENTER;
            gbcPrincipal.insets = new Insets(0,0,0,0);
            principal.add(table, gbcPrincipal);

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
        System.out.println("Cartes Hand Gagnante");
        if(jeu.joueursGagnants().size()==1){
            JPanel handGagnante = new JPanel(layoutCartes);
            Joueur gangant = jeu.joueursGagnants().getFirst();
            for(Carte c : gangant.getHand().getCartesHand()){
                System.out.println(c.toString());
                handGagnante.add(new JLabel(c.icon));
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
        table.removeAll();
        for(int i=0;i<3;i++) {
            gbcTable.gridx = i;
            table.add(new JLabel(jeu.getCartesTable().get(i).icon), gbcTable);
        }
        gbcTable.gridx = 3;
        table.add(new JLabel(carteTournée), gbcTable);
        gbcTable.gridx = 4;
        table.add(new JLabel(carteTournée), gbcTable);
        revalidate();
        repaint();
    }

    protected void turn(){
        table.removeAll();
        for(int i=0;i<4;i++) {
            gbcTable.gridx = i;
            table.add(new JLabel(jeu.getCartesTable().get(i).icon), gbcTable);
        }
        gbcTable.gridx = 4;
        table.add(new JLabel(carteTournée), gbcTable);
        revalidate();
        repaint();
    }

    protected void river(){
        table.removeAll();
        for(int i=0;i<5;i++) {
            gbcTable.gridx = i;
            table.add(new JLabel(jeu.getCartesTable().get(i).icon), gbcTable);
        }
        joueurGagnant.setVisible(true);

        ajouterCoupsJoueurs();

        revalidate();
        repaint();
    }

    public void restart(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        new FenetreJeuV2(nJoueurs);
    }

    protected void call(){
        jeu.getHeadJoueur().parier(jeu.valeurCall);
    }

    protected void raise(int q){
        jeu.getHeadJoueur().parier(q);
    }

    protected void fold(){
        cartesJoueurs.get(jeu.getHeadJoueur()).removeAll();
        cartesJoueurs.get(jeu.getHeadJoueur()).add(new JLabel(carteTournée));
        cartesJoueurs.get(jeu.getHeadJoueur()).add(new JLabel(carteTournée));
        revalidate();
        repaint();
    }

    protected void changerDealer(){
        jeu.changerDealer();
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


