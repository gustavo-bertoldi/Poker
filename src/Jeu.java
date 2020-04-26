import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Jeu extends Thread {

    protected LinkedListCirculaire joueurs; //essai avec circular LL
    protected LinkedListCirculaire joueursDansLaTournee;
    private Paquet paquet; //Le paquet du jeu - remplie dans le constructeur
    private Table table;
    private LinkedList<ImageIcon> iconCartesTable;

    protected int nJoueurs; //Le numéro actuel de joueurs dans le jeu
    private LinkedList<String> nomsJoueursOrdinateurs;
    protected String nomJoueurHumain;
    protected FenetreJeuV3 fenetre;

    protected boolean flop;
    protected boolean turn;
    protected boolean river;
    private LinkedList<Joueur> joueursGagnants;
    protected int valeurSmallBlind = 10; //La valeur du small blind actuel
    protected int valeurBigBlind = 20;
    protected int pariActuel;
    protected int potActuel;
    protected int potAvantAllIn;
    protected Joueur dernierAParier;
    protected Node joueurActuel;
    private boolean smallBlindDansJeu;


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
    public Jeu(int nJoueurs) throws Exception {
        this.nJoueurs = nJoueurs;
        creerListeNomsJoueursOrdinateurs();
        nomJoueurHumain = "Baltazar";

        //Création des Joueurs et définition des attributs dealer, small blind, big blind et playing pour la première tournée
        joueurs = new LinkedListCirculaire();
        joueurs.add(new Joueur(nomJoueurHumain, true));
        for (int i = 0; i < nJoueurs - 1; i++) {
            //Création des joueurs ordinateurs qui prennent un nom aléatoire de la liste nomsJoueursOrdinateurs
            int a = (int) (Math.random() * nomsJoueursOrdinateurs.size());
            joueurs.add(new Ordinateur(nomsJoueursOrdinateurs.get(a), 0));
            nomsJoueursOrdinateurs.remove(a);
        }
        joueurs.getFirst().joueur.dealer = true;
        joueurs.getFirst().prochainNode.joueur.smallBlind = true;
        joueurs.getFirst().prochainNode.prochainNode.joueur.bigBlind = true;
        joueurs.getFirst().prochainNode.prochainNode.prochainNode.joueur.playing = true;

        joueurs.getJoueurs().forEach(j -> j.setArgent(3000)); //On distribue une quantité d'argent initiale à chaque joueur

        joueursDansLaTournee = new LinkedListCirculaire(joueurs.getJoueurs());

        //Initialization des attributs pour la première tournée
        valeurSmallBlind = 10;
        valeurBigBlind = 20;
        potActuel = 0;
        potAvantAllIn = 0;
        pariActuel = 20;
        flop = false;
        turn = false;
        river = false;
        dernierAParier = joueursDansLaTournee.getNodeBigBlind().joueur;
        joueurActuel = joueursDansLaTournee.getNodePlaying();

        paquet = new Paquet();
        table = new Table();
        distribuerCartes();

        //Initialisation de l'interface graphique
        fenetre = new FenetreJeuV3(this);

        joueursDansLaTournee.getNodeBigBlind().joueur.payerBigBlind(valeurBigBlind, this);
        joueursDansLaTournee.getNodeSmallBlind().joueur.payerSmallBlind(valeurSmallBlind, this);
        if (!joueurActuel.joueur.humain) {
            long waitTime = System.currentTimeMillis() + 3000;
            while (System.currentTimeMillis() != waitTime) {
            }
            ((Ordinateur) joueurActuel.joueur).jouer(pariActuel, this);
        }
    }

    public LinkedList<? extends Joueur> getJoueursGagnants() {
        if (joueursGagnants == null) {
            return null;
        } else {
            return joueursGagnants;
        }
    }

    public Joueur getDernierDansLaTournee() {
        return joueursDansLaTournee.getFirst().joueur;
    }

    public LinkedList<ImageIcon> getIconCartesTable() {
        return iconCartesTable;
    }


    /*
  Distribution des 2 cartes sur la main de chaque joueur, de manière aléatoire
   */
    private void distribuerCartes() {
        //On distribue 5 cartes de la table de manière aléatoire à la table
        LinkedList<Carte> cartesTable = new LinkedList<>();
        iconCartesTable = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            int m = (int) ((paquet.size()) * Math.random());
            Carte c = paquet.get(m);
            cartesTable.add(c);
            iconCartesTable.add(c.icon);
            paquet.remove(c);
        }
        table.setCartesTable(cartesTable);

        //On prend deux cartes aléatoires du paquet et les distribue à chaque joueur
        joueurs.getJoueurs().forEach(Joueur -> {
            LinkedList<Carte> cartesJoueur = new LinkedList<>();
            int r = (int) ((paquet.size()) * Math.random());
            Carte c = paquet.get(r);
            cartesJoueur.add(c);
            paquet.remove(c);
            r = (int) ((paquet.size()) * Math.random());
            c = paquet.get(r);
            cartesJoueur.add(c);
            paquet.remove(c);
            Joueur.setHand(cartesJoueur,cartesTable);
        });
    }

    /*
    Cette méthode est utilisée quand un joueur a foldé, c'est-à-dire, il n'a pas payé le montant pour joueur et a
    couché ses cartes.
    On enlève ce joueur de la list de joueurs dans la tournée jusqu'à la prochaine tournée.
     */
    public void sortirDeLaTournee(Joueur j){
        /*
        Si le joueur est le joueur playing, on define d'abord le joueur après lui comme playing et après on l'enlève
        de la liste. Cela est fait pour assurer la bonne continuité du jeu.
         */
        if (j.playing) {
            j.dansTournee=false;
            joueursDansLaTournee.getNode(j).prochainNode.joueur.playing = true;
        }
        joueursDansLaTournee.remove(j);
    }

    public void prochaineTournee() throws Exception {
        fenetre.dispatchEvent(new WindowEvent(fenetre, WindowEvent.WINDOW_CLOSING));
        smallBlindDansJeu = true;
        joueurs.getJoueurs().forEach(joueur -> {
            joueur.coup="";
            if(joueur.getArgent()<=10 && joueur.dealer){
                joueurs.getNode(joueur).prochainNode.joueur.dealer=true;
                joueur.dansJeu=false;}
            else if(joueur.getArgent()<=10){
                joueur.dansJeu=false;
            }
        });
        LinkedList<Joueur> joueursDansJeu = joueurs.getJoueurs();
        if (joueursDansJeu.size()==1){
            jeuFini();
        }
        joueursDansJeu.removeIf(joueur -> !joueur.dansJeu);
        joueursDansLaTournee = new LinkedListCirculaire(joueursDansJeu);
        Node ancienDealer = joueursDansLaTournee.getNodeDealer();
        joueursDansLaTournee.getJoueurs().forEach(Joueur::resetRolesJeu);
        if (joueursDansLaTournee.size() >= 4) {
            ancienDealer.prochainNode.joueur.setRoleJeu(true, false, false, false);
            ancienDealer.prochainNode.prochainNode.joueur.setRoleJeu(false, true, false, false);
            ancienDealer.prochainNode.prochainNode.prochainNode.joueur.setRoleJeu(false, false, true, false);
            ancienDealer.prochainNode.prochainNode.prochainNode.prochainNode.joueur.setRoleJeu(false, false, false, true);
        } else if (joueursDansLaTournee.size() == 3) {
            ancienDealer.prochainNode.joueur.setRoleJeu(true, false, false, true);
            ancienDealer.prochainNode.prochainNode.joueur.setRoleJeu(false, true, false, false);
            ancienDealer.prochainNode.prochainNode.prochainNode.joueur.setRoleJeu(false, false, true, false);
        } else if (joueursDansLaTournee.size() == 2) {
            smallBlindDansJeu=false;
            ancienDealer.prochainNode.joueur.setRoleJeu(true, false, false, true);
            ancienDealer.joueur.setRoleJeu(false, false, true, false);
        }
        flop = false;
        turn = false;
        river = false;
        potActuel = 0;
        potAvantAllIn = 0;
        pariActuel=valeurBigBlind;
        dernierAParier = joueursDansLaTournee.getNodeBigBlind().joueur;
        joueurActuel = joueursDansLaTournee.getNodePlaying();
        paquet = new Paquet();
        table = new Table();
        distribuerCartes();

        Thread reconstruireFenetre = new Thread(() -> fenetre = new FenetreJeuV3(this));
        Thread continuerTournee = new Thread(() -> {
            try {
                joueursDansLaTournee.getNodeBigBlind().joueur.payerBigBlind(valeurBigBlind, this);
                if(smallBlindDansJeu){
                    joueursDansLaTournee.getNodeSmallBlind().joueur.payerSmallBlind(valeurSmallBlind, this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!joueurActuel.joueur.humain) {
                try {
                    ((Ordinateur) joueurActuel.joueur).jouer(pariActuel, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                fenetre.afficherBoutons(true);
            }});
        reconstruireFenetre.start();
        reconstruireFenetre.join();
        continuerTournee.start();
    }

    /*
    Méthode appelée lorsqu'un joueur joue son tour, on appelle le prochain et on le fait jouer
     */
    public void prochainJoueur() throws Exception {
        joueurActuel=joueurActuel.prochainNode;
        if(joueurActuel.joueur.humain){
            fenetre.afficherBoutons(true);
        }
        if (joueursDansLaTournee.size() == 1) {
            tourneeFinie();
        } else if (!joueurActuel.joueur.humain) {
            long waitTime = System.currentTimeMillis() + 1000;
            while (System.currentTimeMillis() != waitTime) {
            }
            ((Ordinateur) joueurActuel.joueur).jouer(pariActuel, this);
        }
    }


    /*
    Méthode appelée quand le tour de paris est fini.
    On vérifie d'abord si tous les joueurs on mis All In: Si c'est le cas le tournée finit et on cherche le gagnant
    Sinon, on montre les cartes de la table et on exécute les actions nécessaires pour un nouveau tour de paris
     */
    public void tourDeParisFini() throws Exception {
        AtomicBoolean tousAllIn= new AtomicBoolean(false);
        joueursDansLaTournee.getJoueurs().forEach(joueur -> {
            joueur.derniereValeurPariee=0;
            if(joueur.allIn){
                tousAllIn.set(true);
            }
        });
        //Dans ce cas tous les joueurs ont mis All In, on montre toutes les cartes et on compare les hands
        if(tousAllIn.get()){
            fenetre.river();
            joueursDansLaTournee.getJoueurs().forEach(joueur -> joueur.allIn=false);
            tourneeFinie();
        }
        //Sinon on poursuit le jeu
        else {
            if (!flop) {
                flop = true;
                pariActuel = 0;
                potAvantAllIn=0;
                fenetre.flop();
                long waitTime = System.currentTimeMillis() + 3000;
                while (System.currentTimeMillis() != waitTime) {
                }
                fenetre.effacerCoupsJoueur();
                joueurActuel = joueursDansLaTournee.getNodeAnterieur(joueursDansLaTournee.getNodePlaying()); //Sera le prochain lors de l'execution de prochainJoueur()
                dernierAParier = joueurActuel.joueur;
                prochainJoueur();
            } else if (!turn) {
                turn = true;
                pariActuel = 0;
                potAvantAllIn=0;
                fenetre.turn();
                long waitTime = System.currentTimeMillis() + 3000;
                while (System.currentTimeMillis() != waitTime) {
                }
                fenetre.effacerCoupsJoueur();
                joueurActuel = joueursDansLaTournee.getNodeAnterieur(joueursDansLaTournee.getNodePlaying());
                dernierAParier = joueurActuel.joueur;
                prochainJoueur();
            } else if (!river) {
                river = true;
                pariActuel = 0;
                potAvantAllIn=0;
                fenetre.river();
                long waitTime = System.currentTimeMillis() + 3000;
                while (System.currentTimeMillis() != waitTime) {
                }
                fenetre.effacerCoupsJoueur();
                joueurActuel = joueursDansLaTournee.getNodeAnterieur(joueursDansLaTournee.getNodePlaying());
                dernierAParier = joueurActuel.joueur;
                prochainJoueur();
            } else {
                tourneeFinie();
            }
        }
    }

    /*
    Cette méthode est appelée quand la tournée est finie. La tournée peut finir soit parce qu'il ne reste qu'un joueur
    dans la tournée, soit parce que tous les tours de paris sont finis et on cherche le(s) joueur(s) gagnant(s).
    L'argent destiné à chaque joueur est bien distribué et on passe à la prochaine tournée.
     */
    public void tourneeFinie(){
        //Cas où il ne reste qu'un joueur dans la tournée.
        if (joueursDansLaTournee.size()==1){
            fenetre.afficherHandGagnante(true);
            joueursDansLaTournee.getFirst().joueur.ajouterArgent(potActuel);
        }
        //Cas où tous les tours de paris sont dinis, on cherche le(s) gagnant(s).
        else {
            joueursGagnants = new LinkedList<>(trouverJoueursGagnants(joueursDansLaTournee.getJoueurs()));
            potActuel = potActuel/joueursGagnants.size();
            fenetre.afficherHandGagnante(false);
            joueursDansLaTournee.getJoueurs().forEach(joueur -> fenetre.montrerCartesJoueur(joueur));
            //Cas où le joeurs gagnant n'a pas payé la valeur complète du pari et a mis AllIn
            joueursGagnants.forEach(joueur -> {
                joueur.ajouterArgent(potActuel);
                fenetre.mettreAJourInfosJoueur(joueur);
            });
        }
        fenetre.afficherBoutonProchaineTournee();
    }



    /*
    Méthode pour finir le jeu, quand il ne reste qu'un joueur dans le jeu, ou bien quand le joueurs humain décide de
    sortir du joueurs. La méthode doit enregistrer le score du joueur, bien comme le temps de jeu et la date.
     */
    public void jeuFini(){
        fenetre.dispatchEvent(new WindowEvent(fenetre, WindowEvent.WINDOW_CLOSING));
    }

    /*
    Retourne le joueur humain
     */
    public Joueur getJoueurHumain() {
        return joueurs.getJoueurHumain(); }

    /*
    Retourne la LikedListCirculaire de joueurs
     */
    public LinkedListCirculaire getJoueurs(){
        return joueurs;
   }

   /*
   Si deux joueurs ont la même valeur de hand, on essaie d'ajouter une carte kicker et départager le match,
   retourne vrai si le match a été départagé, sinon retourne faux.
   @param LinkedList<Joueur> - Liste avec les joueurs égaux à départager
    */
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

    /*
    En utilisant le critère de la plus grande valeur de hand, retourne une liste avec un joueur s'il n'y a q'un joueur
    avec la plus grande valeurs de hand, sinon retourne une liste avec tous les joueurs qui ont la même valeur de hand
    étant la plus haute entre les joueurs dans la tournée.
    @param - On fourni une liste avec les joueurs qui sont dans la tournée pour trouver le(s) gagnant(s) parmi eux.
     */
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

    /*
    La méthode ajoute les noms possibles des joueurs ordinateurs dans une liste. Le nom des joueurs sera tiré au sort
    à la création du jeu.
     */
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
    public static void main(String[] args) throws Exception {
        Jeu j = new Jeu(6);

    }
}