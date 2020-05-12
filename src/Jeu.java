import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Jeu extends Thread {
    /*
    Attribut de base pour la création du jeu
     */
    protected LinkedListCirculaire joueurs; //Liste circulaire avec tous les joueurs.
    protected LinkedListCirculaire joueursDansLaTournee; //Liste circulaire avec les joueurs qui participent de la tournée active.
    private Paquet paquet; //Paquet de cartes de jeu.
    private LinkedList<Carte> cartesTable; //Liste des cartes qui sont sur la table.
    private LinkedList<ImageIcon> iconCartesTable; //Liste des icons des cartes qui sont sur la table, utilisée par la GUI.
    private LinkedList<String> nomsJoueursOrdinateurs; //Liste avec les possibles noms à être attribués aux joueurs ordinateurs.
    protected FenetreJeuV3 fenetre; //La fenêtre qui contient les éléments principaux de la GUI.
    /*
    Attributs pour assures le déroulement du jeu
     */
    protected boolean flop; //True si les cartes du flop ont déjà été affichées (3 prmières de la table)
    protected boolean turn; //True si les cartes du turn ont déjà été affichées (4 prémières de la table)
    protected boolean river; //True si les cartes du river ont été déjà affichées (toutes les cartes sur table)
    protected int moment; // PreFlop = 0, PreTurn = 1, PreRiver = 2, PostRiver = 3
    protected int valeurSmallBlind; //La valeur du small blind actuel
    protected int valeurBigBlind; //La valeur du big blind (2*valeurSmallBlind par définition)
    protected int pariActuel; //La valeur du pari actuel
    protected int potActuel; //Le pot actuel
    /*
    Le boolean potsSecondaires dans jeu indique qu'au moins un pot secondaire a été créé.
    Les pots secondaires sont crées lorsque un joueur n'a pas d'argent suffisant pour payer le pari,
    toute fois il peut parier tout ce qu'il a et continuer dans le jeu, néanmoins s'il gagne, il n'a pas droit au pot
    complet vu qu'il n'a pas parié le montant nécessaire, il a donc un pot secondaire calculé selon la valeur qu'il a pariée.
    CALCUL :
    Ex : Joueur A - 500 fiches
         Joueur B - 100 fiches
         Joueur C - 200 fiches
         Le joueur A parie 300 fiches, si les joueur B et C souhaitent continuer dans le jeu ils soivent parier tout leur argent
         On a alors le pot suivant : 400 (Joueur A) + 100 (Joueur B) + 200 (Joueur C)
         Pot joueur A : 700 (Il peut prendre toutes les fiches) + pots antérieurs
         Pot joueur B : 300 (Il peut prendre 100 fiches de chaque joueur) + pots antérieurs
         Pot Joueur C : 600 (Il peut prendre 200 fiches de chaque joueur) + pots antérieurs
         Donc on multiplie le nombre de joueurs qui ont payé le pari par l'argent du joueur et on obtient son pot secondaire.
     */
    protected boolean potsSecondairesDansJeu; //True s'il y a des pots secondaires dans le jeu
    protected int nJueursQuiOntPayeLeTour; //Utilisé dans le calcul des pots secondaires
    private int potAvantFlop; //Utilisé dans le calcul des pots secondaires
    private int potAvantTurn; //Utilisé dans le calcul des pots secondaires
    private int potAvantRiver; //Utilisé dans le calcul des pots secondaires
    protected Joueur dernierAParier; //Défini au début d'une tournée, c'est le dernier joueur qui va joueur, après lui la tournée est finie
    protected Node joueurActuel; //Le joueur qui joue actuellement

    /*
    Attributs utilisés en fin de tournées pour bien distribuer l'argent au(x) gagnant(s).
     */
    private LinkedList<Joueur> joueursGagnants; //Liste avec le(s) joueur(s) gagnant(s). Obtenue par la plus grande valeur de hand.
    private LinkedHashMap<Joueur, Integer> potsSecondaires; //Liste avec les pots secondaires associés à son respectif joueur.


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

    public Jeu(String nomJoueurHumain, int niveau) throws Exception {

        creerListeNomsJoueursOrdinateurs();
        //Création des Joueurs et définition des attributs dealer, small blind, big blind et playing pour la première tournée
        joueurs = new LinkedListCirculaire();
        joueurs.add(new Joueur(nomJoueurHumain, true, 2));
        for (int i = 0; i < 5; i++) {
            //Création des joueurs ordinateurs qui prennent un nom aléatoire de la liste nomsJoueursOrdinateurs
            int a = (int) (Math.random() * nomsJoueursOrdinateurs.size());
            if(niveau == 2){
                joueurs.add(new Joueur(nomsJoueursOrdinateurs.get(a), false, 2));
            }
            else if (niveau == 1){
                joueurs.add(new Joueur(nomsJoueursOrdinateurs.get(a), false, 1));
            }
            else {
                joueurs.add(new Joueur(nomsJoueursOrdinateurs.get(a), false, 0));
            }
            nomsJoueursOrdinateurs.remove(a);
        }


        paquet = new Paquet();
        cartesTable = new LinkedList<>();
        distribuerCartes();


        joueurs.getFirst().joueur.playing = true;
        joueurs.getFirst().prochainNode.prochainNode.prochainNode.joueur.dealer = true;
        joueurs.getFirst().prochainNode.prochainNode.prochainNode.prochainNode.joueur.smallBlind = true;
        joueurs.getFirst().prochainNode.prochainNode.prochainNode.prochainNode.prochainNode.joueur.bigBlind = true;

        joueurs.getJoueurs().forEach(j -> j.setArgent(3000)); //On distribue une quantité d'argent initiale à chaque joueur
        joueursDansLaTournee = new LinkedListCirculaire(joueurs.getJoueurs());

        //Initialization des attributs pour la première tournée
        valeurSmallBlind = 10;
        valeurBigBlind = 20;
        potActuel = 0;
        pariActuel = 20;
        potAvantFlop=0;
        potAvantTurn=0;
        potAvantRiver=0;
        flop = false;
        turn = false;
        river = false;
        moment = 0;
        potsSecondairesDansJeu=false;
        dernierAParier = joueursDansLaTournee.getNodeBigBlind().joueur;
        joueurActuel = joueursDansLaTournee.getNodePlaying();
        potsSecondaires = new LinkedHashMap<>();
        nJueursQuiOntPayeLeTour=0;




        //Initialisation de l'interface graphique
        fenetre = new FenetreJeuV3(this);

        joueursDansLaTournee.getNodeBigBlind().joueur.payerBigBlind(this);
        joueursDansLaTournee.getNodeSmallBlind().joueur.payerSmallBlind(this);
        fenetre.afficherBoutons(true);
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
    private void distribuerCartes() throws InterruptedException {

        //On distribue 5 cartes de la table de manière aléatoire à la table
        iconCartesTable = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            int m = (int) ((paquet.size()) * Math.random());
            Carte c = paquet.get(m);
            cartesTable.add(c);
            iconCartesTable.add(c.icon);
            paquet.remove(c);
        }


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
            Hand h = new Hand();
            h.definirSurMainEtSurTable(cartesJoueur, cartesTable);
            Joueur.setHand(h);
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
            joueursDansLaTournee.getNode(j).prochainNode.joueur.playing = true;
        }
        joueursDansLaTournee.remove(j);
    }

    public void prochaineTournee() throws Exception {
        fenetre.dispatchEvent(new WindowEvent(fenetre, WindowEvent.WINDOW_CLOSING));
        joueurs.getJoueurs().forEach(joueur -> {
            joueur.coup="";
            if(joueur.getArgent()<=10 && joueur.dealer){
                joueurs.getNode(joueur).prochainNode.joueur.dealer=true;
                joueur.dansJeu=false;}
            else if(joueur.getArgent()<=10){
                joueur.dansJeu=false;
            }
            joueur.resetIntelligence();
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
            ancienDealer.prochainNode.joueur.setRoleJeu(true, false, true, true);
            ancienDealer.joueur.setRoleJeu(false, true, false, false);
        }
        flop = false;
        turn = false;
        river = false;
        potActuel = 0;
        potAvantFlop=0;
        potAvantTurn=0;
        potAvantRiver=0;
        pariActuel=valeurBigBlind;
        dernierAParier = joueursDansLaTournee.getNodeBigBlind().joueur;
        joueurActuel = joueursDansLaTournee.getNodePlaying();
        potsSecondaires= new LinkedHashMap<>();
        potsSecondairesDansJeu=false;
        nJueursQuiOntPayeLeTour=0;
        paquet = new Paquet();
        cartesTable=new LinkedList<>();
        distribuerCartes();

        Thread reconstruireFenetre = new Thread(() -> fenetre = new FenetreJeuV3(this));
        Thread continuerTournee = new Thread(() -> {
            try {
                joueursDansLaTournee.getNodeBigBlind().joueur.payerBigBlind(this);
                joueursDansLaTournee.getNodeSmallBlind().joueur.payerSmallBlind(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!joueurActuel.joueur.humain) {
                try {
                    (joueurActuel.joueur).jouer(pariActuel, this);
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

        if (joueursDansLaTournee.size() == 1) {
            tourneeFinie();
        }
        else if(joueurActuel.joueur.humain){
            if(joueurActuel.joueur.allIn){
                joueurActuel.joueur.setAction(1,pariActuel,this);
            }
            else{
                fenetre.afficherBoutons(true);
            }
        } else  {
            joueurActuel.joueur.intelligence.setBets(pariActuel,potActuel);
            long waitTime = System.currentTimeMillis() + 1000;
            while (System.currentTimeMillis() != waitTime) {
            }
            (joueurActuel.joueur).jouer(pariActuel, this);
        }
    }


    /*
    Méthode appelée quand le tour de paris est fini.
    On vérifie d'abord si tous les joueurs on mis All In: Si c'est le cas le tournée finit et on cherche le gagnant
    Sinon, on montre les cartes de la table et on exécute les actions nécessaires pour un nouveau tour de paris
     */
    public void tourDeParisFini() throws Exception {
        AtomicBoolean tousAllIn= new AtomicBoolean(true);
        joueursDansLaTournee.getJoueurs().forEach(joueur -> {
            joueur.intelligence.setBets(valeurBigBlind, potActuel);
            joueur.derniereValeurPariee=0;
            if(!joueur.allIn){
                tousAllIn.set(false);
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
            nJueursQuiOntPayeLeTour = 0;
            if (!flop) {
                flop = true;
                moment = 1;
                pariActuel = 0;
                potAvantFlop=potActuel;
                fenetre.flop();
                potsSecondaires.forEach((joueur, pot) -> joueur.potsDejaCompletes=true);
                potsSecondaires.forEach((joueur, pot) -> joueur.potsDejaCompletes=true);
                long waitTime = System.currentTimeMillis() + 3000;
                while (System.currentTimeMillis() != waitTime) {
                }
                fenetre.effacerCoupsJoueur();
                joueurActuel = joueursDansLaTournee.getNodeAnterieur(joueursDansLaTournee.getNodePlaying()); //Sera le prochain lors de l'execution de prochainJoueur()
                dernierAParier = joueurActuel.joueur;
                prochainJoueur();
            } else if (!turn) {
                turn = true;
                moment = 2;
                pariActuel = 0;
                potAvantTurn=potActuel;
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
                moment = 3;
                pariActuel = 0;
                potAvantRiver=potActuel;
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
        StringBuilder descriptionPot = new StringBuilder();
        moment = 0;
        if (joueursDansLaTournee.size()==1){
            fenetre.afficherHandGagnante(true, descriptionPot.toString());
            joueursDansLaTournee.getFirst().joueur.ajouterArgent(potActuel);
        }
        //Cas où tous les tours de paris sont dinis, on cherche le(s) gagnant(s).
        else {
            joueursDansLaTournee.getJoueurs().forEach(joueur -> fenetre.montrerCartesJoueur(joueur));
            joueursGagnants = new LinkedList<>(trouverJoueursGagnants(joueursDansLaTournee.getJoueurs()));
            descriptionPot.append(" || ");
            if(potsSecondairesDansJeu && joueursGagnants.getFirst().valeurAllInIncomplet!=0){
                System.out.println("Gagnant pot secondaire");
                int potTotal = potActuel;
                potActuel = potActuel/joueursGagnants.size();
                for(Joueur j : joueursGagnants){
                    if(j.valeurAllInIncomplet!=0) {
                        if (potActuel > potsSecondaires.get(j)) {
                            descriptionPot.append(j.nom).append(" : ").append(potsSecondaires.get(j)).append(" fiches ||");
                            j.ajouterArgent(potsSecondaires.get(j));
                            potTotal = potTotal - potsSecondaires.get(j);
                        } else {
                            descriptionPot.append(j.nom).append(" : ").append(potActuel).append(" fiches ||");
                            j.ajouterArgent(potActuel);
                            potTotal = potTotal - potActuel;
                        }
                    }
                    else{
                        descriptionPot.append(j.nom).append(" : ").append(potActuel).append(" fiches");
                        j.ajouterArgent(potActuel);
                        potTotal = potTotal - potActuel;
                    }
                }
                if(potTotal>0){
                    LinkedList<Joueur> dansTournee = joueursDansLaTournee.getJoueurs();
                    dansTournee.removeAll(joueursGagnants);
                    ajouterKicker(dansTournee);
                    if(!dansTournee.isEmpty()){
                        dansTournee.sort(Collections.reverseOrder());
                        while(potTotal>0 && dansTournee.size()>=1){
                            if(potsSecondaires.containsKey(dansTournee.getFirst())) {
                                if (potTotal > potsSecondaires.get(dansTournee.getFirst())) {
                                    dansTournee.getFirst().ajouterArgent(potsSecondaires.get(dansTournee.getFirst()));
                                    descriptionPot.append(" || ").append(dansTournee.getFirst().nom).append(" : ").append(potsSecondaires.get(dansTournee.getFirst()));
                                    potTotal = potTotal - potsSecondaires.get(dansTournee.getFirst());
                                } else {
                                    dansTournee.getFirst().ajouterArgent(potTotal);
                                    descriptionPot.append(" || ").append(dansTournee.getFirst().nom).append(" prend les ").append(potTotal).append(" fiches restantes ||");
                                    potTotal = 0;
                                }
                            }
                            else {
                                dansTournee.getFirst().ajouterArgent(potTotal);
                                descriptionPot.append(" || ").append(dansTournee.getFirst().nom).append(" prend les ").append(potTotal).append(" fiches restantes ||");
                                potTotal = 0;
                            }
                            dansTournee.removeFirst();
                        }
                    }
                }
            }
            else {
                potActuel = potActuel / joueursGagnants.size();
                //Cas où le joeurs gagnant n'a pas payé la valeur complète du pari et a mis AllIn
                joueursGagnants.forEach(joueur -> {
                    joueur.ajouterArgent(potActuel);
                    fenetre.mettreAJourInfosJoueur(joueur);
                    descriptionPot.append(joueur.nom).append(" : ").append(potActuel).append(" fiches || ");
                });
            }
            fenetre.afficherHandGagnante(false, descriptionPot.toString());
        }
        fenetre.afficherBoutonProchaineTournee();
    }

    public void completerPotsSecondaires(int valeur){
        System.out.println("Completer pots secondaires");
        potsSecondaires.keySet().forEach((joueur) -> {
            if(!joueur.potsDejaCompletes) {
                int pot = potsSecondaires.get(joueur);
                if (valeur > joueur.valeurAllInIncomplet) {
                    pot = pot + (joueur.valeurAllInIncomplet);
                } else {
                    pot = pot + valeur;
                }
                potsSecondaires.replace(joueur, pot);
            }
        });
        System.out.println(potsSecondaires.toString());
    }

    public void creerPotsSecondaires(Joueur j){
        potsSecondairesDansJeu=true;
        if(!flop){
            potsSecondaires.put(j, (j.getArgent()+j.derniereValeurPariee)*(nJueursQuiOntPayeLeTour+1));
        }
        else if(!turn){
            potsSecondaires.put(j, potAvantFlop + (j.getArgent()+j.derniereValeurPariee)*(nJueursQuiOntPayeLeTour+1));
        }
        else if(!river){
            potsSecondaires.put(j, potAvantTurn + (j.getArgent()+j.derniereValeurPariee)*(nJueursQuiOntPayeLeTour+1));
        }
        else{
            potsSecondaires.put(j, potAvantRiver + (j.getArgent()+j.derniereValeurPariee)*(nJueursQuiOntPayeLeTour+1));
        }
        System.out.println(potsSecondaires.toString());
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
                if (j.getHand().getValeurHandApresRiver() <= 14) {
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
                else if (j.getHand().getValeurHandApresRiver() >= 20 && j.getHand().getValeurHandApresRiver() <= 140) {
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
                else if (j.getHand().getValeurHandApresRiver() >= 230 && j.getHand().getValeurHandApresRiver() <= 1530) {
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
                else if (j.getHand().getValeurHandApresRiver() >= 2000 && j.getHand().getValeurHandApresRiver() <= 14000) {
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
                else if (j.getHand().getValeurHandApresRiver() >= 300002 && j.getHand().getValeurHandApresRiver() <= 300014) {
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
        if(joueursEgaux.size()>1) {
            return joueursEgaux.getFirst().getHand().getValeurHandApresRiver() > joueursEgaux.get(1).getHand().getValeurHandApresRiver();
        }
        else {
            return true;
        }
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
        if (joueursDansLaTournee.getFirst().getHand().getValeurHandApresRiver() > joueursDansLaTournee.get(1).getHand().getValeurHandApresRiver()){
            joueursGagnants.add(joueursDansLaTournee.getFirst());
        }

        else {
            int v1 = joueursDansLaTournee.getFirst().getHand().getValeurHandApresRiver();
            joueursDansLaTournee.removeIf(joueur -> joueur.getHand().getValeurHandApresRiver() < v1);
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
                int v2 = joueursDansLaTournee.getFirst().getHand().getValeurHandApresRiver();
                joueursDansLaTournee.removeIf(joueur -> joueur.getHand().getValeurHandApresRiver() < v2);
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
        nomsJoueursOrdinateurs.add("Nicolas Stouls");
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