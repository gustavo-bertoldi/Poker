// comment differencier un joueur reel d'un ordinateur?
public class Joueur {

    private double fiches;
    private Hand hand;
    private boolean isDealer;
    private boolean isSmall;
    private boolean isBig;
    private String nom;

    public Joueur(String nom){
        this.nom = nom;
        fiches = 1000;
        hand=null;
        isDealer = false;
        isSmall = false;
        isBig = false;
    }

    public void devenirDealer(){
        isDealer = true;
    }

    public void devenirSmall(){
        isSmall = true;
    }

    public void devenirBig(){
        isBig = true;
    }

    public void setHand(Carte[] cartes){
        //
    }


    public String toString(){
        return  "Joueur "+ nom + "; Fiches " + fiches + "; Dealer, Small, big" + isDealer + isSmall + isBig;
    }
}
