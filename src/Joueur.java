import java.util.LinkedList;

public class Joueur {

    private LinkedList<Carte> hand;
    private LinkedList<Carte> cartesInitiales;
    protected String nom;
    protected static int nJoueurs=0;
    private int argent;
    private boolean dealer;
    private boolean dansJeu;
    private boolean bigBlind;
    private boolean smallBlind;
    protected Intelligence intelligence;
    protected boolean dejaJoue = false;

    public Joueur(String nom){ // création joueur humain
        nJoueurs++;
        this.nom=nom;
        this.argent=0;
        this.dealer=false;
        this.dansJeu=true;
        this.bigBlind=false;
        this.smallBlind=false;
        intelligence = null;
    }

    public Joueur(int niveau){
        nJoueurs++;
        this.argent=0;
        this.nom="Joueur "+nJoueurs;
        this.dealer=false;
        this.dansJeu=true;
        this.bigBlind=false;
        this.smallBlind=false;
        intelligence = new Intelligence(niveau);
    }

    public LinkedList<Carte> getHand(){
        return hand;
    }

    public LinkedList<Carte> getCartesInitiales(){return cartesInitiales;}

    public boolean parier(int q){
        if(q<=argent) {
            argent = argent - q;
            return true;
        }
        else{
            return false;
        }
    }

    public void jouer(){
        dejaJoue=false;

    }

    public int getArgent(){
        return argent;
    }

    public void setArgent(int q){
        this.argent=q;
    }

    public void ajouterArgent(int q){this.argent+=q;}

    public void setCartesInitiales(LinkedList<Carte> cartes){
        this.cartesInitiales=cartes;
        this.hand=cartes;
    }

    public void ajouterCartes(LinkedList<Carte> cartes){this.hand.addAll(cartes);}

    public void setDealer(){
        this.dealer=true;
    }

    public boolean isDealer(){
        return dealer;
    }

    public String mao(){
        String mao="";
        for(Carte c: hand){
            mao=mao+c.toString()+"\n";
        }
        return mao;
    }

    public void fold(){
        this.dansJeu=false;
        for(Carte c: cartesInitiales){
            c.tournerCarte();
        }
    }

    public boolean isDansJeu(){
        return dansJeu;
    }

    public void setBigBlind(){
        bigBlind=true;
    }

    public void setSmallBlind(){
        smallBlind=true;
    }

    public boolean isBigBlind() {
        return bigBlind;
    }

    public boolean isSmallBlind(){
        return smallBlind;
    }


    public void resetAll(){
        this.dealer=false;
        this.smallBlind=false;
        this.bigBlind=false;
    }
}
