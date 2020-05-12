import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

public class FenetreJeuV3 extends JFrame {

    private final ImageIcon carteTournee, espaceCarteTable;
    private JPanel table, pannelBoutons,cartesTable, pannelSlider, principal, handGagnante;
    private HashMap<Joueur, JPanel> panneauxJoueurs, cartesJoueurs;
    private HashMap<Joueur, JLabel> infosJoueur, coupsJoueur;
    private HashMap<Joueur, LinkedList<ImageIcon>> iconCartesJoueurs;
    Hashtable<Integer, JLabel> valeursSlider;
    private Border raisedBevel, loweredBevel, compound;
    private JButton check, call, raise, fold, prochaineTournee;
    private JSlider raiseSlider;
    private JLabel valeurPot, valeurActuelleSlider;
    private LinkedList<ImageIcon> iconCartesTable;
    private final FlowLayout layoutCartes;
    private GridBagConstraints gbcPrincipal;
    private Jeu jeu;
    private final Color blanc = new Color(255,255,255);
    private final Color bordeauxTable = new Color(82,3,3);
    private final Color jauneCoups = new Color(255,230,77);
    private final Color noirFond = new Color(23,22,21);
    private final Color rougeHorsJeu = new Color(232, 81, 81);

    public FenetreJeuV3(Jeu jeu){
        super("Poker V3");
        setSize(1300, 650);

        this.jeu = jeu;

        //Initialisation des attributs
        panneauxJoueurs = new HashMap<>();
        infosJoueur = new HashMap<>();
        table = new JPanel(new BorderLayout());
        table.setBackground(bordeauxTable);
        raisedBevel = BorderFactory.createSoftBevelBorder(BevelBorder.RAISED);
        loweredBevel = BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED);
        compound = BorderFactory.createCompoundBorder(raisedBevel, loweredBevel);
        table.setBorder(compound);

        pannelBoutons = new JPanel(new BorderLayout());
        pannelBoutons.setOpaque(false);
        layoutCartes = new FlowLayout();
        layoutCartes.setHgap(10);
        cartesTable = new JPanel(layoutCartes);
        cartesTable.setOpaque(false);
        handGagnante = new JPanel(new BorderLayout());
        handGagnante.setOpaque(false);
        cartesJoueurs = new HashMap<>();
        coupsJoueur = new HashMap<>();
        iconCartesJoueurs = new HashMap<>();
        iconCartesTable = new LinkedList<>();
        valeurPot= new JLabel("",SwingConstants.CENTER);
        valeurPot.setForeground(blanc);
        raiseSlider = new JSlider();
        raiseSlider.setOpaque(false);
        valeurActuelleSlider = new JLabel("",SwingConstants.CENTER);
        valeurActuelleSlider.setForeground(blanc);
        pannelSlider = new JPanel(new BorderLayout());
        pannelSlider.setOpaque(false);
        pannelSlider.add(valeurActuelleSlider,BorderLayout.SOUTH);
        pannelSlider.add(raiseSlider,BorderLayout.CENTER);
        pannelBoutons.add(pannelSlider, BorderLayout.SOUTH);
        mettreAJourRaiseSlider();

        jeu.getJoueurs().getJoueurs().forEach(joueur ->
            {
                JPanel pcj = new JPanel(layoutCartes);
                pcj.setOpaque(false);
                cartesJoueurs.put(joueur, pcj);
                JLabel ij = new JLabel("",SwingConstants.CENTER);
                ij.setForeground(blanc);
                infosJoueur.put(joueur,ij);
                JLabel cj = new JLabel("", SwingConstants.CENTER);
                cj.setForeground(jauneCoups);
                coupsJoueur.put(joueur, cj);
                JPanel pj = new JPanel(new BorderLayout());
                pj.setOpaque(false);
                panneauxJoueurs.put(joueur, pj);
                iconCartesJoueurs.put(joueur, new LinkedList<>());
            }
        );
        gbcPrincipal = new GridBagConstraints();

