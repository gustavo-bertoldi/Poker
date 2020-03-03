import java.util.ArrayList;
import java.util.LinkedList;


public class Intelligence {

    private int niveau; // pris de Jeu
    protected double[] rangeBet = new double[2];
    protected double[] rangeCall = new double[2]; // pour quelles bets il va payer
    protected double[] rangeDecisions = new double[2];
    private LinkedList<Carte> surMain = new LinkedList<>(); // les cartes distribuees au debut du jeu
    private LinkedList<Carte> surTable = new LinkedList<>(); //affectée en ajoutant les cartes tournees
    protected Hand hand; // protected pour pouvoir affecter depouis joueur; y a til une façon de l'affecter directement?

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

    public Intelligence(int i) {
        niveau = i; //i donne par Jeu
    }
    public void setSurMain(LinkedList<Carte> surMain){
        this.surMain = surMain;
        hand = new Hand(surMain);
    }

    public void setSurTable(LinkedList<Carte> tourneesSurTable){
        surTable.addAll(tourneesSurTable); // ajoute toutes les cartes prises comme parametre à surTable
        hand.setHand(tourneesSurTable);
    }

    public double qualiteHighCard(){ // appellee s'il y a high card
        double qualite = 0;
        qualite = hand.highCard().valeur/10; // indice multiplicatif de qualiteMain
        return qualite;
    }

    public double qualitePair(){
        double qualite = 0;
        //completer methode
        return qualite;
    }

    public double qualiteThreeOfAKind(){
        double qualite = 0;
        //completer methode
        return qualite;
    }
    public double qualiteTwoPairs(){
        double qualite = 0;
        //completer methode
        return qualite;
    }

    public double qualiteStraight(){
        double qualite = 0;
        //completer methode
        return qualite;
    }
    public double qualiteFullHouse(){
        double qualite = 0;
        //completer methode
        return qualite;
    }

    public double qualiteFourOfAKind(){
        double qualite = 0;
        //completer methode
        return qualite;
    }

    public double qualiteFlush(){
        double qualite = 0;
        //completer methode
        return qualite;
    }

    public double qualiteStraightFlush(){
        double qualite = 0;
        //completer methode
        return qualite;
    }

    public void setRangeBet(){
        // ajouter methode prenant en compte qualite main
    }

}
