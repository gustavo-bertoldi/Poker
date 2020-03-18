import java.util.LinkedList;

public class Joueur {

    private Hand hand= new Hand(); //LL AVEC TOUTES LES CARTES DISPONIBLES
    private LinkedList<Carte> cartesSurMain = new LinkedList<>(); //LL AVEC LES 2 CARTES INITIALES
    protected String nom; //NOM DU JOUEUR
    protected static int nJoueurs=0; //UTILISE POUR DIFERENCIER LES JOUEURS ORDINATEURS
    private int argent; //L'ARGENT
    private boolean dealer; //SI LE JOUEUR EST LE DEALER
    private boolean dansJeu; //SI LE JOUEUR EST ACTIF DANS LA UNE TOURNéE (NA PAS FOLDÉ)
    private boolean bigBlind; //SI LE JOUEUR EST LE BIG BLIND, PAS UTILISE JUSQUICI
    private boolean smallBlind; //SI LE JOUEUR EST LE SMALL BLIND, PAS UTILISE JUSQUICI
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

    /*
    Constructeur pour creer un joueur ordinateur, prend en parametre le niveau d'inteligence
    @param - int niveau - niveau d'inteligence de l'ordi
     */
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
    /*
    Retourne la hand actuelle du joueur en forme de ll de cartes
     */
    public Hand getHand(){
        return hand;
    }

    /*
    Retourne les deux cartes initiales du joueur e forme de ll de cartes
     */
    public LinkedList<Carte> getCartesSurMain(){return cartesSurMain;}

    /*
    Permet au joueur de parier si la quantite desiree est inferieure ou egale à la somme d'argent
    @param int q - quantite a parier
     */
    public boolean parier(int q){
        if(q<=argent) {
            argent = argent - q;
            return true;
        }
        else{
            return false;
        }
    }

    /*
    METHODE A CREER
     */
    public void jouer(){
        dejaJoue=false;

    }
    /*
    Retroune la somme d'argent actuelle
     */
    public int getArgent(){
        return argent;
    }
    /*
    Permet de definir la somme initiale d'argent du joueur
     */
    public void setArgent(int q){
        this.argent=q;
    }

    /*
    Permet de modifier la somme d'argent actuelle, au cas ou le joueur ait gagne ou perdu de l'argent
     */
    public void ajouterArgent(int q){
        this.argent += q;}

    /*
    Permet de definir les deux cartes initiales du jours, ajoute ces deux premier cartes a la hand
    @param LinkedList<Carte> cartes - ll avec les deux cartes initiales
     */
    public void setCartesSurMain(LinkedList<Carte> cartes){
        this.cartesSurMain.addAll(cartes);
        this.hand.setSurMain(cartes);
    }
    /*
    Permet d'ajouter les cartes donnes en parametre a la ll hand
    @param LinkedList<Carte> cartes - cartes a ajouter a hand
     */
    public void ajouterCartes(LinkedList<Carte> cartes){
        this.hand.getCartes().addAll(cartes);
        this.hand.setSurTable(cartes);
    }

    /*
    Permet de definir le joueur comme dealer
     */
    public void setDealer(){
        this.dealer=true;
    }

    /*
    Retourne TRUE si le jouer est le dealer, sinon retourne FALSE
     */
    public boolean isDealer(){
        return dealer;
    }

    /*
    Methode de test
     */
    public String mao(){
        String mao="";
        for(Carte c: hand.getCartes()){
            mao=mao + c.toString()+"\n";
        }
        return mao;
    }

    /*
    Action de fold dans le jeu, fait tourner les cartes du joueur dans l'interface graphique
     */
    public void fold(){
        this.dansJeu=false;
        for(Carte c: cartesSurMain){
            c.tournerCarte();
        }
    }

    /*
    Retourne TRUE si le joueur est actif dans le jeu, sinon retourne FALSE
     */
    public boolean isDansJeu(){
        return dansJeu;
    }

    /*
    Permet de definir le joueur comme le big blind
     */
    public void setBigBlind(){
        bigBlind=true;
    }

    /*
    Permet de definir le joueur comme le small blind
     */
    public void setSmallBlind(){
        smallBlind=true;
    }

    /*
    Retourne TRUE si le joueur est le big blind, sinon retourne false
     */
    public boolean isBigBlind() {
        return bigBlind;
    }

    /*
    Retourne TRUE si le joueur est le small blind, sinon retourne false
     */
    public boolean isSmallBlind(){
        return smallBlind;
    }


    /*
    Permet de reinitialiser toutes les atributs dealer, small blind et big blind du joueur
     */
    public void resetAll(){
        this.dealer=false;
        this.smallBlind=false;
        this.bigBlind=false;
        this.dejaJoue=false;
    }
}