        //Initialisation des boutons et ajout du écouteur
        prochaineTournee=new JButton("Redistribuer Cartes");
        prochaineTournee.setBackground(jauneCoups);
        prochaineTournee.setBorder(raisedBevel);
        prochaineTournee.setForeground(blanc);
        prochaineTournee.addActionListener(new EcouteurV2(this,"ProchaineTournee"));
        prochaineTournee.setVisible(false);
        check = new JButton("Check");
        check.setBorder(raisedBevel);
        check.setForeground(blanc);
        check.setBackground(bordeauxTable);
        check.addActionListener(new EcouteurV2(this, "Check"));
        call = new JButton("Call");
        call.setBorder(raisedBevel);
        call.setForeground(blanc);
        call.setBackground(bordeauxTable);
        call.addActionListener(new EcouteurV2(this, "Call"));
        raise = new JButton("Raise "+2*jeu.pariActuel);
        raise.setBackground(bordeauxTable);
        raise.setBorder(raisedBevel);
        raise.setForeground(blanc);
        raise.addActionListener(new EcouteurV2(this, "Raise"));
        fold = (new JButton("Fold"));
        fold.setBackground(bordeauxTable);
        fold.setBorder(raisedBevel);
        fold.setForeground(blanc);
        fold.addActionListener(new EcouteurV2(this, "Fold"));
        pannelBoutons.add(call, BorderLayout.WEST);
        pannelBoutons.add(raise,BorderLayout.CENTER);
        pannelBoutons.add(fold,BorderLayout.EAST);
        table.add(prochaineTournee,BorderLayout.NORTH);
        pannelBoutons.setVisible(false);


        //Création de l'icone de la carte tournée
        carteTournee = Carte.redimensioner(84, 112, new ImageIcon(getClass().getResource("res/back.png")));
        //Création de l'espace de la carte sur la table
        espaceCarteTable = Carte.redimensioner(84, 112, new ImageIcon(getClass().getResource("res/espace_carte_table.jpg")));
        //Création de l'espace de la carte sur la main du joueur
        //espaceCarteJoueur = Carte.redimensioner(84,112, new ImageIcon(getClass().getResource("res/espace_carte_joueur.jpg")));


        gbcPrincipal = new GridBagConstraints();
        principal = new JPanel(new GridBagLayout());
        principal.setBackground(noirFond);

        creerLayout();

