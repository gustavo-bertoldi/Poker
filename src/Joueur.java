import java.util.LinkedList;

public class Joueur implements Comparable{

    private Hand hand= new Hand();
    private LinkedList<Carte> cartesSurMain = new LinkedList<>(); //Ll avec les deux cartes initiales sur la main du joueur
    protected String nom; //Nom du joueur
    private static LinkedList<String> nomsJoueursOrdinateurs;
    private int argent; //L'ARGENT
    protected String coup;
    //Attributs à utiliser pour déroulement du jeu
    protected boolean dealer; //SI LE JOUEUR EST LE DEALER
    protected boolean dansJeu; //SI LE JOUEUR EST ACTIF DANS LA UNE TOURNéE (NA PAS FOLDÉ)
    protected boolean bigBlind; //SI LE JOUEUR EST LE BIG BLIND, PAS UTILISE JUSQUICI
    protected boolean smallBlind; //SI LE JOUEUR EST LE SMALL BLIND, PAS UTILISE JUSQUICI
    protected boolean playing = false;
    protected boolean humain;

    protected int action;
    protected boolean dejaJoue = false;// pas necessaire si "playing" et "position"
    /*

     */

    public Joueur(String nom, boolean humain){ // création joueur humain
        this.nom=nom;
        this.argent=0;
        this.dealer=false;
        this.dansJeu=true;
        this.bigBlind=false;
        this.smallBlind=false;
        this.coup = "";
        this.humain=humain;
    }

    /*
    Retourne la hand actuelle du joueur en forme de ll de cartes
     */
    public Hand getHand(){
        return hand;
    }

    public void fold(){
        action=0;
        coup="Fold";
        dansJeu=false;
    }

    public void setHand(LinkedList<Carte> cartesSurTable){
        hand.setHand(cartesSurMain,cartesSurTable);
    }

    public void setCartesSurMain(LinkedList<Carte> cartesSurMain) {
        this.cartesSurMain = cartesSurMain;
    }

    public void setAction(int action, int valeurPari, Jeu jeu) throws Exception {
        if(action==0){
            coup="Fold";
            jeu.sortirDeLaTournee(this);
            jeu.fenetre.enleverCartesJoueur(this);
        }
        else if(action==1){
            if(valeurPari==0) {
                coup = "Check";
            }
            else{
                coup="Call "+valeurPari;
            }
            jeu.potActuel = jeu.potActuel+valeurPari;
            parier(valeurPari);
        }
        else{
            jeu.pariActuel=action;
            jeu.potActuel = jeu.potActuel+action;
            coup="Raise "+action;
            parier(action);
            jeu.dernierAParier = jeu.joueursDansLaTournee.getNodeAnterieur(this).joueur;
        }
        System.out.println("Joueur : "+this.nom+", "+this.coup);
        dejaJoue=true;
        this.action=action;
        jeu.fenetre.mettreAJourInfosJoueur(this);
        jeu.fenetre.mettreAJourValuerPot();
        if(jeu.joueurActuel.joueur.equals(jeu.dernierAParier)){
            System.out.println("tour de paris fini");
            jeu.tourDeParisFini();
        }
        else {
            jeu.prochainJoueur();
        }
    }

    public void setActionJoueurHumain(int action, int pariActul, Jeu jeu) {
        Runnable setActionJoueurHumain = () -> {
            try {
                setAction(action,pariActul,jeu);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread setAction = new Thread(setActionJoueurHumain);
        setAction.start();
    }


    public void setRoleJeu(boolean dealer, boolean smallBlind, boolean bigBlind, boolean playing){
        this.dealer=dealer;
        this.smallBlind=smallBlind;
        this.bigBlind=bigBlind;
        this.playing=playing;
    }

    public void resetRolesJeu(){
        dealer=false;
        bigBlind=false;
        smallBlind=false;
        playing=false;
    }


    /*
        Retourne les deux cartes initiales du joueur e forme de ll de cartes
         */
    public LinkedList<Carte> getCartesSurMain(){return cartesSurMain;}

    public LinkedList<Carte> getCartesHand(){
        return hand.getCartesHand();
    }

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
    Retourne la somme d'argent actuelle
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
        this.argent += q;
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
