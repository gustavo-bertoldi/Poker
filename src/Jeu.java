

import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.LinkedList;

public class Jeu {
    private FenetreJeuV2 fenetreJeu;

    private CircularLinkedList joueurs; //essai avec circular LL
    private Paquet paquet; //Le paquet du jeu - remplie dans le constructeur
    private Table table;
    private int moment; // 0 = preFlop; 1 = flop; 2 = turn; 3 = river; 4 = tout le monde fold;
    private int niveau;
    private int nJoueurs; //Le numéro actuel de joueurs dans le jeu
    private LinkedList<Joueur> joueursGagnants;
    private  LinkedList<String> nomsJoueursOrdinateurs;
    protected int valeurSmallBlind; //La valeur du small blind actuel
    protected int valeurBigBlind;
    protected int valeurCall; //La valeur minimale de pari pour jouer, défini en fonction des paris des joueurs
    protected int pariActuel;
    protected String nomJoueurHumain;

    /*      DÉROULEMENT JEU
    - Chaque joueur a, comme attribut, deux infos: dansJeu(pas foldé) comme position(par rapport au tour de paris)
    - Au début, pos Dealer = joueurs.size()-3, pos SB = joueurs.size()-2, posBB = joueurs.size()-1 étant le dernier a decider
    - S'il y a un pari, le joueur qui a parié prend la position joueurs.size() et tous les autres changent aussi.

     */

    /*
    Jeu a deux constructeurs, un prend en paramètre:
    @param int nJoueurs - numéro de joueurs
    @param int smallBlind - la valeur de la première small blind
    @param int niveau - pour contrôler l'intelligence de l'ordinateur

    Le constructeur cree un joueur human, et nJoueurs-1 ordinateurs, un paquet de cartes
    une table et fait la distribution des cartes et de l'argent entre les joueurs et la table

    Il change aussi l'icon des cartes du joueur pour q'elles soient affichées dans l'interface graphique
     */
    public Jeu(int nJoeurs, int niveau){  // méthode utilisée pour l'instant
        this.nJoueurs=nJoeurs;
        this.niveau = niveau;
        this.joueurs = new CircularLinkedList();
        creerListeNomsJoueursOrdinateurs();
        nomJoueurHumain = "BALTAZAR"; // ça viendra du constructeur, mais mis par default pour simplicite
        joueurs.addNode(new Joueur(nomJoueurHumain));
        for(int i=1;i<nJoueurs;i++){
            int r = (int)((nomsJoueursOrdinateurs.size())*Math.random());
            Joueur j = new Joueur(nomsJoueursOrdinateurs.get(r), niveau); // Ajouté pour pouvoir ajouter aussi a joueursCirc sans avoir a tt enlever
            nomsJoueursOrdinateurs.remove(r);
            if(i==2){
                j.dealer=true;
            }
            if(i==3){
                j.smallBlind=true;
            }
            if(i==4){
                j.bigBlind=true;
            }
            joueurs.addNode(j);
        }


        paquet= new Paquet();
        table = new Table();
        distribuerCartesJoueurs();
        distribuerCartesTable();
        setHands();
        distribuerArgent(3000);
        fenetreJeu = new FenetreJeuV2(this);
        definirPositionsBB();

    }

    /*
    Retourne une ll avec les cartes dans la table
     */
    public LinkedList<Carte> getCartesTable(){
        return table.getTable();
    }


    public int getNJoueurs(){
        return nJoueurs;
    }

    /*
    Méthode enlève le joueur donné en paramètre et met à jour nJoueurs.
    @param Joueur j - Joueur à enlever.
     */
    public void sortirJoueur(Joueur j) {
        joueurs.remove(j);
        nJoueurs--;
    }

    public LinkedList<Joueur> getJoueursGagnants(){
      if(joueursGagnants== null){
          return joueursGagnants;
      }else {
          return joueursGagnants;
      }
    }
    public void setFenetreJeu(FenetreJeuV2 f){
        fenetreJeu = f;
    }


