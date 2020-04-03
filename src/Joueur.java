import java.util.LinkedList;

public class Joueur implements Comparable{

    private Hand hand= new Hand();
    private LinkedList<Carte> cartesSurMain = new LinkedList<>(); //Ll avec les deux cartes initiales sur la main du joueur
    protected String nom; //Nom du joueur
    protected static int compteurJoueurs = 1; //Utilisé pour le nom deus joueurs ordinateurs
    private int argent; //L'ARGENT
    protected Intelligence intelligence;
    protected String coup;

    //Attributs à utiliser pour déroulement du jeu
    protected boolean dealer; //SI LE JOUEUR EST LE DEALER
    protected boolean dansJeu; //SI LE JOUEUR EST ACTIF DANS LA UNE TOURNéE (NA PAS FOLDÉ)
    protected boolean bigBlind; //SI LE JOUEUR EST LE BIG BLIND, PAS UTILISE JUSQUICI
    protected boolean smallBlind; //SI LE JOUEUR EST LE SMALL BLIND, PAS UTILISE JUSQUICI
    protected boolean dejaJoue = false;// pas necessaire si "playing" et "position"
    protected boolean playing = false;
    protected int position; // position sur tour de paris //peut ne pas etre optimale (VERIFI DANS JEU)
    /*

     */

    public Joueur(String nom){ // création joueur humain
        this.nom=nom;
        this.argent=0;
        this.dealer=false;
        this.dansJeu=true;
        this.bigBlind=false;
        this.smallBlind=false;
        this.intelligence = null;
        this.coup = "";
    }

    /*
    Constructeur pour creer un joueur ordinateur, prend en parametre le niveau d'inteligence
    @param - int niveau - niveau d'inteligence de l'ordi
     */
    public Joueur(int niveau){
        compteurJoueurs++;
        this.argent=0;
        this.nom="Joueur "+compteurJoueurs;
        this.dealer=false;
        this.dansJeu=true;
        this.bigBlind=false;
        this.smallBlind=false;
        this.intelligence = new Intelligence(niveau);
        this.coup = "";
    }
    /*
    Retourne la hand actuelle du joueur en forme de ll de cartes
     */
    public Hand getHand(){
        return hand;
    }

    public void setHand(LinkedList<Carte> cartesSurMain, LinkedList<Carte> cartesSurTable){
        hand.setHand(cartesSurMain,cartesSurTable);
    }

    public void setCartesSurMain(LinkedList<Carte> cartesSurMain) {
        this.cartesSurMain = cartesSurMain;
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
    public boolean jouer(int pariActuel, boolean b){
        int decision = intelligence.decision();
        boolean parie = false;
        if(decision == -1) {
            fold();
        }else if(decision == 0){
            check();
        } else if(decision == 1){
            call();
        } else if(decision>1){
            raise(decision);
        }
        return parie;
    }
    public void fold(){
        //IMPLEMENTER
    }
    public void check(){
        //implementer
    }
    public void call(){
        //implementer
    }
    public void raise(int pari){
        //implementer
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
    /*
    Prend en compte la valeur de la hand du joueur pour les trier selon la puissance de la hand
     */
    public int compareTo(Object j2) {
        int comparaison = 0;
        if (this.getHand().getValeurHand() > ((Joueur)j2).getHand().getValeurHand()) {
            comparaison = 1;
        } else if (this.getHand().getValeurHand() < ((Joueur)j2).getHand().getValeurHand()) {
            comparaison = -1;
        }
        return comparaison;
    }

    public boolean equals(Object o){
        Joueur j = (Joueur)o;
        return j.nom.equals(this.nom);
    }
    public String toString(){
        return nom;
    }
}
