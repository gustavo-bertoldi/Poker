import java.util.LinkedList;

public class Joueur implements Comparable{

    private Hand hand= new Hand();
    protected String nom; //Nom du joueur.
    private int argent; //Quantité d'argent.
    protected String coup; //Dernier coup pour affichage dans la GUI. (Ex. Raise 200)
    protected boolean dealer; //True si le joueur est le dealer.
    protected boolean bigBlind; //True si le joueur est le Big Blind.
    protected boolean smallBlind; //True si le joueur est le Small Blind.
    protected boolean playing; //True si le joueur est le joueur actuel.
    protected boolean humain; //True si le joueur est le joueur humain
    protected int derniereValeurPariee; //Dernière valeur pariée par le joueur. Utilisé quand le joueur doit compléter le pari de quelqu'un.
    protected boolean dansJeu; //True si le joueur est dans le jeu (N'a pas perdu).
    protected boolean allIn; //True si le joueur a mis All In dans son dernier tour
    protected int valeurAllInIncomplet; //True si le joueur a accepté un pari plus grande que sa quantité d'argent et a parié tout son argent.
    protected boolean potsDejaCompletes; //True si le calcul du pot secondaire du joueur est fini.
    protected int pariDerniereTournee; // valeur que le joueur a parié lors de la derniere tournee de paris
    protected boolean dansTourneeParis; // True par default, mais mis false si le joueur fold;

    public Joueur(String nom, boolean humain){
        this.nom=nom;
        this.argent=0;
        this.dealer=false;
        this.bigBlind=false;
        this.smallBlind=false;
        this.coup ="";
        this.humain=humain;
        this.derniereValeurPariee=0;
        this.dansJeu=true;
        this.dansTourneeParis=true;
        this.allIn=false;
        this.valeurAllInIncomplet=0;
        this.potsDejaCompletes = false;
    }

    /*
    Retourne la hand actuelle du joueur en forme de ll de cartes
     */
    public Hand getHand(){
        return hand;
    }

    /*
    Permet de définir toutes les cartes de la hand du joueur (2 sur main + 5 sur table)
    @param - LinkedList<Carte> cartesSurMain - Les 2 cartes sur la main du joueur
    @param - LinkedList<Carte> cartesSurTable - Les 5 cartes sur la table
     */
    public void setHand(LinkedList<Carte> cartesSurMain, LinkedList<Carte> cartesSurTable){
        hand.definirSurMainEtSurTable(cartesSurMain,cartesSurTable);
    }

    /*
    Utilisé lorsque le joueur est le Big Blind et doit payer son tour
    @param - Jeu jeu - Utilisé pour récupérer la valeur du Big Blind et pour mettre à jour la GUI.
     */
    public void payerBigBlind(Jeu jeu){
        int bigBlind = jeu.valeurBigBlind;
        coup = "Paye BigBlind "+bigBlind;
        parier(bigBlind);
        derniereValeurPariee=bigBlind;
        jeu.potActuel=jeu.potActuel+bigBlind;
        jeu.fenetre.mettreAJourInfosJoueur(this);
    }

    /*
    Utilisé lorsque le joueur est le Small Blind et doit payer son tour
    @param - Jeu jeu - Utilisé pour récupérer la valeur du Small Blind et pour mettre à jour la GUI.
     */
    public void payerSmallBlind(Jeu jeu){
        int smallBlind = jeu.valeurSmallBlind;
        coup = "Paye SmallBlind "+smallBlind;
        parier(smallBlind);
        derniereValeurPariee=smallBlind;
        jeu.potActuel=jeu.potActuel+smallBlind;
        jeu.fenetre.mettreAJourInfosJoueur(this);
    }

    public void jouer(Jeu jeu){
        if(humain){
            jeu.fenetre.afficherBoutons();
        }else{
            (Ordinateur)this.jouer(jeu.pariActuel, jeu);
        }
    }

