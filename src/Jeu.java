import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.LinkedList;

public class Jeu {

    private FenetreJeu fenetreJeu;
    private LinkedList<Joueur> joueurs = new LinkedList<Joueur>(); //Les joueurs dans le jeu - remplie dans le constructeur
    private CircularLinkedList joueursCirc = new CircularLinkedList(joueurs); //essai avec circular LL
    private Paquet paquet; //Le paquet du jeu - remplie dans le constructeur
    private Table table;
    private int niveau;
    private int valeurCall=200; //La valeur minimale de pari pour jouer, defini en fonction des paris des joueurs
    private int tourDealer=0; //L'indice du joueur qui sera le dealer dans la ll joueurs
    private int tourBig; //L'indice du jouer qui sera le big blind dans la ll joueurs
    private int tourSmall; //L'indice du joueur qui sera le small blind dans la ll joueurs
    private int nJoueurs; //Le numero actuel de joueurs dans le jeu
    protected int joueurActif; //L'indice du joueur qui sera le prochain a jouer
    private int smallBlind; //La valeur du small blind actuel
    private int bigBlind; //La valeur du big blind actuel (2*smallBlind)
    protected String nomJoueur;

    /*      DÉRROULEMENT JEU
    - Chaque joueur a, comme attribut, deux infos: dansJeu(pas foldé) comme position(par rapport au tour de paris)
    - Au début, pos Dealer = joueurs.size()-3, pos SB = joueurs.size()-2, posBB = joueurs.size()-1 étant le dernier a decider
    - S'il y a un pari, le joueur qui a parié prend la position joueurs.size() et tous les autres changent aussi.

     */

    /*
    Jeu a deux constructeurs, un prend en parametre:
    @param int nJoueurs - numero de joueurs
    @param int smallBlind - la valeur de la première small blind
    @param int niveau - pour controler l'intelligence de l'ordinateur

    Le constructeur cree un joueur human, et nJoueurs-1 ordinateurs, un paquet de cartes
    une table et fait la distribution des cartes et de l'argent entre les joueurs et la table

    Il change aussi l'icon des cartes du joueur pour q'elles soient affichées dans l'interface graphique
     */
    public Jeu(int nJoeurs, int niveau){  // methode utilisee pour l'instant
        this.nJoueurs=nJoeurs;
        this.niveau = niveau;
        nomJoueur = "BALTAZAR"; // ça viendra du constructeur, mais mis par default pour simplicite
        for(int i=0;i<nJoueurs;i++){
            Joueur j = new Joueur(niveau); // Ajouté pour pouvoir ajouter aussi a joueursCirc sans avoir a tt enlever
            joueurs.add(j);
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

            joueursCirc.addNode(j);

        }
        paquet= new Paquet();
        table = new Table();
        distribuerCartesJoueurs();
        distribuerCartesTable();
        setHands();
        distribuerArgent(1500);
        fenetreJeu = new FenetreJeu(this, nJoueurs);
    }

    /*
    Le deuxieme constructeur fait essentiellement la meme chose que le premier, mais
    on met le nombre joueurs en 6 par default.
     */
    public Jeu(int niveau){
        this.nJoueurs=6;
        joueurs.add(new Joueur(niveau));
        for(int i=1;i<6;i++){
            joueurs.add(new Ordinateur(niveau));
        }
        paquet= new Paquet();
        table = new Table();
        distribuerCartesJoueurs();
        distribuerCartesTable();
        distribuerArgent(1500);

    }

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


    /*
    Retourne une ll avec les cartes dans la table
     */
    public LinkedList<Carte> getCartesTable(){
        return table.getTable();
    }

    /*
    Retourne la table
     */
    public Table getTable(){
        return table;
    }

    /*
    Retourne une ll avec les joueurs dans le jeu
     */
    public LinkedList<Joueur> getJoueurs(){
        return joueurs;
    }

    /*
    Retourne la valeur minimale de pari pour continuer le jeu, valeur de call
     */
    public int getValeurCall(){
        return valeurCall;
    }

    /*
    Permet de changer la valeur minimale pour jouer
    @param int valeurCall - valeur a donner
     */
    public void setValeurCall(int valeurCall){
        this.valeurCall=valeurCall;
    }

    /*
    Permet de faire un joueur parier une certaine quantité
    @param int q - quantité a parier par le joueur
    @param int indiceJoueur - l'indice du joueur qui va parier dans la ll joueurs
     */
    public void parier(int q, int indiceJoueur){
        if(joueurs.get(indiceJoueur).parier(q)) {
            table.ajouterAuPot(q);
        }
    }

    /*
    Change l'icon des cartes de joueur humain pour les afficher dans
    l'interface graphique
     */
    private void montrerCartesJoueurActif() {
        for (Carte c : joueurs.get(0).getHand().getCartes()) {
            c.montrerCarte();
        }
    }

    /*
    Retourne la valeur de small blind Actuelle
     */
    public int getSmallBlind() {
        return smallBlind;
    }

    /*
    Retourne la valeur de big blind actuelle
     */
    public int getBigBlind(){
        return bigBlind;
    }

    /*
    Permet de changer les valeurs du small blind et du big blind actuelles
    @param int smallBlind - valeur a prendre
     */
    public void setSmallBlind(int smallBlind){
        this.smallBlind=smallBlind;
        this.bigBlind=2*smallBlind;
    }

