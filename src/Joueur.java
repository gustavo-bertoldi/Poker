import java.util.LinkedList;

public class Joueur {

    private LinkedList<Carte> hand;
    protected String nom;
    protected static int nJoueurs=0;
    private int argent;
    private boolean dealer;
    private boolean dansJeu;
    private boolean bigBlind;
    private boolean smallBlind;

    public Joueur(String nom){
        nJoueurs++;
        this.nom=nom;
        this.argent=0;
        this.dealer=false;
        this.dansJeu=true;
        this.bigBlind=false;
        this.smallBlind=false;
    }

    public Joueur(){
        nJoueurs++;
        this.argent=0;
        this.nom="Joueur "+nJoueurs;
        this.dealer=false;
        this.dansJeu=true;
        this.bigBlind=false;
        this.smallBlind=false;
    }

    public LinkedList<Carte> getHand(){
        return hand;
    }

    public void payer(int q){
        argent=argent-q;
    }

    public void ajouterArgent(int q){
        argent=argent+q;
    }

    public int getArgent(){
        return argent;
    }

    public void setHand(LinkedList<Carte> Cartes){
        this.hand=Cartes;
    }

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