    private void distribuerCartesJoueurs(){
        System.out.println("Cartes joueur: ");

        Node current = joueurs.head;

        for(int i=0; i<nJoueurs; i++){
            System.out.println(current.joueur.nom);
            LinkedList<Carte> cartesJoueur = new LinkedList<>();
            int r = (int)((paquet.size())*Math.random());
            Carte c=paquet.get(r);
            cartesJoueur.add(c);
            System.out.println(paquet.get(r).toString());
            paquet.remove(c);
            r = (int)((paquet.size())*Math.random());
            c=paquet.get(r);
            cartesJoueur.add(c);
            System.out.println(paquet.get(r).toString());
            paquet.remove(c);
            current.joueur.setCartesSurMain(cartesJoueur);
            current=current.prochainNode;
            System.out.println("");
        }
    }

    private void distribuerCartesTable(){
        System.out.println("Cartes table");
        LinkedList<Carte> cartesTable = new LinkedList<>();
        for(int i = 0 ; i < 5 ; i++){
            int m = (int) ((paquet.size()) * Math.random());
            Carte c = paquet.get(m);
            cartesTable.add(c);
            System.out.println(c.toString());
            paquet.remove(c);
        }
        table.setCartesTable(cartesTable);
    }

    private void setHands(){
        Node current = joueurs.head;
        for(int i=0; i<nJoueurs; i++){
            current.joueur.setHand(current.joueur.getCartesSurMain(),getCartesTable());
            current=current.prochainNode;
        }
    }

    private void distribuerArgent(int q){
        Node current = joueurs.head;

        for(int i=0; i<nJoueurs; i++){
            current.joueur.setArgent(q);
            current=current.prochainNode;
        }
    }
     /*
                                        Méthodes pour dérroulement
     */

    /*
                                    Méthode pour changer Dealer, SB et BB
     */
    public void changerDealer(){ //FONCTIONNELLE VOIR MAIN
        Node current = joueurs.head; // toujours partir de la tete de la liste
        do{
            current = current.prochainNode;
        }while(!current.joueur.playing );
        current.joueur.playing=false; // parcourir la table jusqua trouver le dernier à jouer et lui affecter false pour playing
        current = joueurs.head;
        do{
            current = current.prochainNode;
        }while(!current.joueur.dealer ); // parcourir CLL jusqu'a ce que l'on trouve le dealer
        (current.joueur).dealer = false; //Dealer ne l'est plus
        (current.prochainNode.joueur).smallBlind = false; // SB ne l'est plus
        (current.prochainNode.joueur).dealer = true; // SB deviant dealer
        (current.prochainNode.prochainNode).joueur.bigBlind = false; // BB ne l'est plus
        (current.prochainNode.prochainNode).joueur.smallBlind = true; // BB deviant SB
        (current.prochainNode.prochainNode.prochainNode).joueur.bigBlind = true; // le prochain joueur deviant BB
        // (current.prochainNode.prochainNode.prochainNode.prochainNode).joueur.playing = true; fait sur definir positions
        definirPositionsBB(); //SB et BB payent automatiquement
    }


    /*                              Idée derrière l'avancement du jeu
    0) SB et BB placent leur bets -> definirPositionsBB() le fait
    1) On définit l'ordre des prises de décision avec definirPositionsBB() -> appellée sur changer dealer
    -> après l'appel à la méthode, on aura les positions comme le suivant:
    D (nJoueurs-3) SB (nJoueurs-2) BB (nJoueurs-1) J1 (0) J2 (1) J3(2)...
    Le joueur J1 a comme position 0, et a playing == true; tous les autres playing == false.
    2) Puis, on commence avec le joueur de position 0: il pourra (call, fold, raise) (dans un premier temps call)
        3) Si fold, j.dansJeu =false, puis, passer au joueur de position == position +1;
        4) Si call, passer au joueur de position == position+1;
        5) Si raise, redefinir positions de tous les joueurs, étant lui celui de position == 0, puis passer a celui de position==position+1;
    6) Pour savoir si joueur va jouer on regarde j.dansJeu
    7) On tourne la table jusqua ce qu'on arrive à position == nJoueurs, donc juste après le joueur de position nJ-1 avoir joue
    8) On redéfinit les positions de façon à ce que le SB soit celui de position 0 et le Dealer celui de position nJ-1 definirPositionsDealer();
                                 Dès que le tour est fini:
    9) ajouter l'argent parié au pot -> changer label
    10) recommencer le parcours de la table par les positions en vérifiant dansJeu;
                                 Dès que la main est finie:
    11) redefinir Dealer;
    12) redefinir Positions;
    13) tous dansJeu = true;
    14) Recommencer de 0;
     */