    /*
    Quand un joueur perd, permet de le sortir du jeu bien comme de la ll de joueurs
    @param int indice- indice du joueur a sortir
     */
    public void sortirJoueur(int indice) {
        joueurs.remove(indice);
        nJoueurs--;
    }

    /*
    Methode private, retourne une ll avec les hands des joueurs dea triee
     */
    private LinkedList<Hand> getAllHands(){
        LinkedList<Hand> hands = new LinkedList<>();
        for(Joueur j:joueurs){
            hands.add(j.getHand());
        }
        Collections.sort(hands,Collections.reverseOrder());
        return hands;
    }

    /*
    Retourne la hand gagnante, si split pot, retou
     */
    public LinkedList<Hand> handsGagnantes(){
        LinkedList<Hand> handsGagnantes = new LinkedList<>();
        LinkedList<Hand> allHands = getAllHands();
        handsGagnantes.add(allHands.getFirst());
        for(int i=1;i<allHands.size();i++){
            if(allHands.get(i)==allHands.get(0)){
                handsGagnantes.add(allHands.get(i));
            }
        }
        return handsGagnantes;
    }

    private void distribuerCartesJoueurs(){ // la seule chose a changer pour joueursCirc est le parcours de la liste
        Node current = joueursCirc.head;
        do{
            LinkedList<Carte> cartesJoueur = new LinkedList<>();
            int i = (int) ((paquet.size()) * Math.random());
            cartesJoueur.add(paquet.get(i));
            paquet.remove(i);
            i = (int) ((paquet.size()) * Math.random());
            cartesJoueur.add(paquet.get(i));
            paquet.remove(i);
            current.joueur.setCartesSurMain(cartesJoueur);
            current = current.prochainNode;
        }while(!current.equals(joueursCirc.head));
        /* for (Joueur j : joueurs){
            LinkedList<Carte> cartesJoueur = new LinkedList<>();
            int i = (int) ((paquet.size()) * Math.random());
            cartesJoueur.add(paquet.get(i));
            paquet.remove(i);
            i = (int) ((paquet.size()) * Math.random());
            cartesJoueur.add(paquet.get(i));
            paquet.remove(i);
            j.setCartesSurMain(cartesJoueur);
        }

         */
    }

    private void distribuerCartesTable(){
        LinkedList<Carte> cartesTable = new LinkedList<>();
        for(int i = 0 ; i < 5 ; i++){
            int m = (int) ((paquet.size()) * Math.random());
            cartesTable.add(paquet.get(m));
            paquet.remove(m);
        }
        table.setCartesTable(cartesTable);
    }

    private void setHands(){
        Node current = joueursCirc.head;
        do{
            current.joueur.setHand(current.joueur.getCartesSurMain(),getCartesTable());
            current = current.prochainNode;
        }while(!current.prochainNode.equals(joueursCirc.head));
        /* for (Joueur j : joueurs){
            j.setHand(j.getCartesSurMain(),getCartesTable());
        }

        */
    }

    private void distribuerArgent(int q){
        Node current = joueursCirc.head;
        do{
            current.joueur.setArgent(q);
            current = current.prochainNode;
        }while(!current.equals(joueursCirc.head));
       /* for (Joueur j : joueurs){
            j.setArgent(q);
        }

        */
    }
     /*
                                        Méthodes pour dérroulement
     */

    /*
                                    Méthode pour changer Dealer, SB et BB
     */
    public void changerDealer(){ //FONCTIONNELLE VOIR MAIN
        Node current = joueursCirc.head; // toujours partir de la tete de la liste
        do{
            current = current.prochainNode;
        }while(!current.joueur.playing );
        current.joueur.playing=false; // parcourir la table jusqua trouver le dernier à jouer et lui affecter false pour playing
        current = joueursCirc.head;
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
        fenetreJeu.ajouterNomsJoueurs();
        fenetreJeu.repaint();
        fenetreJeu.revalidate();
    }

    public void avancerJeu(){ // methode a appeller des qu'une decision est prise par le joueur actif (Peut etre inutile)
        Node current = joueursCirc.head; // toujours partir de la tete de la liste
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
        Node current = joueursCirc.head; // partir de la tete de la liste jusqua trouver le BB
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
                    Methode pour definir l'ordre des prises de decision suite a un pari
                                (Donc, à être appellée si qqun parie)
     */
    public void definirPositionsPari(Joueur joueurQuiAParie) {
        Node current = joueursCirc.head; // partir de la tete de la liste jusqua trouver le BB
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
        Node current = joueursCirc.head; // partir de la tete de la liste jusqua trouver le Dealer
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
   public void reinitialiser(){  // methode utilisee pour l'instant
        paquet= new Paquet();
        table = new Table();
        distribuerCartesJoueurs(); // changer parcours de la liste
        distribuerCartesTable();
        setHands(); // parcours de liste
        fenetreJeu.dispatchEvent(new WindowEvent(fenetreJeu, WindowEvent.WINDOW_CLOSING));
        fenetreJeu = new FenetreJeu(this, nJoueurs);
    }

    public Joueur getHeadJoueur(){
        return joueursCirc.head.joueur;
    }

    public Node getHead(){
        return joueursCirc.head;
    }

    public Joueur getTailJoueur(){ // PE inutile
        return joueursCirc.tail.joueur;
    }

    public CircularLinkedList getJoueursCirc(){
        return joueursCirc;
    }


    public static void main(String[] args) {
        Jeu j = new Jeu(6,0);
    }
}
