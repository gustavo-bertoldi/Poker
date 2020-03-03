import java.util.ArrayList;
import java.util.LinkedList;


public class Intelligence {

    private int niveau; // pris de Jeu
    protected double[] rangeBet = new double[2];
    protected double[] rangeDecisions = new double[2];
    private LinkedList<Carte> surMain = new LinkedList<>(); // les cartes distribuees au debut du jeu
    private LinkedList<Carte> surTable = new LinkedList<>(); //affectée en ajoutant les cartes tournees
    protected Hand hand; // protected pour pouvoir affecter depouis joueur

    /*
    - qualiteMain mesure en pourcentage le pouvoir de la main
    - est affecté à chaque round
    - prend en compte les valeurs et position des cartes pour evaluer la qualité
    - prendre en compte les possibilites des autres mains si niveau haut
     */
    protected double qualiteMain = 0; // en quelle mesure est la hand bonne pourcentage
    /*
    HighCard[0;0,1] 0,1 si Ace
    OnePair[0,1;0,3] 0,2 si pair de aces
    TwoPairs[0,3;0,45] 0,3 si aces et rois (considerer si pairs sur table)
    ThreeOfAKind[0,45;0,6] 0,4 si aces
    Straight[0,6;0,75] 0,5 si roi kicker
    Flush [0,75;0,85] 0,6 si roi kicker
    FullHouse [0,85;0,9] 0,7 si trois aces
    FourOfAKind[0,9;0,95] 0,8 si aces
    StraightFlush[0,95;1] 1 si royalStraight
    RoyalStraightFlush[1]
     */

    /*
    La prise de decisions prend en compte les pourcentages de qualite de main.
    Le niveau d'intelligence change à partir de quel pourcentage on prend telle ou telle decision
    Mais change aussi le blef
     */
    private ArrayList<Integer> blef = new ArrayList<>();// Array pour avoir contains()

    public Intelligence(int i, LinkedList<Carte> surMain) {
        niveau = i; //i donne par Jeu
        this.surMain = surMain;
        hand = new Hand(surMain);
    }
    // CREER METHODE AJOUTER CARTE A HAND
    public double raise(double[] range){
        double montant = 0;

        return montant;
    }

}
