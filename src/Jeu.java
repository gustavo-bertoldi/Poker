import org.w3c.dom.css.CSSImportRule;

import java.util.Collections;
import java.util.LinkedList;

public class Jeu extends Thread{

    private LinkedListCirculaire joueurs;
    private LinkedListCirculaire joueursDansLaTournee = new LinkedListCirculaire();
    private LinkedList<Joueur> gagnantsDeLaTournee;
    private LinkedList<String> nomsJoueursOrdinateurs;
    private String nomJoueurHumain;

    private Paquet paquet;
    private Table table;

    private int nJoueurs;

    protected int valeurSmallBlind;
    protected int valeurBigBlind;
    protected int pariActuel;
    protected int potActuel;

    private boolean dealerDejaChange;
    private boolean tourneeFinie;

    private FenetreJeuV2 fenetre;

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
    public Jeu(int nJoueurs) {
        this.nJoueurs=nJoueurs;
        creerListeNomsJoueursOrdinateurs();
        nomJoueurHumain = "Baltazar";

        //Création des Joueurs et définition des attributs dealer, small blind, big blind et playing pour la première tournée
        joueurs = new LinkedListCirculaire();
        joueurs.add(new Joueur(nomJoueurHumain,true));
        for (int i=0; i<nJoueurs;i++){
            //Création des joueurs ordinateurs qui prennent un nom aléatoire de la liste nomsJoueursOrdinateurs
            int a = (int)(Math.random()*nomsJoueursOrdinateurs.size());
            joueurs.add(new Ordinateur(nomsJoueursOrdinateurs.get(a),0));
            nomsJoueursOrdinateurs.remove(a);
        }
        joueurs.getFirst().joueur.dealer=true;
        joueurs.getFirst().prochainNode.joueur.smallBlind=true;
        joueurs.getFirst().prochainNode.prochainNode.joueur.bigBlind=true;
        joueurs.getFirst().prochainNode.prochainNode.prochainNode.joueur.playing=true;

        paquet = new Paquet();
        table = new Table();

        //Distribution des cartes de chaque joueur ainsi que celles de la table
        distribuerCartesJoueurs();
        distribuerCartesTable();
        joueurs.getJoueurs().forEach(j -> j.setArgent(10000)); //On distribue une quantité d'argent initiale à chaque joueur

        //Initialization des attributs pour la première tournée
        valeurSmallBlind=10;
        valeurBigBlind=20;
        potActuel=0;
        pariActuel=20;

        //Initialisation de l'interface graphique
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

    public LinkedList<Joueur> getGagnantsDeLaTournee(){
        assert gagnantsDeLaTournee!=null : "Jeu : gagnantsDeLaTournee null";
        return gagnantsDeLaTournee;
    }

    public Node getFirstNode(){
        return joueurs.getFirst();
    }

    /*
    Distribution des 2 cartes sur la main de chaque joueur, de manière aléatoire
     */
    private void distribuerCartesJoueurs() {
        //On prend deux cartes aléatoires du paquet et les distribue à chaque joueur
        joueurs.getJoueurs().forEach(Joueur -> {
            LinkedList<Carte> cartesJoueur = new LinkedList<>();
            int r = (int)((paquet.size())*Math.random());
            Carte c=paquet.get(r);
            cartesJoueur.add(c);
            paquet.remove(c);
            r = (int)((paquet.size())*Math.random());
            c=paquet.get(r);
            cartesJoueur.add(c);
            paquet.remove(c);
            Joueur.setCartesSurMain(cartesJoueur);
        });
    }

    /*
    Distribution des 5 cartes de la table et définition de la hand de 7 cartes de chaque joueur
     */
    private void distribuerCartesTable(){
        //On distribue 5 cartes de manière aléatoire à la table
        LinkedList<Carte> cartesTable = new LinkedList<>();
        for(int i = 0 ; i < 5 ; i++){
            int m = (int) ((paquet.size()) * Math.random());
            Carte c = paquet.get(m);
            cartesTable.add(c);
            paquet.remove(c);
        }
        table.setCartesTable(cartesTable);
        //On ajoute les cartes de la table dans la hand de chaque joueur pour le calcul du valeur de la hand
        joueurs.getJoueurs().forEach(Joueur -> Joueur.setHand(cartesTable));
    }

    public void commencerTourDeParis() throws Exception {
        if(joueursDansLaTournee==null){
            throw new Exception("LinkeListCirculaire joueursDansLaTournee non initialisée");
        }
        if(gagnantsDeLaTournee==null){
            throw new Exception("LinkedList<Joueur> gagnantsDeLaTournee non initialisée");
        }
        Node current = joueursDansLaTournee.getNodeBigBlind().prochainNode;
        Node dernierAParier = current;
        boolean tourFini = false;
        boolean controle = false; //boolean controle utilisé pour assurer que le premier joueur a déjà joué par le première fois

        while (!tourFini) {
            if (joueursDansLaTournee.size() == 1) {
                tourFini = true;
                tourneeFinie = true;
                System.out.print("tous fold");
            } else if (current == dernierAParier && controle) {
                tourFini = true;
                System.out.println("Tour complet");
            } else {
                System.out.print("tour :" + current.joueur.nom);
                current.joueur.jouer(pariActuel); //Le joueur joue
                // Le joueur a augmenté le pot
                if (current.joueur.action >= 2) {
                    //Mise à jour du pari actuel si un joueur augmente le pot
                    pariActuel = current.joueur.action;
                    dernierAParier = current;
                    //fenetre.mettreAJourInfosJoueur(current.joueur);
                    System.out.println(" Raise");
                    current.joueur.playing=false;
                    current.prochainNode.joueur.playing=true;
                    current=current.prochainNode;
                }
                // Le joueur a couché ses cartes
                else if (current.joueur.action == 0) {
                    if(!current.equals(dernierAParier)){
                        joueursDansLaTournee.remove(current);
                        dernierAParier=current; //Lors de la prochaine exécution de la boucle, le tour finit
                    }
                    else{
                        Node aEnlever = current;
                        current.joueur.playing=false;
                        current.prochainNode.joueur.playing=true;
                        current=current.prochainNode;
                        joueursDansLaTournee.remove(aEnlever);
                    }
                    //fenetre.mettreAJourInfosJoueur(current.joueur);
                    System.out.println(" Fold");
                } else if (current.joueur.action == 1) {
                    System.out.println(" Call");
                    current.joueur.playing=false;
                    current.prochainNode.joueur.playing=true;
                    current=current.prochainNode;
                    //fenetre.mettreAJourInfosJoueur(current.joueur);
                }
                else{
                    System.err.println("Action non definie");
                }
                controle = true;
            }
        }
    }

    public void commencerTournee() throws Exception {
        joueurs.getNodeSmallBlind().joueur.parier(valeurSmallBlind);
        joueurs.getNodeBigBlind().joueur.parier(valeurBigBlind);
        joueursDansLaTournee = new LinkedListCirculaire(joueurs.getJoueurs());
        gagnantsDeLaTournee = new LinkedList<>();
        pariActuel = valeurBigBlind; //La valeur à payer pour joueur la tournée
        commencerTourDeParis();
        /*
        Il n'est resté qu'un seul joueur après le tour de paris et lui est le gagnant
         */
        if(tourneeFinie){
            gagnantsDeLaTournee.add(joueursDansLaTournee.tete.joueur);
            fenetre.afficherHandGagnante(gagnantsDeLaTournee,true);
        }
        /*Plus d'un joueur continue dans le jeu, on montre les trois premières cartes et un
        nouveau tour de paris commence
        */
        else {
            System.out.println("\nJoueurs dans Flop");
            //fenetre.flop();
            commencerTourDeParis();

            if (tourneeFinie) {
                System.out.println("Finie apres flop");
                gagnantsDeLaTournee.add(joueursDansLaTournee.tete.joueur);
                fenetre.afficherHandGagnante(gagnantsDeLaTournee,true);
            }

            /*Plus d'un joueur continue dans le jeu après le flop, on montre la quatrième carte et un
            nouveau tour de paris commence
             */
            else {
                System.out.println("\nTurn");
                //fenetre.turn();
                commencerTourDeParis();

                if (tourneeFinie) {
                    System.out.println("Finie apres river");
                    gagnantsDeLaTournee.add(joueursDansLaTournee.tete.joueur);
                    fenetre.afficherHandGagnante(gagnantsDeLaTournee,true);
                }

                /*Plus d'un joueur continue dans le jeu après le flop, on montre la dernière carte et un
                nouveau tour de paris commence
                */
                else {
                    System.out.println("\nRiver");
                    //fenetre.river();
                    commencerTourDeParis();

                    if (tourneeFinie) {
                        System.out.println("Finie apres river");
                        gagnantsDeLaTournee.add(joueursDansLaTournee.tete.joueur);
                        fenetre.afficherHandGagnante(gagnantsDeLaTournee,true);
                    } else {
                        System.out.println("Hand plus haute");
                        trouverJoueursGagnants(joueursDansLaTournee.getJoueurs());
                        fenetre.afficherHandGagnante(trouverJoueursGagnants(joueursDansLaTournee.getJoueurs()),false);
                    }
                }
            }
        }
    }


    public boolean ajouterKicker (LinkedList<? extends Joueur> joueursEgaux){
        for (Joueur j : joueursEgaux) {
            /*
            Creation d'une linked list kicker qui contient toutes les 7 cartes du joueur, sauf celles utilisees pour
            former la hand actuelle (pair, brelan, etc)
            La liste kicker est triée en ordre décroissante
             */
            LinkedList<Carte> kickers = j.getHand().getToutesCartes();
            LinkedList<Carte> kickersSurMain = j.getCartesSurMain();
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
                        j.getHand().ajouterKicker(kickersSurMain.getFirst());
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
                        j.getHand().ajouterKicker(kickersSurMain.getFirst());
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
                        j.getHand().ajouterKicker(kickersSurMain.getFirst());
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
                        j.getHand().ajouterKicker(kickersSurMain.getFirst());
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
                        j.getHand().ajouterKicker(kickersSurMain.getFirst());
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

    protected Joueur getJoueurHumain() throws Exception {
        return joueurs.getJoueurHumain();
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