import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.LinkedList;

public class FenetreJeuV2 extends JFrame {

    private ImageIcon carteTournee;

    private JPanel table, principal, joueurGagnant, panelBoutons, cartesTourneesJoueur;

    private HashMap<Joueur, JPanel> cartesEtInfosJoueurs, cartesJoueurs;

    private HashMap<Joueur, JLabel> infosJoueur, coupsJoueur;

    private LinkedList<JLabel> cartesTable; //LinkedList avec l'icon de chacune des cartes dans la table

    private JButton call, raise, fold, avancerJeu, restart, flop, turn, river;

    private FlowLayout layoutCartes;

    private GridBagConstraints gbcPrincipal, gbcTable;

    private int nJoueurs;

    protected Jeu jeu;

    public FenetreJeuV2 (int nJoueurs){
        super("Poker V2");
        setSize(1300, 650);

        this.nJoueurs = nJoueurs;
        jeu = new Jeu(nJoueurs, 0, this);

        joueurGagnant=new JPanel(new BorderLayout());

        layoutCartes = new FlowLayout();
        layoutCartes.setHgap(10);

        //Création du panel avec les cartes d'un joueur tournées
        carteTournee = new ImageIcon("src/res/back.png");
        carteTournee = Carte.redimensioner(84,112,carteTournee);
        cartesTourneesJoueur = new JPanel(layoutCartes);
        cartesTourneesJoueur.add(new JLabel(carteTournee));
        cartesTourneesJoueur.add(new JLabel(carteTournee));


        gbcPrincipal = new GridBagConstraints();
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
            cartesTable.add(new JLabel(carteTournee));
            table.add(cartesTable.get(i), gbcTable);
            i++;
        }
    }



    private void ajouterCartesEtInfosJoueurs(){
        Node current = jeu.getHead();
        cartesJoueurs = new HashMap<>();
        infosJoueur = new HashMap<>();
        cartesEtInfosJoueurs = new HashMap<>();
        coupsJoueur=new HashMap<>();

        for(int i=0; i<nJoueurs; i++){
            cartesEtInfosJoueurs.put(current.joueur,new JPanel(new BorderLayout()));
            cartesJoueurs.put(current.joueur, new JPanel(layoutCartes));
            coupsJoueur.put(current.joueur, new JLabel("",SwingConstants.CENTER));

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

            cartesEtInfosJoueurs.get(current.joueur).add(cartesJoueurs.get(current.joueur),BorderLayout.NORTH);
            cartesEtInfosJoueurs.get(current.joueur).add(infosJoueur.get(current.joueur), BorderLayout.CENTER);
            cartesEtInfosJoueurs.get(current.joueur).add(coupsJoueur.get(current.joueur), BorderLayout.SOUTH);

            current = current.prochainNode;
        }
    }

    public void nouvelleTournee(){
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
            coupsJoueur.get(current.joueur).setText("");
            current=current.prochainNode;
        }

        revalidate();
        repaint();
    }

    /*
    Méthode appelée lors quand un joueur a joué son tour et ses informations on changé (Argent, coup)
     */
    public void mettreAJourInfosJoueur(Joueur j){
        if(j.isDealer()){
            infosJoueur.get(j).setText(j.nom+" || Argent: "+j.getArgent()+" || Dealer");
        }
        else if(j.isBigBlind()){
            infosJoueur.get(j).setText(j.nom+" || Argent: "+j.getArgent()+" || BigBlind");
        }
        else if(j.isSmallBlind()){
            infosJoueur.get(j).setText(j.nom+" || Argent: "+j.getArgent()+" || SmallBlind");
        }
        else {
            infosJoueur.get(j).setText(j.nom + " || Argent: " + j.getArgent());
        }
        coupsJoueur.get(j).setText(j.coup);
        revalidate();
        repaint();
    }

    public void creerHandGagnante(LinkedList<Joueur> joueursGagnants, boolean tousFold){

        System.out.println("\nHand Gagnante:");
        if(tousFold){
            joueurGagnant.add(new JLabel(joueursGagnants.getFirst().nom+" prend le pot."));
        }
        else {
            if (joueursGagnants.size() == 1) {
                JPanel handGagnante = new JPanel(layoutCartes);
                Joueur gagnant = joueursGagnants.getFirst();
                System.out.println(gagnant.getHand().getDescription());
                for (Carte c : gagnant.getHand().getCartesHand()) {
                    handGagnante.add(new JLabel(c.icon));
                    System.out.println(c.toString());
                }

                joueurGagnant.add(handGagnante, BorderLayout.CENTER);
                joueurGagnant.add(new JLabel(gagnant.nom + " || " + gagnant.getHand().getDescription(), SwingConstants.CENTER), BorderLayout.SOUTH);
            } else {
                StringBuilder noms = new StringBuilder("<html>|| ");
                for (Joueur j : joueursGagnants) {
                    noms.append(j.nom).append(" || ");
                }
                noms.append("<br/>").append(joueursGagnants.getFirst().getHand().getDescription()).append("</html>");
                joueurGagnant.add(new JLabel(noms.toString(), SwingConstants.CENTER), BorderLayout.CENTER);
                System.out.println(joueursGagnants.getFirst().getHand().getDescription());
            }
        }

        System.out.println("\n");

    }

    public void creerLayout(){
        Node current = jeu.getHead();
        ajouterCartesEtInfosJoueurs();
        ajouterTable();
        gbcPrincipal = new GridBagConstraints();

        if(nJoueurs==6) {
            gbcPrincipal.weightx=1;
            gbcPrincipal.weighty=1;

            //JOUEUR 0 - PRINCIPAL (HUMAIN) SUD
            gbcPrincipal.gridy = 2;
            gbcPrincipal.gridx = 2;
            gbcPrincipal.gridwidth=1;
            gbcPrincipal.anchor=GridBagConstraints.SOUTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 0);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 1 - OUEST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.weightx=0;
            gbcPrincipal.anchor=GridBagConstraints.WEST;
            gbcPrincipal.insets = new Insets(0, 5, 0, 0);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;

            //JOUEUR 2 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 0;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(0, 10, 5, 0);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 3 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 1;
            gbcPrincipal.gridwidth=3;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(0, 30, 5, 30);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 4 - NORD
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridx = 4;
            gbcPrincipal.gridwidth=1;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 10);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;


            //JOUEUR 5 - EST
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridx = 4;
            gbcPrincipal.anchor=GridBagConstraints.EAST;
            gbcPrincipal.insets = new Insets(0, 0, 0, 5);
            principal.add(cartesEtInfosJoueurs.get(current.joueur), gbcPrincipal);


            //TABLE - CENTRE
            gbcPrincipal.gridx=1;
            gbcPrincipal.gridy=1;
            gbcPrincipal.gridwidth=3;
            gbcPrincipal.anchor=GridBagConstraints.CENTER;
            gbcPrincipal.insets = new Insets(0, 0, 0, 0);
            principal.add(table, gbcPrincipal);

            //JOUEUR GAGNANT
            gbcPrincipal.gridx=0;
            gbcPrincipal.gridy=2;
            gbcPrincipal.gridwidth=2;
            gbcPrincipal.anchor=GridBagConstraints.SOUTHWEST;
            joueurGagnant.setVisible(false);
            principal.add(joueurGagnant, gbcPrincipal);




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
            avancerJeu = new JButton("Commencer");
            avancerJeu.addActionListener(new EcouteurV2(this, "commencer"));
            flop = new JButton("Flop");
            flop.addActionListener(new EcouteurV2(this, "flop"));
            turn = new JButton("Turn");
            turn.addActionListener(new EcouteurV2(this,"turn"));
            river = new JButton("River");
            river.addActionListener(new EcouteurV2(this,"river"));
            panelBoutons.add(avancerJeu);
            panelBoutons.add(restart);
            panelBoutons.add(call);
            panelBoutons.add(raise);
            panelBoutons.add(fold);
            panelBoutons.add(flop);
            panelBoutons.add(turn);
            panelBoutons.add(river);
            gbcPrincipal.gridx=3;
            gbcPrincipal.gridy=2;
            gbcPrincipal.gridwidth=2;
            gbcPrincipal.anchor=GridBagConstraints.SOUTHEAST;
            gbcPrincipal.insets = new Insets(0,0,10,10);
            principal.add(panelBoutons,gbcPrincipal);


        }

        else if (nJoueurs==9){
            gbcPrincipal.weighty=1;
            gbcPrincipal.weightx=1;

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


            //CARTES TABLE
            gbcPrincipal.gridx=1;
            gbcPrincipal.gridy=1;
            gbcPrincipal.gridwidth=2;
            gbcPrincipal.gridheight=2;
            gbcPrincipal.anchor=GridBagConstraints.CENTER;
            gbcPrincipal.insets = new Insets(0,0,0,0);
            principal.add(table, gbcPrincipal);



        }

    }

    protected void flop(){
        for(int i=0;i<3;i++) {
            cartesTable.get(i).setIcon(jeu.getCartesTable().get(i).icon);
        }
        revalidate();
        repaint();
    }

    protected void turn(){
        cartesTable.get(3).setIcon(jeu.getCartesTable().get(3).icon);
        revalidate();
        repaint();
    }

    protected void afficherHandGagnante(LinkedList<Joueur> joueursGagnants, boolean tousFold){
        creerHandGagnante(joueursGagnants, tousFold);
        joueurGagnant.setVisible(true);
        revalidate();
        repaint();
    }

    protected void river(){
        cartesTable.get(4).setIcon(jeu.getCartesTable().get(4).icon);

        revalidate();
        repaint();
    }

    public void restart(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        new FenetreJeuV2(nJoueurs);
    }

    protected void avancerJeu(){
        jeu.commencerTournee();

    }

    protected void foldJoueurHumain(){
        cartesJoueurs.get(jeu.getHeadJoueur()).removeAll();
        cartesJoueurs.get(jeu.getHeadJoueur()).add(new JLabel(carteTournee));
        cartesJoueurs.get(jeu.getHeadJoueur()).add(new JLabel(carteTournee));
        revalidate();
        repaint();
    }

    protected void changerDealer(){
        long tLimite = System.currentTimeMillis() + 5000;
        long t = System.currentTimeMillis();
        while(t<tLimite){
            t=System.currentTimeMillis();
        }
        //jeu.changerDealer();
    }

    private void valeursHandTerminal(){
        Node current = jeu.getHead();
        for(int i=0; i<nJoueurs; i++){
            System.out.println(current.joueur.nom+" : "+current.joueur.getHand().getValeurHand());
            current=current.prochainNode;
        }
    }

    protected void sortirJoueur(Joueur j){
        jeu.sortirJoueur(j);
        nJoueurs--;
        cartesEtInfosJoueurs.get(j).setVisible(false);
    }


    public static void main(String[] args){
        FenetreJeuV2 f = new FenetreJeuV2(6);

    }
}


