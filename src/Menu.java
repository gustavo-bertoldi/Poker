import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Menu extends JFrame {

    private Jeu jeu;
    private ImageIcon imageLogo;
    private JPanel principal, logo, choix, panneauBoutonsDif;
    private JButton easy, medium, hard;
    private JLabel nom, difficulte, labelLogo;
    private JTextField nomJoueur;
    private JButton highScores;
    private JButton start;
    private Border raisedBevel;

    public Menu(){
        super("POKERINSA");
        setSize(650,650);

        //INITIALISATION DE JEU -> Utilisé pour affecter les attributs du jeu
        //this.jeu = jeu;

        //                INITIALISATION DES ATTRIBUTS GRAPHIQUES
        //                              BORDER
        raisedBevel = BorderFactory.createSoftBevelBorder(BevelBorder.RAISED);

        //                              LOGO
        imageLogo = new ImageIcon("res/logo");
        labelLogo = new JLabel(imageLogo);

        //                            PANELS
        principal = new JPanel(new BorderLayout());
        principal.setBackground(new Color(23,22,21));
        logo = new JPanel();
        choix = new JPanel(new GridLayout(2,2));
        choix.setOpaque(false);
        panneauBoutonsDif = new JPanel();
        panneauBoutonsDif.setOpaque(false);

        //                         BOUTONS DIFFICULTE
        //   EASY
        easy = new JButton("Facile");
        easy.setBorder(raisedBevel);
        easy.setBackground(new Color(82,3,3));
        easy.addActionListener(new EcouteurMenu(this, "facile"));
        //   MEDIUM
        medium = new JButton("Ça va");
        medium.setBorder(raisedBevel);
        medium.setBackground(new Color(82,3,3));
        medium.addActionListener(new EcouteurMenu(this, "medium"));
        //   HARD
        hard = new JButton("Pokerstars");
        hard.setBorder(raisedBevel);
        hard.setBackground(new Color(82,3,3));
        hard.addActionListener(new EcouteurMenu(this, "hard"));

        // START
        start = new JButton("Sapeca iá iá!!");
        start.setBorder(raisedBevel);
        start.setBackground(new Color(82,3,3));
        start.addActionListener(new EcouteurMenu(this, "start"));

        //                    LABELS
        //   NOM
        nom = new JLabel("Saisissez votre prénom: ");
        nom.setOpaque(false);
        nom.setForeground(new Color(255,230,77));

        //  DIFFICULTE
        difficulte = new JLabel("Choissez le niveau de difficulté: ");
        difficulte.setOpaque(false);
        difficulte.setForeground(new Color(255,230,77));

        //                     TEXT FIELD
        nomJoueur = new JTextField(15);
        nomJoueur.setForeground(new Color(255,230,77));
        nomJoueur.setOpaque(false);

        creerLayout();
        this.add(principal);
        this.setVisible(true);
    }

    public void setDifficulte(int niveau){
        jeu.setNiveau(niveau);
    }


    public void lancerJeu() throws Exception {
        jeu.lancerJeu(nomJoueur.getText());
    }
    public void closeMenu(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    private void creerLayout(){
        // MISE EN POSITION


        //    NORTH -> LOGO
        logo.add(labelLogo);

        //  CENTER-> CHOIX
        choix.add(nom);
        choix.add(nomJoueur);

        panneauBoutonsDif.add(easy);
        panneauBoutonsDif.add(medium);
        panneauBoutonsDif.add(hard);

        choix.add(difficulte);
        choix.add(panneauBoutonsDif);

        // SOUTH -> LANCER JEU
        principal.add(logo, BorderLayout.NORTH);
        principal.add(choix, BorderLayout.CENTER);
        principal.add(start, BorderLayout.SOUTH);

    }
    public static void main (String [] a){
        Menu m = new Menu();
    }
}
