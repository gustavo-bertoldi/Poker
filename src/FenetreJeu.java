import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
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

    private JComboBox<String> raiseField;

    private LinkedList<JPanel> joueursOrdinateursCartes;

    public FenetreJeu(int nJoueurs){
        super("Poker");
        this.setSize(1350 ,700);

        this.nJoueurs = nJoueurs;

        joueursOrdinateursCartes = new LinkedList<>();
        jPrincipalCartes = new JPanel();
        tableCartes = new JPanel();
        principal = new JPanel(new BorderLayout());

        call = new JButton("Call");
        raise = new JButton("Raise");
        fold = new JButton("Fold");
        raiseField = new JComboBox<>();

        jPrincipalFonctions = new JPanel(new FlowLayout());
        jPrincipalFonctions.add(fold);
        jPrincipalFonctions.add(call);
        jPrincipalFonctions.add(raise);
        jPrincipalFonctions.add(raiseField);

        //Affichage des cartes de cahque joueur aindi comme celles de la table
        jeu = new Jeu(nJoueurs,0);

        for(int i=2; i<=nJoueurs;i++){
            joueursOrdinateursCartes.add(new JPanel());
        }
        ajouterCartesJoueursEtTable();
        creerLayout();

        this.add(principal);

        this.setVisible(true);
    }

    /*
    Recupere l'icon des cartes des joueurs et celles de la table pour leur afficher dans la fenetre
     */
    private void ajouterCartesJoueursEtTable(){
        for (Carte c : jeu.getJoueurs().get(0).getCartesSurMain()){
            c.montrerCarte();
            jPrincipalCartes.add(new JLabel(c.icon));
        }

        for (int i=1; i<nJoueurs ; i++){
            for (Carte c : jeu.getJoueurs().get(i).getCartesSurMain()){
                joueursOrdinateursCartes.get(i-1).add(new JLabel(c.icon));
            }
        }

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

    public static void main(String[] args){
         FenetreJeu fj = new FenetreJeu(9);
    }
}