    /*
    Définit l'action du joueur, entre coucher ses cartes et sortir de la tournée, accepter le pari actuel ou augmenter
    le pari actuel. Une quatrième action pré-définie, si le joueur a mis All In dans son dernier tour, on passe
    automatiquement au prochain joueur vu que celui qui a mis All In n'a aucune action possible autre qu'attendre la
    fin de la tournée. On définit aussi le coup du jour selon son action et un possible changement de son argent.
        -Coucher ses cartes (fold, action=-1):
            On tourne les cartes du joueur dans l'interface graphique, et on sort ce joueur de la liste de joueurs joueurs
            dans la tournée du jeu.
        -Ne pas parier ni payer (Check, action = 0):

        -Accepter le pari (Call ou Check, action=1):
            Call si le pari actuel est différent de 0, ou si le joueur a déjà payé le pari actuel, sinon check.
            Ici, si le pari est supérieur au argent du joueur il est obligé à parier tout son argent pour continuer dans
            la tournée, s'il le fait, on crée un pot secondaire pour lui et on le met dans la liste de pots secondaires du jeu.
            Si la valeur pariée est différente de 0 on met à jour son argent, sa dernière valeur pariée, la GUI et les pots
            secondaires des autres joueurs dans le jeu si c'est le cas.

        -Augmenter le pari (Raise, action > 1):
            Le joueur a augmenté le pari actuel, dans ce cas, le joueur antérieur à lui est le nouveau dernier a parier.
            On met à jour l'argent, la GUI et les pots secondaires des joueurs si c'est le cas.

    Après les action liés au choix du joueur, on vérifie si le même était le dernier a parier, si oui ou appelle la méthode
    tourDeParisFini(), sinon on appelle la méthode prochainJoueur(), les deux de jeu.

    @param - int action - L'action du joueur, donnée soir par l'intelligence dans le cas des joueurs ordinateurs, soit
    par le clique d'un bouton dans l'interface graphique dans le cas du joueur humain.
    @param Jeu jeu - le jeu, utilisée pour contrôler le déroulement
     */
    public void setAction(int action, int valeurPari, Jeu jeu) throws Exception {
        //Cas joueur a mis All In lors de son dernier tour
        System.out.println("Set Action começou " + nom);
        System.out.println("Sur Main: " );
        int i =0;
        while(i<getHand().getSurMain().size()){
            System.out.print(getHand().getSurMain().get(i).toString() + " ");
            i++;
        }
        i=0;
        System.out.println("");
        if(allIn || !dansTourneeParis){
                jeu.prochainJoueur();
        }
        else {
            //Cas joueur a couché ses cartes et ne participe plus
            if (action < 0) {
                coup = "Fold";
                dansTourneeParis = false;
                jeu.fenetre.enleverCartesJoueur(this);
                jeu.sortirDeLaTournee(this);
            } else if(action == 0){ //check
                if(valeurPari == 0){
                    coup = "Check";
                }// else, ne rien faire, juste passer à la prochaine tournee -> fait à la fin de la methode
            } else if (action == 1) { // call

                //Cas où la valeur de pari est supérieure à l'argent du joueur - All in pour jouer
                if (valeurPari >= (argent + derniereValeurPariee)) {
                    valeurAllInIncomplet = argent + derniereValeurPariee;
                    if (jeu.potsSecondairesDansJeu) {
                        jeu.completerPotsSecondaires(valeurAllInIncomplet);
                    }
                    coup = "Call - All in " + (argent);
                    valeurAllInIncomplet = argent + derniereValeurPariee;
                    jeu.potActuel = jeu.potActuel + argent;
                    jeu.creerPotsSecondaires(this);
                    parier(argent);
                    derniereValeurPariee = argent;
                } else {
                    if (jeu.potsSecondairesDansJeu) {
                        jeu.completerPotsSecondaires(valeurPari);
                    }
                    coup = "Call " + (valeurPari - derniereValeurPariee);
                    parier(valeurPari - derniereValeurPariee);
                    jeu.potActuel = jeu.potActuel + (valeurPari - derniereValeurPariee);
                    derniereValeurPariee = valeurPari;
                }

            }
            //Cas joueur a augmenté le pari, action contient le montant du pari
            else {
                if (action >= (derniereValeurPariee + argent)) {
                    if (jeu.potsSecondairesDansJeu) {
                        jeu.completerPotsSecondaires(argent);
                    }
                    coup = "All in " + (argent);
                    if(jeu.pariActuel<=argent){ // si le all in du joueur est plus petit que le pariActuel, le pariActuel reste
                        jeu.pariActuel = argent;
                    }
                    jeu.potActuel = jeu.potActuel + (derniereValeurPariee);
                    parier(action);
                } else {
                    if (jeu.potsSecondairesDansJeu) {
                        jeu.completerPotsSecondaires(action - derniereValeurPariee);
                    }
                    jeu.pariActuel = action;
                    jeu.potActuel = jeu.potActuel + (action - derniereValeurPariee);
                    coup = "Raise " + (action);
                    parier(action - derniereValeurPariee);
                }
                jeu.dernierAParier = (jeu.joueursDansLaTournee.getNodeAnterieur(this)).joueur;
                derniereValeurPariee = action;
                jeu.nJoueursQuiOntPayeLeTour = 0;
                jeu.fenetre.effacerCoupsJoueur();
            }
            jeu.fenetre.mettreAJourInfosJoueur(this);
            jeu.fenetre.mettreAJourValuerPot();
            if (jeu.joueurActuel.joueur.equals(jeu.dernierAParier)) {
                jeu.tourDeParisFini();
            } else {
                if (action != -1) {
                    jeu.nJoueursQuiOntPayeLeTour++;
                }

            }
            System.out.println("Set Action terminou " + nom);
            while(i<getHand().getSurMain().size()){
                System.out.print(getHand().getSurMain().get(i).toString() + " ");
                i++;
            }
            System.out.println(" ");
            System.out.println(" ");

            jeu.prochainJoueur();

        }
    }

