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
    private Joueur joueur;

    /*
    - qualiteMain mesure en pourcentage le pouvoir de la main
    - est affecté à chaque round
    - prend en compte les valeurs et position des cartes pour evaluer la qualité
    - prendre en compte les possibilites des autres mains si niveau haut
     */
    protected double qualiteMain = 0; // en quelle mesure est la hand bonne pourcentage

    /*
    HighCard[0;0,1] 0,1 si Ace 2->14
    OnePair[0,1;0,3] 0,2 si pair de aces 20->140
    TwoPairs[0,3;0,45] 0,3 si aces et rois 200->2700 pair*10 +pair2*10
    ThreeOfAKind[0,45;0,6] 0,4 si aces 20000->140000
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

    public Intelligence(int i,Joueur j,Hand h) {
        niveau = i; //i donne par Jeu
        this.joueur=j;
        this.hand=h;
    }
   /* public void setSurMain(LinkedList<Carte> surMain){
        this.surMain = surMain;
        hand.setSurMain(surMain);

    public void setSurTable(LinkedList<Carte> tourneesSurTable){
        surTable.addAll(tourneesSurTable); // ajoute toutes les cartes prises comme parametre à surTable
        hand.setHand(tourneesSurTable);
    }*/

    public boolean Jouable(){// methode pour sortir si on a pas mieu que hotaur as
		int v=0;
		v= hand.getValeurHand();
			if (v<15){
			return false;
		}
   /* public int decision(){
        return 0;
    }*/


    /*public void setRangeBet(){
         ajouter methode prenant en compte qualite main
    }
    * */

   public char jouer(int pariActuel; int argentJoueur){
        //Cas de l'intelligence aléatoire
        if(niveau==0){
            int r = (int) (Math.random() * 3);
            if(r==0){
                return 'f';
            }

            else if(r==1){
                return 'c';
            }

            else{
                int raise = pariActuel + (int)((argentJoueur+1)*Math.random());
                return 'r';
            }
        }
        else{
			if(niveau ==1){
				int v= hand.getValeurHand();
				if (joueur.getArgent()>= pariActuel+100){
					if ( Jouable()== true){
						if( v<14001){		// egaler la mise pour continuer dans le jeu si on a inferieur a 2 paires
							return "c";
						}
						else{
							joueur.parier(pariActuel+(int)(Math.random()*((10-1)+1)*10));// aumenter la mise d'une quntiter aleatoire entre 0-100
							return "r";
						}
					}
				else {
					return "f";// joueur ne joue pas ce tour et couche
				}
				}
				}

			else{
            return 'a';
         }
	 }
	 }


}
}