    public void next(){
        Node current = joueurs.getJoueurPlaying(); //ce joueur est playing et pos 0
        if(current.joueur.position!=0){
            if(current.equals(joueurs.head)){
                fenetreJeu.montrerBoutons();
            } else {
                current.joueur.jouer(pariActuel, true);
            }if(current.joueur.dejaJoue){
                fenetreJeu.mettreAJourInfosJoueur(current.joueur);
            }
        } else{
            if(moment == 0 && current.joueur.bigBlind){
                if(current.equals(joueurs.head)){
                    fenetreJeu.montrerBoutons();
                } else{
                    current.joueur.jouer(pariActuel, true); // à la fin de jouer, appel à next()
                }
            }else{
                avancerJeu();
            }
        }
    }
    public void avancerJeu(){
        if(getNumDansJeu()==1){
            //changerDealer();
            fenetreJeu.restart();
            moment =0;
        }else {
            if (moment == 0) {
                fenetreJeu.flop();
                definirPositionsDealer();
            } else if (moment == 1) {
                fenetreJeu.turn();
                definirPositionsDealer();
            } else if (moment == 2) {
                fenetreJeu.river();
                definirPositionsDealer();
            } else if (moment == 3) {
                joueursGagnants= null;
                fenetreJeu.restart();
                moment = -1;
            }
            moment++;
        }
    }

    public int getNumDansJeu(){ //Parcourir la table et trouver numero de joueurs qui n'ont pas foldé
       Node current = joueurs.head.prochainNode;
       int joueursActifs = 0;
       do{
           if(current.joueur.dansJeu){
               joueursActifs++;
           }
           current = current.prochainNode;
       }while(!current.equals(joueurs.head));
       return joueursActifs;
    }
    /*
                        Methode pour definir l'ordre du premier tour de decisions à partir du BB
     */
    public void definirPositionsBB() {
        Node current = joueurs.head; // partir de la tete de la liste jusqua trouver le BB
        int pos = 0; // ça va définir l'ordre sur le tour de paris
        do {
            current = current.prochainNode;
            if(current.joueur.smallBlind){
                current.joueur.parier(valeurSmallBlind);
            }
            if(current.joueur.bigBlind){
                current.joueur.parier(valeurBigBlind);
            }
        } while (!current.joueur.bigBlind);// en sortant de la boucle current == BB
        Node bigBlind = current;
        do {
            current.joueur.position = pos; // BB pos ==0
            pos++; //incrementation de la position pour que ça augmente au fur et a mesure
            current = current.prochainNode;
        } while (current.prochainNode!=bigBlind);
        //next();
        // à la fin le but est que celui a gauche du BB ait pos == 1 et BB ait pos 0
    }
    /*
                    Méthode pour définir l'ordre des prises de decision suite a un pari
                                (Donc, à être appelée si qqun parie)
     */
    public void definirPositionsPari(Joueur joueurQuiAParie) { //a appeller des que qqun parie
        Node current = joueurs.head; // partir de la tete de la liste jusqua trouver le joueur qui a parie
        int pos = 0; // ça va définir l'ordre sur le tour de paris
        do {
            current = current.prochainNode;
        } while (current.joueur!=joueurQuiAParie);// en sortant de la boucle current == joueurQuiAParie
        Node aParie = current; // Node correspondant au joueur qui a parie
        do {
            current.joueur.position = pos; //fin du parcours de la table se fait si position == 0, donc pos jQAP = 0;
            pos++; //incrementation de la position pour que ça augmente au fur et a mesure
            current = current.prochainNode;
        } while (current!=aParie);
        //next();
    }
    /*
                    Methode pour definir les positions en fonction de la position du dealer
                                (utilisée pour fin de tour de paris)
     */
    public void definirPositionsDealer() {
        Node current = joueurs.head; // partir de la tete de la liste jusqua trouver le Dealer
        int pos = 0; // ça va définir l'ordre sur le tour de paris
        do {
            current = current.prochainNode;
        } while (!current.joueur.dealer);// en sortant de la boucle current == BB
        Node dealerNode = current; // Node correspondant au joueur qui a parie
        do {
            current.joueur.position = pos; //celui juste apres le dealer commence à decider
            pos++; //incrementation de la position pour que ça augmente au fur et a mesure
            current = current.prochainNode;
        } while (current != dealerNode);
        //next();
        // à la fin le but est que celui a gauche du dealer ait pos == 0 et le dealer ait pos la plus grande
    }
    public void recommencer(){
        paquet= new Paquet();
        table = new Table();
        distribuerCartesJoueurs();
        distribuerCartesTable();
        setHands();

    }