    /*
    La méthode ne fait que commencer un Thread qui appelle la méthode setAction. Le Thread est nécessaire pour assurer
    la continuité de l'interface graphique.
    Tous les paramètres sont utilisés lors du appel de setAction().
     */
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

    /*
    Utilisée pour définir les rôles de chaque joueur lors de la création d'une nouvelle tournée
    La valeur des paramètres est passée aux respectifs attributs.
     */
    public void setRoleJeu(boolean dealer, boolean smallBlind, boolean bigBlind, boolean playing){
        this.dealer=dealer;
        this.smallBlind=smallBlind;
        this.bigBlind=bigBlind;
        this.playing=playing;
    }

    /*
    Utilisée lors de la création d'une nouvelle tournée, pour réinitialiser les paramètres de contrôle du jeu.
     */
    public void resetRolesJeu(){
        dealer=false;
        bigBlind=false;
        smallBlind=false;
        playing=false;
        dansTourneeParis=true;
        derniereValeurPariee=0;
        this.allIn=false;
        this.potsDejaCompletes=false;
        this.valeurAllInIncomplet=0;
    }
    public void prochaineTournee(int moment){
       hand.setHandMoment(moment);
    }

    /*
    Retourne une liste avec les cartes sur la main de joueur.
     */
    public LinkedList<Carte> getCartesSurMain(){return hand.getSurMain();}

    /*
    Retourne une liste avec les cartes qui forment la hand du joueur (Ex.: Flush, pair...)
     */
    public LinkedList<Carte> getCartesHand(){
        return hand.getCartesHand();
    }

    /*
    Permet au joueur de parier, si la quantité à parier est égale à la somme d'argent, définit comme true le boolean allIn.
    @param int q - quantité à parier
     */
    public void parier(int q){
        if(q>=argent) {
            q=argent;
            allIn=true;
        }
        pariDerniereTournee = q;
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

        if (this.getHand().getValeurHandFinale() > ((Joueur)j2).getHand().getValeurHandFinale()) {
            comparaison = 1;
        } else if (this.getHand().getValeurHandFinale() < ((Joueur)j2).getHand().getValeurHandFinale()) {
            comparaison = -1;
        }
        return comparaison;
    }

    public boolean equals(Object o){
        Joueur j = (Joueur)o;
        return j.nom.equals(this.nom);
    }


    public String toString(){
        return "Joueur: "+nom;
    }
}
