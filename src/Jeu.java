import java.util.Collections;
import java.util.LinkedList;

public class Jeu {

    private CircularLinkedList<Joueur> joueurs; //essai avec circular LL
    private CircularLinkedList<Joueur> joueursDansLaTournee;
    private Paquet paquet; //Le paquet du jeu - remplie dans le constructeur
    private Table table;
    private int niveau;
    private int nJoueurs; //Le numéro actuel de joueurs dans le jeu
    private LinkedList<Joueur> joueursGagnants;
    private  LinkedList<String> nomsJoueursOrdinateurs;
    protected int valeurSmallBlind=10; //La valeur du small blind actuel
    protected int valeurBigBlind=20;
    protected int pariActuel;
    protected String nomJoueurHumain;
    private FenetreJeuV2 fenetre;
    private boolean tourneeFinie = false;
    private Node dernierQuiAPari;

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
    public Jeu(int nJoeurs, int niveau, FenetreJeuV2 fenetre){  // méthode utilisée pour l'instant
        this.nJoueurs=nJoeurs;
        this.niveau = niveau;
        this.joueurs = new CircularLinkedList<>();
        this.fenetre=fenetre;
        creerListeNomsJoueursOrdinateurs();
        nomJoueurHumain = "Baltazar Jhones"; // ça viendra du constructeur, mais mis par default pour simplicite
        joueurs.addNode(new Joueur(nomJoueurHumain));
        //Ajout des joueurs ordinateurs dans la liste de joueurs
        for(int i=1;i<nJoueurs;i++){
            int r = (int)((nomsJoueursOrdinateurs.size())*Math.random());
            Ordinateur ord = new Ordinateur(nomsJoueursOrdinateurs.get(r), niveau); // Ajouté pour pouvoir ajouter aussi a joueursCirc sans avoir a tt enlever
            nomsJoueursOrdinateurs.remove(r);
            if(i==2){
                ord.dealer=true;
            }
            if(i==3){
                ord.smallBlind=true;
            }
            if(i==4){
                ord.bigBlind=true;
            }
            if(i==5){
                ord.playing=true;
            }
            joueurs.addNode(ord);
        }
        paquet= new Paquet();
        table = new Table();
        distribuerCartesJoueurs();
        distribuerCartesTable();
        setHands();
        distribuerArgent(3000);
    }

    /*
    Retourne une ll avec les cartes dans la table
     */
    public LinkedList<Carte> getCartesTable(){
        return table.getTable();
    }


    /*
    Méthode enlève le joueur donné en paramètre et met à jour nJoueurs.
    @param Joueur j - Joueur à enlever.
     */
    public void sortirJoueur(Joueur j) {
        joueurs.remove(j);
        nJoueurs--;
    }

