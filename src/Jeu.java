

import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.LinkedList;

public class Jeu {

    private CircularLinkedList joueurs; //essai avec circular LL
    private Paquet paquet; //Le paquet du jeu - remplie dans le constructeur
    private Table table;
    private int niveau;
    private int nJoueurs; //Le numéro actuel de joueurs dans le jeu

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
        nomJoueurHumain = "BALTAZAR"; // ça viendra du constructeur, mais mis par default pour simplicite
        joueurs.addNode(new Joueur(nomJoueurHumain));

        for(int i=1;i<nJoueurs;i++){
            Joueur j = new Joueur(niveau); // Ajouté pour pouvoir ajouter aussi a joueursCirc sans avoir a tt enlever
            if(i==2){
                j.dealer=true;
            }
            if(i==3){
                j.smallBlind=true;
            }
            if(i==4){
                j.bigBlind=true;
            }
            if(i==5){
                j.playing=true;
            }
            joueurs.addNode(j);
        }
        paquet= new Paquet();
        table = new Table();
        distribuerCartesJoueurs();
        distribuerCartesTable();
        setHands();
        distribuerArgent(3000);
    }
    /*
    public Jeu(int niveau, char c){ //boolean juste pour differencier de l'autre methode
        this.nJoueurs=6;
        if(c=='d'){
            for(int i=0;i<nJoueurs;i++){
                Joueur j = new Joueur ((char)i);
                if(i==4){
                    j.dealer = true;
                }
                joueursCirc.addNode(j);
            }
        }
        if(c=='a'){
            for(int i=0;i<nJoueurs;i++){
                Joueur j = new Joueur ("joueur " +i);
                if(i==4){
                    j.playing = true;
                }
                joueursCirc.addNode(j);
            }
        }
    }

     */


    /*
    Retourne une ll avec les cartes dans la table
     */
    public LinkedList<Carte> getCartesTable(){
        return table.getTable();
    }

    /*
    Retourne une linked list circulaire avec les joueurs dans le jeu
     */
    public CircularLinkedList getJoueurs(){
        return joueurs;
    }

    /*
    Méthode enlève le joueur donné en paramètre et met à jour nJoueurs.
    @param Joueur j - Joueur à enlever.
     */
    public void sortirJoueur(Joueur j) {
        joueurs.remove(j);
        nJoueurs--;
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
        (current.prochainNode.prochainNode.prochainNode.prochainNode).joueur.playing = true;
    }

    public void avancerJeu(){ // methode a appeller des qu'une decision est prise par le joueur actif (Peut etre inutile)
        Node current = joueurs.head; // toujours partir de la tete de la liste
        do {
                current = current.prochainNode;
        } while (!current.joueur.playing);
        current.joueur.playing = false;
        current.prochainNode.joueur.playing = true;

    }
    /*
                        Methode pour definir l'ordre du premier tour de decisions à partir du BB
     */
    public void definirPositionsBB() {
        Node current = joueurs.head; // partir de la tete de la liste jusqua trouver le BB
        int pos = 0; // ça va définir l'ordre sur le tour de paris
        do {
            current = current.prochainNode;
        } while (!current.joueur.bigBlind);// en sortant de la boucle current == BB
        Node bigBlind = current;
        do {
            current = current.prochainNode;
            current.joueur.position = pos; //celui juste apres le BB commence à decider
            pos++; //incrementation de la position pour que ça augmente au fur et a mesure
        } while (current!=bigBlind);

        // à la fin le but est que celui a gauche du BB ait pos == 0 et BB ait pos la plus grande
    }
    /*
                    Méthode pour définir l'ordre des prises de decision suite a un pari
                                (Donc, à être appelée si qqun parie)
     */
    public void definirPositionsPari(Joueur joueurQuiAParie) {
        Node current = joueurs.head; // partir de la tete de la liste jusqua trouver le BB
        int pos = 0; // ça va définir l'ordre sur le tour de paris
        do {
            current = current.prochainNode;
        } while (current.joueur!=joueurQuiAParie);// en sortant de la boucle current == BB
        Node aParie = current; // Node correspondant au joueur qui a parie
        do {
            current = current.prochainNode;
            current.joueur.position = pos; //celui juste apres le BB commence à decider
            pos++; //incrementation de la position pour que ça augmente au fur et a mesure
        } while (current!=aParie);
        // à la fin le but est que celui a gauche du aParie ait pos == 0 et BB ait pos la plus grande
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
            current = current.prochainNode;
            current.joueur.position = pos; //celui juste apres le dealer commence à decider
            pos++; //incrementation de la position pour que ça augmente au fur et a mesure
        } while (current != dealerNode);
        // à la fin le but est que celui a gauche du dealer ait pos == 0 et le dealer ait pos la plus grande
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

    public CircularLinkedList getJoueursCirc(){
        return joueurs;
    }

    public boolean ajouterKicker (LinkedList<Joueur> joueursEgaux){
        for (Joueur j : joueursEgaux) {
            /*
            Creation d'une linked list kicker qui contient toutes les 7 cartes du joueur, sauf celles utilisees pour
            former la hand actuelle (pair, flush, etc)
            La liste kicker est triee en ordre decroissante
             */
            LinkedList<Carte> kickers = j.getHand().getToutesCartes();
            LinkedList<Carte> kickersSurMain = j.getHand().getSurMain();
            kickers.removeAll(j.getHand().getCartesHand());
            kickersSurMain.removeAll(j.getHand().getCartesHand());
            kickers.sort(Collections.reverseOrder());
            kickersSurMain.sort(Collections.reverseOrder());

            /*
            S'il n'y a pas de kickers sur la main du joueur, on passe au prochain joueur
             */
            if (kickersSurMain.size() < 1) {
                j.getHand().ajouterDescription(". Partage du pot");
            }
            /*
            S'il y a kickers sur la main du joueur, on les cherche selon la hand
             */
            else {
                /*
                Cas carte haute
                 */
                if (j.getHand().getValeurHand() <= 14) {
                /*
                Si un des 4 kickers plus hauts est sur la main du joueur, sa valeur est ajoutee a la valeur de la hand
                 */
                    if (kickersSurMain.contains(kickers.getFirst()) || kickersSurMain.contains(kickers.get(1)) ||
                            kickersSurMain.contains(kickers.get(2)) || kickersSurMain.contains(kickers.get(3))) {
                        j.getHand().ajouterValeurKicker(kickersSurMain.getFirst().valeur);
                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                    /*
                    Si les 4 kicker sont sur la table, est impossible que le jouer gagne en cas de match nul
                    */
                    else {
                        j.getHand().ajouterDescription(". Partage du pot");
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
                        j.getHand().ajouterValeurKicker(kickersSurMain.getFirst().valeur);
                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                    /*
                    Les kickers ne sont pas sur la main du joueur
                    */
                    else {
                        j.getHand().ajouterDescription(". Partage du pot");
                    }
                }

                /*
                Cas deux pairs
                */
                else if (j.getHand().getValeurHand() >= 230 && j.getHand().getValeurHand() <= 1530) {
                    /*
                    Si le kicker est sur la main du joueur, sa valeur est ajoutee a la valeur de la hand
                    */
                    if (kickersSurMain.contains(kickers.getFirst())) {
                        j.getHand().ajouterValeurKicker(kickersSurMain.getFirst().valeur);
                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                    /*
                    Le kicker n'est pas sur la main du jouer
                    */
                    else {
                        j.getHand().ajouterDescription(". Partage du pot");
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
                        j.getHand().ajouterValeurKicker(kickersSurMain.getFirst().valeur);
                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                    /*
                    Les kicker ne sont pas sur la main du joueur
                     */
                    else {
                        j.getHand().ajouterDescription(". Partage du pot");
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
                        j.getHand().ajouterValeurKicker(kickersSurMain.getFirst().valeur);
                        j.getHand().ajouterDescription(". Kicker " + kickersSurMain.getFirst().description(false));
                        j.getHand().ajouterCarteKicker(kickersSurMain.getFirst());
                    }
                    /*
                    Le kicker n'est pas sur la main du joueur
                     */
                    else {
                        j.getHand().ajouterDescription(". Partage du pot");
                    }
                }
                /*
                Une des hands où il n'y a pas de kicker (straight, flush, full house, straight flush et royal straight flush)
                 */
                else {
                    j.getHand().ajouterDescription(". Partage du pot");
                }
            }
        }

        joueursEgaux.sort(Collections.reverseOrder());
        /*
        On arrive a enlever l'egalite en ajoutant un kicker
         */
        /*
        L'inegalite n'est pas suprimee
         */
        return joueursEgaux.getFirst().getHand().getValeurHand() > joueursEgaux.get(1).getHand().getValeurHand();
    }

    public LinkedList<Joueur> joueursGagnants(){
        LinkedList<Joueur> tousJoueurs = new LinkedList<>();
        for(Node n : joueurs.joueurs){
            tousJoueurs.add(n.joueur);
        }
        LinkedList<Joueur> gagnants = new LinkedList<>();

        //Triée selon la valeur de la hand
        tousJoueurs.sort(Collections.reverseOrder());


        if (tousJoueurs.getFirst().getHand().getValeurHand() > tousJoueurs.get(1).getHand().getValeurHand()){
            gagnants.add(tousJoueurs.getFirst());
        }

        else {
            for(int i=1;i< tousJoueurs.size();i++){

                int plusHauteHand = tousJoueurs.getFirst().getHand().getValeurHand();
                Joueur candidat = tousJoueurs.get(i);

                if(candidat.getHand().getValeurHand() != plusHauteHand){
                    tousJoueurs.remove(candidat);
                }

            }

            /*
            On ajoute la valeur du kicker sur la hand de chacun des joueurs égaux, et on trie la liste
            une nouvelle fois par valeur de la hand pour vérifier si l'égalité a été enlevée
             */
            ajouterKicker(tousJoueurs);
            tousJoueurs.sort(Collections.reverseOrder());
            int plusHauteHand = tousJoueurs.getFirst().getHand().getValeurHand();

            if (plusHauteHand > tousJoueurs.get(1).getHand().getValeurHand()){
                /*
                Dans ce cas l'égalité a bien été enlevée.
                 */
                gagnants.add(tousJoueurs.getFirst());
            }
            else {
                /*
                Si l'égalité existe toujours, on enlève tous les joueurs qui ont une valeur de hand inférieure
                à celle de la plus haute hand, et on ajoute tous les joueurs qui ont une valeur de hand égale à
                celle de la plus haute hand à la ll gagnants.
                 */
                for (int i=1; i<tousJoueurs.size(); i++){
                    Joueur candidat = tousJoueurs.get(i);
                    if(candidat.getHand().getValeurHand() != plusHauteHand){
                        tousJoueurs.remove(candidat);
                    }
                }
                gagnants.addAll(tousJoueurs);
            }
        }

        return gagnants;
    }

    /*
    public static void main(String[] args) {
        Jeu j = new Jeu(9,0);
        System.out.println("Valeurs des hands:\n");
        Node current = j.getHead();
        for(int i=0; i<j.nJoueurs;i++){
            System.out.println(current.joueur.nom+" : "+current.joueur.getHand().getValeurHand()+"\n");
            current=current.prochainNode;
        }
    }

     */
}
