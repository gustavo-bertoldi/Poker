import java.util.LinkedList;

public class Joueur implements Comparable{

    private Hand hand= new Hand();
    protected String nom; //Nom du joueur
    private int argent;
    protected String coup;

    //Attributs à utiliser pour déroulement du jeu
    protected boolean dealer; //SI LE JOUEUR EST LE DEALER
    protected boolean bigBlind; //SI LE JOUEUR EST LE BIG BLIND, PAS UTILISE JUSQUICI
    protected boolean smallBlind; //SI LE JOUEUR EST LE SMALL BLIND, PAS UTILISE JUSQUICI
    protected boolean playing;
    protected boolean humain;
    protected int derniereValeurPariee;
    protected boolean dansJeu;
    protected boolean dansTournee;
    protected boolean allIn;
    protected int valeurAllInIncomplet;
    /*

     */

    public Joueur(String nom, boolean humain){ // création joueur humain
        this.nom=nom;
        this.argent=0;
        this.dealer=false;
        this.bigBlind=false;
        this.smallBlind=false;
        this.coup ="";
        this.humain=humain;
        this.derniereValeurPariee=0;
        this.dansJeu=true;
        this.allIn=false;
        this.valeurAllInIncomplet=0;
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

    public void payerBigBlind(int bigBlind, Jeu jeu){
        coup = "Paye BigBlind "+bigBlind;
        parier(bigBlind);
        derniereValeurPariee=bigBlind;
        jeu.potActuel=jeu.potActuel+bigBlind;
        jeu.fenetre.mettreAJourInfosJoueur(this);
    }
    public void payerSmallBlind(int smallBlind, Jeu jeu){
        coup = "Paye SmallBlind "+smallBlind;
        parier(smallBlind);
        derniereValeurPariee=smallBlind;
        jeu.potActuel=jeu.potActuel+smallBlind;
        jeu.fenetre.mettreAJourInfosJoueur(this);
    }

    public void setAction(int action, int valeurPari, Jeu jeu) throws Exception {
        //Cas joueur a couché ses cartes et ne participe plus
        if(action==0){
            coup="Fold";
            jeu.fenetre.enleverCartesJoueur(this);
            jeu.sortirDeLaTournee(this);
        }
        //Cas joueur a payé le pari
        else if(action==1){
            if(valeurPari==0 || valeurPari==derniereValeurPariee) {
                coup = "Check";
            }
            else{
                //Cas où la valeur de pari est supérieure à l'argent du joueur - All in pour jouer
                if(valeurPari>=argent){
                    coup="Call - All in "+(argent);
                    valeurAllInIncomplet = argent;
                    jeu.potActuel = jeu.potActuel + argent;
                    parier(argent);
                    derniereValeurPariee=argent;
                }
                else {
                    coup = "Call " + (valeurPari - derniereValeurPariee);
                    parier(valeurPari - derniereValeurPariee);
                    jeu.potActuel = jeu.potActuel + (valeurPari - derniereValeurPariee);
                    derniereValeurPariee = valeurPari;
                }
            }
        }
        //Cas joueur a augmenté le pari
        else{
            if(action>=argent){
                jeu.potAvantAllIn=jeu.potActuel;
                coup="All in "+(argent);
                jeu.pariActuel = argent;
                jeu.potActuel = jeu.potActuel + (argent);
                parier(action);
            }
            else {
                jeu.pariActuel = action;
                jeu.potActuel = jeu.potActuel + (action - derniereValeurPariee);
                coup = "Raise " + (action);
                parier(action - derniereValeurPariee);
            }
            jeu.dernierAParier = (jeu.joueursDansLaTournee.getNodeAnterieur(this)).joueur;
            derniereValeurPariee = action;
            jeu.fenetre.effacerCoupsJoueur();
        }
        jeu.fenetre.mettreAJourInfosJoueur(this);
        jeu.fenetre.mettreAJourValuerPot();
        if(jeu.joueurActuel.joueur.equals(jeu.dernierAParier)){
            jeu.tourDeParisFini();
        }
        else {
            jeu.prochainJoueur();
        }
    }

    public void setActionJoueurHumain(int action, int pariActuel, Jeu jeu) throws InterruptedException {
        Runnable setActionJoueurHumain = () -> {
            try {
                setAction(action,pariActuel,jeu);
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
        derniereValeurPariee=0;
    }


    public LinkedList<Carte> getCartesSurMain(){return hand.getSurMain();}

    public LinkedList<Carte> getCartesHand(){
        return hand.getCartesHand();
    }

    /*
    Permet au joueur de parier si la quantite desiree est inferieure ou egale à la somme d'argent
    @param int q - quantité a parier
     */
    public void parier(int q){
        if(q>=argent) {
            q=argent;
            allIn=true;
        }
        argent = argent - q;
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