        this.add(principal);
        this.setVisible(true);
    }

    public void nouvelleTournee() throws Exception {
        jeu.prochaineTournee();

    }

    public void mettreAJourRaiseSlider(){
        raiseSlider.removeAll();
        valeursSlider= new Hashtable<>();
        if(jeu.pariActuel==0){
            JLabel min = new JLabel("20");
            min.setForeground(blanc);
            raiseSlider.setMinimum(20);
            valeursSlider.put(20,min);
        }
        else {
            JLabel min = new JLabel(""+raiseSlider.getMinimum());
            min.setForeground(blanc);
            raiseSlider.setMinimum(jeu.pariActuel*2);
            valeursSlider.put(jeu.pariActuel*2,min);
        }
        raiseSlider.setMaximum(jeu.getJoueurHumain().getArgent());
        JLabel max = new JLabel(""+raiseSlider.getMaximum());
        max.setForeground(blanc);
        valeursSlider.put(raiseSlider.getMaximum(),max);
        raiseSlider.setLabelTable(valeursSlider);
        raiseSlider.setMinorTickSpacing(10);
        raiseSlider.setMajorTickSpacing(500);
        raiseSlider.setSnapToTicks(true);
        raiseSlider.setValue(raiseSlider.getMinimum());
        raiseSlider.setPaintLabels(true);
        raiseSlider.addChangeListener(e -> {
            String text;
            if (raiseSlider.getValue()==jeu.getJoueurHumain().getArgent()){
                text="All in: "+raiseSlider.getValue();
                valeurActuelleSlider.setText(text);
            }
            else {
                text="Raise: " + raiseSlider.getValue();
                valeurActuelleSlider.setText(text);
            }
            raise.setText(text);
        });

    }

    public void creerTable(){
        iconCartesTable.addAll(jeu.getIconCartesTable());
        for(int i=0; i<5; i++){
            cartesTable.add(new JLabel(espaceCarteTable));
        }
        table.add(cartesTable, BorderLayout.CENTER);
        valeurPot.setText("Pot : "+jeu.potActuel);
        table.add(valeurPot, BorderLayout.SOUTH);
    }

    public void mettreAJourValuerPot(){
        valeurPot.setText("Pot : "+jeu.potActuel+" || Pari : "+jeu.pariActuel);
    }


    public void creerPanneauxJoueurs(){
        Node current = jeu.getJoueurs().getNodeHumain();
        for (int i=0; i<6; i++){
            Joueur j = current.joueur;
            if(j.dansJeu) {
                j.getCartesSurMain().forEach(Carte -> {
                            iconCartesJoueurs.get(j).add(Carte.icon);
                            cartesJoueurs.get(j).add(new JLabel(carteTournee));
                        }
                );
                if (j.dealer) {
                    infosJoueur.get(j).setText(j.nom + " || Argent: " + j.getArgent() + "|| Dealer");
                } else if (j.smallBlind) {
                    infosJoueur.get(j).setText(j.nom + " || Argent: " + j.getArgent() + "|| Small Blind");
                } else if (j.bigBlind) {
                    infosJoueur.get(j).setText(j.nom + " || Argent: " + j.getArgent() + "|| Big Blind");
                } else {
                    infosJoueur.get(j).setText(j.nom + " || Argent: " + j.getArgent());
                }
                coupsJoueur.get(j).setText(j.coup);
                panneauxJoueurs.get(j).add(cartesJoueurs.get(j), BorderLayout.NORTH);
                panneauxJoueurs.get(j).add(infosJoueur.get(j), BorderLayout.CENTER);
                panneauxJoueurs.get(j).add(coupsJoueur.get(j), BorderLayout.SOUTH);
            }
            else {
                JPanel plusDArgent = new JPanel();
                plusDArgent.setOpaque(false);
                JLabel hj = new JLabel(j.nom+" || Hors jeu");
                hj.setForeground(rougeHorsJeu);
                plusDArgent.add(hj);
                panneauxJoueurs.get(j).add(plusDArgent);
            }
            current=current.prochainNode;
        }
    }

    public void montrerCartesJoueur(Joueur j){
        cartesJoueurs.get(j).removeAll();
        j.getCartesSurMain().forEach(carte -> cartesJoueurs.get(j).add(new JLabel(carte.icon)));
    }

    public void enleverCartesJoueur(Joueur j){
        cartesJoueurs.get(j).setVisible(false);
    }

    public void mettreAJourInfosJoueur(Joueur j){
        coupsJoueur.get(j).setText(j.coup);
        if(j.dealer && j.bigBlind) {
            infosJoueur.get(j).setText(j.nom + " || Argent: "+j.getArgent()+" || Dealer - BB");
        }
        else if(j.dealer){
            infosJoueur.get(j).setText(j.nom + " || Argent: "+j.getArgent()+" || Dealer");
        }
        else if (j.bigBlind){
            infosJoueur.get(j).setText(j.nom + " || Argent: "+j.getArgent()+" || BigBlind");
        }
        else if (j.smallBlind){
            infosJoueur.get(j).setText(j.nom + " || Argent: "+j.getArgent()+" || SmallBlind");
        }
        else {
            infosJoueur.get(j).setText(j.nom + " || Argent: "+j.getArgent());
        }
    }

    public void creerHandGagnant(boolean tousFold, String descriptionPot){
        if(tousFold){
            JLabel tf = new JLabel(jeu.getDernierDansLaTournee().nom+" prend le pot.", SwingConstants.CENTER);
            tf.setForeground(blanc);
            handGagnante.add(tf, BorderLayout.CENTER);
        }
        else {
            assert !jeu.getJoueursGagnants().isEmpty() : "Liste de joueurs gagnants vide";
            StringBuilder infosHandGagnante = new StringBuilder();
            if (jeu.getJoueursGagnants().size() == 1) {
                JPanel cartesHandGagnante = new JPanel(layoutCartes);
                cartesHandGagnante.setOpaque(false);
                Joueur gagnant = jeu.getJoueursGagnants().getFirst();
                gagnant.getCartesHand().forEach(carte -> cartesHandGagnante.add(new JLabel(carte.icon)));
                infosHandGagnante.append(gagnant.nom).append(" || ").append(gagnant.getHand().getDescription());
                handGagnante.add(cartesHandGagnante, BorderLayout.NORTH);
                JLabel ihg = new JLabel(infosHandGagnante.toString(), SwingConstants.CENTER);
                ihg.setForeground(blanc);
                handGagnante.add(ihg, BorderLayout.CENTER);
                JLabel dp = new JLabel(descriptionPot, SwingConstants.CENTER);
                dp.setForeground(jauneCoups);
                handGagnante.add(dp, BorderLayout.SOUTH);
            } else {
                for (Joueur j : jeu.getJoueursGagnants()) {
                    infosHandGagnante.append(j.nom).append(" || ");
                }
                infosHandGagnante.append(jeu.getJoueursGagnants().getFirst().getHand().getDescription());
                JLabel ihg = new JLabel(infosHandGagnante.toString());
                ihg.setForeground(blanc);
                handGagnante.add(ihg,BorderLayout.CENTER);
                JLabel dp = new JLabel(descriptionPot,SwingConstants.CENTER);
                dp.setForeground(jauneCoups);
                handGagnante.add(dp, BorderLayout.SOUTH);
            }
        }

    }

    public void afficherHandGagnante(boolean tousFold, String descriptionPot){
        creerHandGagnant(tousFold, descriptionPot);
        handGagnante.setVisible(true);
    }

    public void effacerCoupsJoueur(){
        coupsJoueur.values().forEach(label -> label.setText(""));
    }

    public void creerLayout(){
        creerTable();
        creerPanneauxJoueurs();

        Node current = jeu.getJoueurs().getNodeHumain();
            //Joueur 0 - humain
            gbcPrincipal.gridx = 2;
            gbcPrincipal.gridy = 2;
            gbcPrincipal.weightx=1;
            gbcPrincipal.weighty=1;
            gbcPrincipal.anchor=GridBagConstraints.SOUTH;
            gbcPrincipal.insets = new Insets(0, 0, 5, 0);
            principal.add(panneauxJoueurs.get(current.joueur), gbcPrincipal);
            montrerCartesJoueur(current.joueur); //Montre les cartes du joueur humain
            current = current.prochainNode;

            //Joueur 1 - ouest
            gbcPrincipal.gridx = 0;
            gbcPrincipal.gridy = 1;
            gbcPrincipal.anchor=GridBagConstraints.WEST;
            gbcPrincipal.insets = new Insets(0, 10, 0, 0);
            principal.add(panneauxJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;

            //Joueur 2 - nord
            gbcPrincipal.gridx = 0;
            gbcPrincipal.gridy = 0;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(5, 20, 0, 0);
            principal.add(panneauxJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;

            //Joueur 3 - nord
            gbcPrincipal.gridx = 1;
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridwidth = 3;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(5, 0, 0, 0);
            principal.add(panneauxJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;

            //Joueur 4 - nord
            gbcPrincipal.gridx = 4;
            gbcPrincipal.gridy = 0;
            gbcPrincipal.gridwidth = 1;
            gbcPrincipal.anchor=GridBagConstraints.NORTH;
            gbcPrincipal.insets = new Insets(5, 0, 0, 20);
            principal.add(panneauxJoueurs.get(current.joueur), gbcPrincipal);
            current = current.prochainNode;

            //Joueur 5 - est
            gbcPrincipal.gridx = 4;
            gbcPrincipal.gridy = 1;
            gbcPrincipal.anchor=GridBagConstraints.EAST;
            gbcPrincipal.insets = new Insets(0, 0, 0, 10);
            principal.add(panneauxJoueurs.get(current.joueur), gbcPrincipal);

            //Table - centre
            gbcPrincipal.gridx = 1;
            gbcPrincipal.gridy = 1;
            gbcPrincipal.gridwidth = 3;
            gbcPrincipal.anchor=GridBagConstraints.CENTER;
            gbcPrincipal.insets = new Insets(0, 0, 0, 0);
            principal.add(table, gbcPrincipal);

            //Pannel boutons - bas
            gbcPrincipal.gridx = 3;
            gbcPrincipal.gridy = 2;
            gbcPrincipal.gridwidth = 2;
            gbcPrincipal.insets = new Insets(0, 0, 10, 10);
            gbcPrincipal.anchor=GridBagConstraints.SOUTHEAST;
            principal.add(pannelBoutons, gbcPrincipal);

            //Hand gagnante - bas
            gbcPrincipal.gridx = 0;
            gbcPrincipal.gridy = 2;
            gbcPrincipal.anchor=GridBagConstraints.SOUTHWEST;
        gbcPrincipal.insets = new Insets(0, 10, 10, 0);
            handGagnante.setVisible(false);
            principal.add(handGagnante, gbcPrincipal);
    }

    public void foldJoueurHumain(){
        cartesJoueurs.get(jeu.getJoueurHumain()).removeAll();
        cartesJoueurs.get(jeu.getJoueurHumain()).add(new JLabel(carteTournee));
        cartesJoueurs.get(jeu.getJoueurHumain()).add(new JLabel(carteTournee));
    }

    public void flop(){
        cartesTable.removeAll();
        cartesTable.add(new JLabel(iconCartesTable.get(0)));
        cartesTable.add(new JLabel(iconCartesTable.get(1)));
        cartesTable.add(new JLabel(iconCartesTable.get(2)));
        cartesTable.add(new JLabel(espaceCarteTable));
        cartesTable.add(new JLabel(espaceCarteTable));
        revalidate();
        repaint();
    }

    public void turn(){
        cartesTable.removeAll();
        cartesTable.add(new JLabel(iconCartesTable.get(0)));
        cartesTable.add(new JLabel(iconCartesTable.get(1)));
        cartesTable.add(new JLabel(iconCartesTable.get(2)));
        cartesTable.add(new JLabel(iconCartesTable.get(3)));
        cartesTable.add(new JLabel(espaceCarteTable));
        revalidate();
        repaint();
    }

    public void river(){
        cartesTable.removeAll();
        iconCartesTable.forEach(ImageIcon -> cartesTable.add(new JLabel(ImageIcon)));
        revalidate();
        repaint();
    }

    public void afficherBoutons(boolean afficher){
        mettreAJourRaiseSlider();
        if((jeu.pariActuel-jeu.getJoueurHumain().derniereValeurPariee)==0) {
            call.setText("Check");
        }
        else {
            call.setText("Call: "+(jeu.pariActuel - jeu.getJoueurHumain().derniereValeurPariee));
        }
        pannelBoutons.setVisible(afficher);
    }

    public void setActionJoueurHumain(int action) throws Exception {
        jeu.getJoueurHumain().setActionJoueurHumain(action, jeu.pariActuel, jeu);
        afficherBoutons(false);
    }

    public void afficherBoutonProchaineTournee(){
        prochaineTournee.setVisible(true);
    }


    public int getValeurRaiseSlider(){
        return raiseSlider.getValue();
    }

}
