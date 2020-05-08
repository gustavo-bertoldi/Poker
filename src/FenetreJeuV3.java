import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

public class FenetreJeuV3 extends JFrame {

    private final ImageIcon carteTournee;
    private final ImageIcon espaceCarte;
    private JPanel table, pannelBoutons,cartesTable, pannelSlider, principal, handGagnante, infosJeu;
    private Border raisedBevel, loweredBevel, compound;
    private HashMap<Joueur, JPanel> panneauxJoueurs, cartesJoueurs;
    private HashMap<Joueur, JLabel> infosJoueur, coupsJoueur;
    private HashMap<Joueur, LinkedList<ImageIcon>> iconCartesJoueurs;
    Hashtable<Integer, JLabel> valeursSlider;
    private JButton check, call, raise, fold, restart, prochaineTournee;
    private JSlider raiseSlider;
    private JLabel valeurPot, valeurActuelleSlider;
    private LinkedList<ImageIcon> iconCartesTable;
    private final FlowLayout layoutCartes;
    private GridBagConstraints gbcPrincipal;
    private Jeu jeu;

    public FenetreJeuV3(Jeu jeu){
        super("Poker V3");
        setSize(1300, 650);

        this.jeu = jeu;

        //Initialisation des attributs
        panneauxJoueurs = new HashMap<>();
        infosJoueur = new HashMap<>();
        infosJeu = new JPanel(new GridLayout());
        infosJeu.setOpaque(false);
        table = new JPanel(new BorderLayout());
        table.setBackground(new Color(82, 3, 3));
        raisedBevel = BorderFactory.createSoftBevelBorder(BevelBorder.RAISED);
        loweredBevel = BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED);
        compound = BorderFactory.createCompoundBorder(raisedBevel, loweredBevel);
        table.setBorder(compound);

        pannelBoutons = new JPanel(new BorderLayout());
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
        valeurPot.setForeground(new Color(255, 255, 255));

        raiseSlider = new JSlider();
        raiseSlider.setBorder(raisedBevel);

        valeurActuelleSlider = new JLabel("",SwingConstants.CENTER);
        valeurActuelleSlider.setForeground(new Color(255,255,255));
        pannelSlider = new JPanel(new BorderLayout());
        pannelSlider.setOpaque(false);
        pannelSlider.add(valeurActuelleSlider,BorderLayout.SOUTH);
        pannelSlider.add(raiseSlider,BorderLayout.CENTER);
        pannelBoutons.add(pannelSlider, BorderLayout.SOUTH);
        mettreAJourRaiseSlider();

        jeu.getJoueurs().getJoueurs().forEach(joueur ->
            {cartesJoueurs.put(joueur,new JPanel(layoutCartes));

            infosJoueur.put(joueur,new JLabel("",SwingConstants.CENTER));
            coupsJoueur.put(joueur, new JLabel("", SwingConstants.CENTER));
            panneauxJoueurs.put(joueur, new JPanel(new BorderLayout()));
            iconCartesJoueurs.put(joueur, new LinkedList<>()); }
        );
        gbcPrincipal = new GridBagConstraints();

        //Initialisation des boutons et ajout du écouteur
        prochaineTournee=new JButton("Prochaine tournee");
        prochaineTournee.setBackground(new Color (82,3,3));
        prochaineTournee.addActionListener(new EcouteurV2(this,"ProchaineTournee"));
        prochaineTournee.setVisible(false);
        check = new JButton("Check");
        check.setBackground(new Color (82,3,3));
        check.setForeground(new Color (255,255,255));
        check.addActionListener(new EcouteurV2(this, "Check"));
        call = new JButton("Call");
        call.setBorder(raisedBevel);
        call.setForeground(new Color (255,255,255));
        call.setBackground(new Color (82,3,3));
        call.addActionListener(new EcouteurV2(this, "Call"));
        raise = new JButton("Raise "+2*jeu.pariActuel);
        raise.setBorder(raisedBevel);
        raise.setForeground(new Color (255,255,255));
        raise.setBackground(new Color (82,3,3));
        raise.addActionListener(new EcouteurV2(this, "Raise"));
        fold = (new JButton("    Fold    "));
        fold.setBorder(raisedBevel);
        fold.setForeground(new Color(255,255,255));
        fold.setBackground(new Color (82,3,3));
        fold.addActionListener(new EcouteurV2(this, "Fold"));
        pannelBoutons.add(check, BorderLayout.NORTH);
        pannelBoutons.add(call, BorderLayout.WEST);
        pannelBoutons.add(raise,BorderLayout.CENTER);
        pannelBoutons.add(fold,BorderLayout.EAST);
        table.add(prochaineTournee,BorderLayout.NORTH);
        pannelBoutons.setVisible(false);


        //Création de l'icone de la carte tournée
        carteTournee = Carte.redimensioner(84, 112, new ImageIcon(getClass().getResource("res/back.png")));
        // Création de l'espace des cartes sur la table
        espaceCarte = Carte.redimensioner(84, 112, new ImageIcon(getClass().getResource("res/espace_carte.jpg")));


        gbcPrincipal = new GridBagConstraints();
        principal = new JPanel(new GridBagLayout());