    public LinkedList<? extends Joueur> getJoueursGagnants(){
        if(joueursGagnants==null){
            return null;
        }
        else{
            return joueursGagnants;
        }
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

    public void commencerTourDeParis(){
        Node current = joueursDansLaTournee.getJoueurPlaying();
        boolean tourFini = false;

        while (!tourFini){
            if(joueursDansLaTournee.size()==1){
                tourFini=true;
                tourneeFinie = true;
                joueursGagnants.add(joueursDansLaTournee.head.joueur);
                System.out.print("tous fold");
            }
            else if(current.joueur == dernierQuiAPari.joueur){
                tourFini=true;
                System.out.println("Tour complet");
            }
            else {
                System.out.print("tour :"+current.joueur.nom);
                current.joueur.jouer(pariActuel);
                // Le joueur a augmenté le pot
                if (current.joueur.action >= 2) {
                    //Mise à jour du pari actuel si un joueur augmente le pot
                    pariActuel=current.joueur.action;
                    dernierQuiAPari = current;
                    System.out.println(" Raise");
                }
                // Le joueur a couché ses cartes
                else if(current.joueur.action == 0){
                    if(current.joueur.playing){
                        current.prochainNode.joueur.playing=true;
                    }
                    joueursDansLaTournee.remove(current.joueur);
                    System.out.println(" Fold");
                }
                else if(current.joueur.action==1){
                    System.out.println(" Call");
                }
                fenetre.mettreAJourInfosJoueur(current.joueur);
                current=current.prochainNode;
            }
        }

    }

    public void commencerTournee(){
        joueurs.getJoueurBigBlind().joueur.parier(valeurSmallBlind);
        joueurs.getJoueurBigBlind().joueur.parier(valeurBigBlind);
        joueursDansLaTournee = joueurs;
        joueursGagnants=new LinkedList<>();
        dernierQuiAPari = joueurs.getJoueurBigBlind();
        pariActuel=valeurBigBlind; //La valeur à payer pour joueur la tournée
        commencerTourDeParis();
        /*
        Il n'est resté qu'un seul joueur après le tour de paris et lui est le gagnant
         */
        if(tourneeFinie){
            joueursGagnants.add(joueursDansLaTournee.head.joueur);
            fenetre.afficherHandGagnante(joueursGagnants,true);
        }
        /*Plus d'un joueur continue dans le jeu, on montre les trois premières cartes et un
        nouveau tour de paris commence
        */
        else {
            System.out.println("\nJoueurs dans Flop");
            joueursDansLaTournee.display();
            flop();
            commencerTourDeParis();

            if (tourneeFinie) {
                System.out.println("Finie apres flop");
                joueursGagnants.add(joueursDansLaTournee.head.joueur);
                fenetre.afficherHandGagnante(joueursGagnants,true);
            }

            /*Plus d'un joueur continue dans le jeu après le flop, on montre la quatrième carte et un
            nouveau tour de paris commence
             */
            else {
                System.out.println("\nTurn");
                turn();
                commencerTourDeParis();

                if (tourneeFinie) {
                    System.out.println("Finie apres river");
                    joueursGagnants.add(joueursDansLaTournee.head.joueur);
                    fenetre.afficherHandGagnante(joueursGagnants,true);
                }

                /*Plus d'un joueur continue dans le jeu après le flop, on montre la dernière carte et un
                nouveau tour de paris commence
                */
                else {
                    System.out.println("\nRiver");
                    river();
                    commencerTourDeParis();

                    if (tourneeFinie) {
                        System.out.println("Finie apres river");
                        joueursGagnants.add(joueursDansLaTournee.head.joueur);
                        fenetre.afficherHandGagnante(joueursGagnants,true);
                    } else {
                        System.out.println("Hand plus haute");
                        trouverJoueursGagnants(joueursDansLaTournee.getLinkedListJoueurs());
                        fenetre.afficherHandGagnante(trouverJoueursGagnants(joueursDansLaTournee.getLinkedListJoueurs()),false);
                    }
                }
            }
        }
    }


    private void flop(){
        fenetre.flop();
    }

    private void turn(){
        fenetre.turn();
    }

    private void river(){
        fenetre.river();
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

    public CircularLinkedList<Joueur> getJoueursCirc(){
        return joueurs;
    }

    public boolean ajouterKicker (LinkedList<? extends Joueur> joueursEgaux){
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

    public LinkedList<Joueur> trouverJoueursGagnants(LinkedList<Joueur> joueursDansLaTournee){
        LinkedList<Joueur> joueursGagnants = new LinkedList<>();
        joueursDansLaTournee.sort(Collections.reverseOrder());

        /*
        Cas où il y a un seul joueur gagnant
         */
        if (joueursDansLaTournee.getFirst().getHand().getValeurHand() > joueursDansLaTournee.get(1).getHand().getValeurHand()){
            joueursGagnants.add(joueursDansLaTournee.getFirst());
        }

        else {
            int v1 = joueursDansLaTournee.getFirst().getHand().getValeurHand();
            joueursDansLaTournee.removeIf(joueur -> joueur.getHand().getValeurHand() < v1);
            /*
            Après cette boucle for, tous les joueurs qui ont une valeur de hand inférieure à celle de la hand plus haute
            ont été enlevés de la liste tousJoueurs. On ajoute la valeur du kicker (si applicable) dans la nouvelle liste
            et on vérifie si l'égalité a pu être enlevée.
             */

            if (ajouterKicker(joueursDansLaTournee)) {
                /*
                Dans ce cas l'égalitée a pu être enlevée et on ajoute le seul joueur gagnant dans la liste joueursGagnants
                 */
                joueursDansLaTournee.sort(Collections.reverseOrder());
                joueursGagnants.add(joueursDansLaTournee.getFirst());
            } else {
                /*
                Ici, l'égalité reste même après l'ajout de la valeur du kicker. On enlève de la liste tous les joueurs qui
                ont une hand inférieure à la plus haute et on ajoute tous ceux qui ont une valeur de hand égale a la plus haute
                é joueursGagnants. La boucle commence dans l'indice 2 parce qu'on sait déjà que les joueurs d'indice 0 et 1
                ont une hand de même valeur.
                 */
                joueursDansLaTournee.sort(Collections.reverseOrder());
                int v2 = joueursDansLaTournee.getFirst().getHand().getValeurHand();
                joueursDansLaTournee.removeIf(joueur -> joueur.getHand().getValeurHand() < v2);
                joueursGagnants.addAll(joueursDansLaTournee);
                joueursGagnants.getFirst().getHand().ajouterDescription(". Partage du pot");
            }

        }

        return joueursGagnants;
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
}