    public Joueur getHeadJoueur(){
        return joueurs.head.joueur;
    }

    public Node getHead(){
        return joueurs.head;
    }

    public Joueur getTailJoueur(){ // PE inutile
        return joueurs.tail.joueur;
    }

    public boolean ajouterKicker (LinkedList<Joueur> joueursEgaux){
        for (Joueur j : joueursEgaux) {
            /*
            Creation d'une linked list kicker qui contient toutes les 7 cartes du joueur, sauf celles utilisees pour
            former la hand actuelle (pair, brelan, etc)
            La liste kicker est triée en ordre décroissante
             */
            LinkedList<Carte> kickers = j.getHand().getToutesCartes();
            LinkedList<Carte> kickersSurMain = j.getHand().getSurMain();
            kickers.removeAll(j.getHand().getCartesHand());
            kickersSurMain.removeAll(j.getHand().getCartesHand());
            kickers.sort(Collections.reverseOrder());
            kickersSurMain.sort(Collections.reverseOrder());

            /*
            S'il n'y a pas de kickers sur la main du joueur, on passe au prochain joueur
            S'il y a kickers sur la main du joueur, on les cherche selon la hand
             */
            if(kickersSurMain.size() != 0) {
                /*
                Cas carte haute
                 */
                if (j.getHand().getValeurHand() <= 14) {
                /*
                Si un des 4 kickers plus hauts est sur la main du joueur, sa valeur est ajoutée a la valeur de la hand
                 */
                    if (kickersSurMain.contains(kickers.getFirst()) || kickersSurMain.contains(kickers.get(1)) ||
                            kickersSurMain.contains(kickers.get(2)) || kickersSurMain.contains(kickers.get(3))) {

                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                }

                /*
                Cas pair
                */
                else if (j.getHand().getValeurHand() >= 20 && j.getHand().getValeurHand() <= 140) {
                    /*
                    Si un des 3 kicker plus hauts est sur la main du jouer, sa valeur est ajoutee a la valeur de la hand
                    */
                    if (kickersSurMain.contains(kickers.getFirst()) || kickersSurMain.contains(kickers.get(1)) ||
                            kickersSurMain.contains(kickers.get(2))) {

                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                }

                /*
                Cas deux pairs
                */
                else if (j.getHand().getValeurHand() >= 230 && j.getHand().getValeurHand() <= 1530) {
                    /*
                    Si le kicker est sur la main du joueur, sa valeur est ajoutée a la valeur de la hand
                    */
                    if (kickersSurMain.contains(kickers.getFirst())) {
                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                }

                /*
                Cas brelan (three of a kind)
                 */
                else if (j.getHand().getValeurHand() >= 2000 && j.getHand().getValeurHand() <= 14000) {
                    /*
                    Au moins un des deux kicker est sur la main du joueur
                     */
                    if (kickersSurMain.contains(kickers.getFirst()) || kickersSurMain.contains(kickers.get(1))) {
                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                }

                /*
                Cas carré (four of a kind)
                 */
                else if (j.getHand().getValeurHand() >= 300002 && j.getHand().getValeurHand() <= 300014) {
                    /*
                    Le kicker est sur la main du joueur
                     */
                    if (kickersSurMain.contains(kickers.getFirst())) {
                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                }
                /*
                Si aucune action n'est faite, il n'y a pas de kicker pour cette hand (straight, flush, full house, straight flush et royal straight flush)
                 */

            }
        }

        joueursEgaux.sort(Collections.reverseOrder());

        return joueursEgaux.getFirst().getHand().getValeurHand() > joueursEgaux.get(1).getHand().getValeurHand();
    }

    public void trouverJoueursGagnants(){
        LinkedList<Joueur> tousJoueurs = joueurs.getLinkedListJoueurs();
        LinkedList<Joueur> joueursGagnants = new LinkedList<>();
        tousJoueurs.sort(Collections.reverseOrder());

        /*
        Cas où il y a un seul joueur gagnant
         */
        if (tousJoueurs.getFirst().getHand().getValeurHand() > tousJoueurs.get(1).getHand().getValeurHand()){
            joueursGagnants.add(tousJoueurs.getFirst());
        }

        else {
            int v1 = tousJoueurs.getFirst().getHand().getValeurHand();
            tousJoueurs.removeIf(joueur -> joueur.getHand().getValeurHand() < v1);
            /*
            Après cette boucle for, tous les joueurs qui ont une valeur de hand inférieure à celle de la hand plus haute
            ont été enlevés de la liste tousJoueurs. On ajoute la valeur du kicker (si applicable) dans la nouvelle liste
            et on vérifie si l'égalité a pu être enlevée.
             */

            if (ajouterKicker(tousJoueurs)) {
                /*
                Dans ce cas l'égalitée a pu être enlevée et on ajoute le seul joueur gagnant dans la liste joueursGagnants
                 */
                tousJoueurs.sort(Collections.reverseOrder());
                joueursGagnants.add(tousJoueurs.getFirst());
            } else {
                /*
                Ici, l'égalité reste même après l'ajout de la valeur du kicker. On enlève de la liste tous les joueurs qui
                ont une hand inférieure à la plus haute et on ajoute tous ceux qui ont une valeur de hand égale a la plus haute
                é joueursGagnants. La boucle commence dans l'indice 2 parce qu'on sait déjà que les joueurs d'indice 0 et 1
                ont une hand de même valeur.
                 */
                tousJoueurs.sort(Collections.reverseOrder());
                int v2 = tousJoueurs.getFirst().getHand().getValeurHand();
                tousJoueurs.removeIf(joueur -> joueur.getHand().getValeurHand() < v2);
                joueursGagnants.addAll(tousJoueurs);
                joueursGagnants.getFirst().getHand().ajouterDescription(". Partage du pot");
            }

        }

        this.joueursGagnants=joueursGagnants;
    }

    private void creerListeNomsJoueursOrdinateurs(){
        nomsJoueursOrdinateurs = new LinkedList<>();
        nomsJoueursOrdinateurs.add("Nicolas Stous");
        nomsJoueursOrdinateurs.add("Guy Atahanaze");
        nomsJoueursOrdinateurs.add("Adrien Petrov");
        nomsJoueursOrdinateurs.add("Valerie Kaftandjian");
        nomsJoueursOrdinateurs.add("Boran Kim");
        nomsJoueursOrdinateurs.add("Laurence Dupont");
        nomsJoueursOrdinateurs.add("Vladimir Lysenko");
        nomsJoueursOrdinateurs.add("Marie Aumeunier");
        nomsJoueursOrdinateurs.add("Elisabeth Aumeunier");
        nomsJoueursOrdinateurs.add("JP Devau");
        nomsJoueursOrdinateurs.add("Marie-Pierre Noutary");
        nomsJoueursOrdinateurs.add("Thomas Monnier");
        nomsJoueursOrdinateurs.add("Phillipe Guy");
        nomsJoueursOrdinateurs.add("Thomas Boulanger");
        nomsJoueursOrdinateurs.add("Aurelian Saulot");
        nomsJoueursOrdinateurs.add("Ali Belarouci");
        nomsJoueursOrdinateurs.add("Frederic Theoule");
        nomsJoueursOrdinateurs.add("Edicto Garay");
        nomsJoueursOrdinateurs.add("Nicolas Rosenstiehl");
        nomsJoueursOrdinateurs.add("Tarkan Gezer");
        nomsJoueursOrdinateurs.add("Tetyana Nychyporuk");
        nomsJoueursOrdinateurs.add("Laurence Barret");
        nomsJoueursOrdinateurs.add("Vincent Condat");
        nomsJoueursOrdinateurs.add("Eveline Manna");

    }
    public static void main(String[] args){
         new Jeu(6, 0);
    }
}