        //Mise de Couleurs
        principal.setBackground(new Color(23,22,21));
        pannelBoutons.setOpaque(false);
        jeu.getJoueurs().getJoueurs().forEach(joueur ->
                {   JPanel j = new JPanel(layoutCartes);
                    j.setOpaque(false);
                    cartesJoueurs.put(joueur,j);

                    infosJoueur.put(joueur,new JLabel("",SwingConstants.CENTER));
                    coupsJoueur.put(joueur, new JLabel("", SwingConstants.CENTER));
                    panneauxJoueurs.put(joueur, new JPanel(new BorderLayout()));
                    iconCartesJoueurs.put(joueur, new LinkedList<>()); }
        );


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
            raiseSlider.setMinimum(10);
            valeursSlider.put(10,new JLabel("10"));
        }
        else {
            raiseSlider.setMinimum(jeu.pariActuel*2);
            valeursSlider.put(jeu.pariActuel*2,new JLabel(""+raiseSlider.getMinimum()));
        }
        raiseSlider.setMaximum(jeu.getJoueurHumain().getArgent());
        valeursSlider.put(raiseSlider.getMaximum(),new JLabel(""+raiseSlider.getMaximum()));
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
            cartesTable.add(new JLabel(espaceCarte));
        }
        table.add(cartesTable, BorderLayout.CENTER);
        table.add(new JLabel("                                "), BorderLayout.WEST);
        table.add(new JLabel("                                "), BorderLayout.EAST);
        table.add(new JLabel(("  ")),BorderLayout.NORTH);
        valeurPot.setOpaque(false);
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
                    infosJoueur.get(j).setForeground(new Color(255,255,255));
                    coupsJoueur.get(j).setForeground(new Color(208, 176, 50));
                } else if (j.smallBlind) {
                    infosJoueur.get(j).setText(j.nom + " || Argent: " + j.getArgent() + "|| Small Blind");
                    infosJoueur.get(j).setForeground(new Color(255,255,255));
                    coupsJoueur.get(j).setForeground(new Color(208, 176, 50));
                } else if (j.bigBlind) {
                    infosJoueur.get(j).setText(j.nom + " || Argent: " + j.getArgent() + "|| Big Blind");
                    infosJoueur.get(j).setForeground(new Color(255,255,255));
                    coupsJoueur.get(j).setForeground(new Color(208, 176, 50));
                } else {
                    infosJoueur.get(j).setText(j.nom + " || Argent: " + j.getArgent());
                    infosJoueur.get(j).setForeground(new Color(255,255,255));
                    coupsJoueur.get(j).setForeground(new Color(208, 176, 50));
                }
                coupsJoueur.get(j).setText(j.coup);
                panneauxJoueurs.get(j).setOpaque(false);
                panneauxJoueurs.get(j).add(cartesJoueurs.get(j), BorderLayout.NORTH);
                panneauxJoueurs.get(j).add(infosJoueur.get(j), BorderLayout.CENTER);
                panneauxJoueurs.get(j).add(coupsJoueur.get(j), BorderLayout.SOUTH);
            }
            else {
                JPanel plusDArgent = new JPanel();
                plusDArgent.setOpaque(false);
                plusDArgent.add(new JLabel(j.nom+" || Hors jeu"));
                plusDArgent.setForeground(new Color(255,255,255));
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
        if(j.dealer) {
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
            handGagnante.add(new JLabel(jeu.getDernierDansLaTournee().nom+" prend le pot.", SwingConstants.CENTER), BorderLayout.SOUTH);
        }
        else {
            assert !jeu.getJoueursGagnants().isEmpty() : "Liste de joueurs gagnants vide";
            StringBuilder infosHandGagnante = new StringBuilder();
            if (jeu.getJoueursGagnants().size() == 1) {
                JPanel cartesHandGagnante = new JPanel(layoutCartes);
                Joueur gagnant = jeu.getJoueursGagnants().getFirst();
                gagnant.getCartesHand().forEach(carte -> cartesHandGagnante.add(new JLabel(carte.icon)));
                infosHandGagnante.append(gagnant.nom).append(" || ").append(gagnant.getHand().getDescription());
                handGagnante.add(cartesHandGagnante, BorderLayout.NORTH);
                handGagnante.add(new JLabel(infosHandGagnante.toString(), SwingConstants.CENTER), BorderLayout.CENTER);
                handGagnante.add(new JLabel(descriptionPot, SwingConstants.CENTER), BorderLayout.SOUTH);
            } else {
                for (Joueur j : jeu.getJoueursGagnants()) {
                    infosHandGagnante.append(j.nom).append(" || ");
                }
                infosHandGagnante.append(jeu.getJoueursGagnants().getFirst().getHand().getDescription());
                handGagnante.add(new JLabel(infosHandGagnante.toString()),BorderLayout.CENTER);
                handGagnante.add(new JLabel(descriptionPot,SwingConstants.CENTER), BorderLayout.SOUTH);
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
        cartesTable.add(new JLabel(espaceCarte));
        cartesTable.add(new JLabel(espaceCarte));
        revalidate();
        repaint();
    }

    public void turn(){
        cartesTable.removeAll();
        cartesTable.add(new JLabel(iconCartesTable.get(0)));
        cartesTable.add(new JLabel(iconCartesTable.get(1)));
        cartesTable.add(new JLabel(iconCartesTable.get(2)));
        cartesTable.add(new JLabel(iconCartesTable.get(3)));
        cartesTable.add(new JLabel(espaceCarte));
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
        if((jeu.pariActuel == 0)) {
            call.setVisible(false);
            check.setVisible(true);
        }
        else {
            call.setText("Call: "+(jeu.pariActuel - jeu.getJoueurHumain().derniereValeurPariee));
            check.setVisible(false);
            call.setVisible(true);
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
