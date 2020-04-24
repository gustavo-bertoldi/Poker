import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaLiteralAnnotationArgument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class FenetreJeuV3 extends JFrame {

    private final ImageIcon carteTournee;
    private JPanel table, pannelBoutons,cartesTable, pannelSlider, principal, handGagnante;
    private HashMap<Joueur, JPanel> panneauxJoueurs, cartesJoueurs;
    private HashMap<Joueur, JLabel> infosJoueur, coupsJoueur;
    private HashMap<Joueur, LinkedList<ImageIcon>> iconCartesJoueurs;
    Hashtable<Integer, JLabel> valeursSlider;
    private JButton call, raise, fold, restart, prochaineTournee;
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
        table = new JPanel(new BorderLayout());
        pannelBoutons = new JPanel(new BorderLayout());
        layoutCartes = new FlowLayout();
        layoutCartes.setHgap(10);
        cartesTable = new JPanel(layoutCartes);
        handGagnante = new JPanel(new BorderLayout());
        cartesJoueurs = new HashMap<>();
        coupsJoueur = new HashMap<>();
        iconCartesJoueurs = new HashMap<>();
        iconCartesTable = new LinkedList<>();
        valeurPot= new JLabel("",SwingConstants.CENTER);
        raiseSlider = new JSlider();
        valeurActuelleSlider = new JLabel("",SwingConstants.CENTER);
        pannelSlider = new JPanel(new BorderLayout());
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
        prochaineTournee.addActionListener(new EcouteurV2(this,"ProchaineTournee"));
        prochaineTournee.setVisible(false);
        call = new JButton("Call");
        call.addActionListener(new EcouteurV2(this, "Call"));
        raise = new JButton("Raise");
        raise.addActionListener(new EcouteurV2(this, "Raise"));
        fold = (new JButton("Fold"));
        fold.addActionListener(new EcouteurV2(this, "Fold"));
        pannelBoutons.add(call, BorderLayout.WEST);
        pannelBoutons.add(raise,BorderLayout.CENTER);
        pannelBoutons.add(fold,BorderLayout.EAST);
        table.add(prochaineTournee,BorderLayout.NORTH);
        pannelBoutons.setVisible(false);


        //Création de l'icone de la carte tournée
        carteTournee = Carte.redimensioner(84, 112, new ImageIcon("src/res/back.png"));


        gbcPrincipal = new GridBagConstraints();
        principal = new JPanel(new GridBagLayout());

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
        revalidate();
        repaint();

    }

    public void creerTable(){
        iconCartesTable.addAll(jeu.getIconCartesTable());
        for(int i=0; i<5; i++){
            cartesTable.add(new JLabel(carteTournee));
        }
        table.add(cartesTable, BorderLayout.CENTER);
        valeurPot.setText("Pot : "+jeu.potActuel);
        table.add(valeurPot, BorderLayout.SOUTH);
    }

    public void mettreAJourValuerPot(){
        valeurPot.setText("Pot : "+jeu.potActuel);
    }


    public void creerPanneauxJoueurs(){
        Node current = jeu.getJoueurs().getNodeHumain();
        System.out.println("Creation des panneaux des joueurs");
        for (int i=0; i<jeu.nJoueurs; i++){
            Joueur j = current.joueur;
            System.out.println("Current : "+j.nom);
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
                plusDArgent.add(new JLabel(j.nom+" || Hors jeu"));
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
        revalidate();
        repaint();
    }

    public void creerHandGagnant(boolean tousFold){
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
                handGagnante.add(cartesHandGagnante, BorderLayout.CENTER);
                handGagnante.add(new JLabel(infosHandGagnante.toString(), SwingConstants.CENTER), BorderLayout.SOUTH);
            } else {
                for (Joueur j : jeu.getJoueursGagnants()) {
                    infosHandGagnante.append(j.nom).append(" || ");
                }
                infosHandGagnante.append(jeu.getJoueursGagnants().getFirst().getHand().getDescription());
                handGagnante.add(new JLabel(infosHandGagnante.toString()));
            }
        }

    }

    public void afficherHandGagnante(boolean tousFold){
        creerHandGagnant(tousFold);
        revalidate();
        repaint();
        handGagnante.setVisible(true);
    }

    public void effacerCoupsJoueur(){
        coupsJoueur.values().forEach(label -> label.setText(""));
    }

    public void creerLayout(){
        creerTable();
        creerPanneauxJoueurs();

        Node current = jeu.getJoueurs().getNodeHumain();

        if (jeu.nJoueurs == 6){
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
            gbcPrincipal.insets = new Insets(0, 0, 0, 5);
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
            gbcPrincipal.insets = new Insets(0, 0, 0, 5);
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
            gbcPrincipal.anchor=GridBagConstraints.CENTER;
            handGagnante.setVisible(false);
            principal.add(handGagnante, gbcPrincipal);

        }
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
        cartesTable.add(new JLabel(carteTournee));
        cartesTable.add(new JLabel(carteTournee));
        revalidate();
        repaint();
    }

    public void turn(){
        cartesTable.removeAll();
        cartesTable.add(new JLabel(iconCartesTable.get(0)));
        cartesTable.add(new JLabel(iconCartesTable.get(1)));
        cartesTable.add(new JLabel(iconCartesTable.get(2)));
        cartesTable.add(new JLabel(iconCartesTable.get(3)));
        cartesTable.add(new JLabel(carteTournee));
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
        if(jeu.pariActuel==0) {
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
