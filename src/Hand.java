import javax.swing.*;
import java.util.*;


public class Hand  implements Comparable{

    private LinkedList<Carte> cartes = new LinkedList<>();
    private LinkedList<Carte> surMain= new LinkedList<>();
    private LinkedList<Carte> surTable= new LinkedList<>();
    private LinkedList<Carte> hand = new LinkedList<>();
    private String description = ""; //Description textuelle de chaque hand Ex: Pair de dames
    private int valeurHand = -1;
    /*
    Idée derrière valeurHand: *IDEIA FODA*
    - Chaque hand aura une valeur correspondant aux cartes presentes, on pourra donc utilise Comparable/compareTo() et classer les hands sur jeu
    - VALEURS:
    Hand                           Calcul                                                                   Range                          DONE
    HC                    valeurHand = valeur de la carte haute                                             [2 -14]                     OK
    Pair                  valeurHand = 10*(valeur du pair)                                                  [20 - 140]                  OK
    TwoPairs              valeurHand = 100*(valeur du pair plus haut) + (valeur du 2eme pair)               [230 - 1530]                OK
    ThreeOAK              valeurHand = 1.000*(valeur du brelan)                                             [2000 - 14000]              OK
    Straight              valeurHand = 15.000 + (valeur de la carte haute de la suite)                      [15.005 - 15.0014]          OK
    Flush                 valeurHand = 10.000*(haute) + 1.000*(2eme) + 100*(3eme) + 10*(4eme) +(5eme)       [23.457 - 154319]           OK
    FullHouse             valeurHand = 200.000 + 10*(valeur du brelan) + (valeur du pair)                   [200.023 - 200.153]         OK
    FourOAK               valeurHand = 300.000 + (valeur du carre)                                          [300.002 - 300.014]         OK
    StraightFlush         valeurHand = 400.000 + (valeur de la carte haute)                                 [400.005 - 400.013]         OK
    RoyalStraightFlush    valeurHand = 500.000                                                              500.000                     OK

    Si deux hands ont la même valeur, on ajoutera la valeur du kicker quand applicable
     */

    public Hand(){
    }

    public LinkedList<Carte> getToutesCartes(){return cartes;}

    public LinkedList<Carte> getSurMain() {
        return surMain;
    }

    public LinkedList<Carte> getSurTable() {return surTable;}

    public LinkedList<Carte> getCartesHand() {return hand;}

    public String getDescription(){
        return description;
    }

    public void ajouterDescription(String aAjouter){
        description+=aAjouter;
    }

    public void ajouterValeurKicker(int valeurKicker){
        valeurHand=valeurHand+valeurKicker;
    }

    public void ajouterCarteKicker(Carte kicker){
        hand.add(kicker);
    }

    public void setHand(LinkedList<Carte> cartesSurMain, LinkedList<Carte> cartesTable){
        Collections.sort(cartesSurMain,Collections.reverseOrder());
        this.surMain = cartesSurMain;
        this.surTable = cartesTable;
        this.cartes.addAll(surTable);
        this.cartes.addAll(surMain);
        Collections.sort(cartes, Collections.reverseOrder());
        calculerValeurHand();
    }

    public int getValeurHand(){
        return valeurHand;
    }
    /*
    Verifie la quantite de pairs dans une hand et retourne une LL avec les cartes trouvées
     */
    private LinkedList<Carte> pairs(){
        LinkedList<Carte> pairs = new LinkedList<>();
        for (int i=2;i<=14;i++){
            int q = 0;
            LinkedList<Carte> cartesTrouvees = new LinkedList<>();
            for(Carte c : cartes){
                if(c.valeur==i){
                    q++;
                    cartesTrouvees.add(c);
                }
            }
            if (q==2){
                pairs.addAll(cartesTrouvees);
            }
        }
        Collections.sort(pairs,Collections.reverseOrder());
        if(pairs.size()>=2) {
            while (pairs.size() > 4) {
                pairs.removeLast();
            }
            return pairs;
        }
        else{return null;}
    }

    private LinkedList<Carte> threeOfAKind(){
        LinkedList<Carte> threeOfAKind = new LinkedList<>();
        for (int i=2;i<=14;i++){
            int q=0;
            LinkedList<Carte> cartesTrouvees = new LinkedList<>();
            for(Carte c : cartes){
                if(c.valeur==i){
                    q++;
                    cartesTrouvees.add(c);
                }
            }
            if(q==3){
                threeOfAKind.addAll(cartesTrouvees);
            }
        }
        Collections.sort(threeOfAKind,Collections.reverseOrder());
        if(threeOfAKind.size()>=3) {
            while (threeOfAKind.size() > 3) {
                threeOfAKind.removeLast();
            }
            return threeOfAKind;
        }
        else{return null;}
    }


    private LinkedList<Carte> fourOfAKind(){
        LinkedList<Carte> fourOfAKind = new LinkedList<>();
        for (int i=2;i<=14;i++){
            int q=0;
            LinkedList<Carte> cartesTrouvees = new LinkedList<>();
            for(Carte c : cartes){
                if(c.valeur==i){
                    q++;
                    cartesTrouvees.add(c);
                }
            }
            if(q==4){
                fourOfAKind.addAll(cartesTrouvees);
                break;
            }
        }
        if(fourOfAKind.size()==4){
            return fourOfAKind;
        }
        else{return null;}
    }

    /*
    S'il y a un flush dans la hand, la methode retourne une LL avec les 5 cartes du flush, sinon elle retourne null
     */
    private LinkedList<Carte> flush(){
        int qt=0; //QUANTITE DE CARTES DE TREFLES
        int qc=0; //QUANTITE DE CARTES DE COEURS
        int qp=0; //QUANTITE DE CARTES DE PIQUES
        int qk=0; //QUANTITR DE CARTES DE CARREAUX
        LinkedList<Carte> flush=new LinkedList<>();

        /*
        La boucle compte la quantité de cartes de chaque couleur dans la hand
         */
        for(Carte c:cartes){
            if(c.couleur=='t'){
                qt++;
            }
            else if(c.couleur=='c'){
                qc++;
            }
            else if(c.couleur=='p'){
                qp++;
            }
            else if(c.couleur=='d'){
                qk++;
            }
        }
        /*
        Verification s'il y a 5 ou plus cartes d'une meme couleur dans la hand,
        dans ce cas, ajoute les cartes correspondantes à la liste flush
         */
        if(qt>=5){
            for(Carte c:cartes){
                if(c.couleur=='t'){
                    flush.add(c);
                }
            }
        }
        else if(qc>=5){
            for(Carte c:cartes){
                if(c.couleur=='c'){
                    flush.add(c);
                }
            }
        }
        else if(qp>=5){
            for(Carte c:cartes){
                if(c.couleur=='p'){
                    flush.add(c);
                }
            }
        }
        else if(qk>=5){
            for(Carte c:cartes){
                if(c.couleur=='d'){
                    flush.add(c);
                }
            }
        }
        if(flush.size()>=5) {
            Collections.sort(flush, Collections.reverseOrder()); //TRIE LA LISTE EN VALEURS DECROISSANTES
            while (flush.size()>5){
                flush.removeLast();
            }
            return flush;
        }
        else{
            return null;
        }
    }

    /*
        Retourne la carte de valeur plus haute dans la hand
     */
    private Carte highCard(){
        Collections.sort(cartes, Collections.reverseOrder());
       return cartes.getFirst();
    }

    /*
    Verifie s'il y a un full house dans la hand et retourne une liste avec
    les cartes du full house, sinon retourne null
     */
    private LinkedList<Carte> fullHouse(){
        LinkedList<Carte> fullHouse = new LinkedList<Carte>();
        if(pairs()!=null && threeOfAKind()!=null && pairs().size()>=2 && threeOfAKind().size()>=1){ //possibilité d'avoir deux pairs et une TOAK
            LinkedList<Carte> pair = pairs();
            LinkedList<Carte> brelan = threeOfAKind();
            fullHouse.addAll(brelan);
            fullHouse.addAll(pair);
            return fullHouse;
        }
        else{
            return null;
        }
    }

    // si straight
    private LinkedList<Carte> straight(){
        // 5 das sete cartes sejam em sequencia // caso a parte pro As 2 3 4 5
        LinkedList<Carte> straight = new LinkedList<Carte>();
        if(!isRoyalStraightPossible() && cartes.get(0).valeur==14){
            cartes.get(0).valeur=1;
            if(cartes.get(1).valeur==13) {
                cartes.get(1).valeur = 1;
            }
            Collections.sort(cartes, Collections.reverseOrder());
            // si un troisieme ace dans la hand, Royal Straight false.
        }
        straight.add(cartes.get(0));
        for(int i =1; i<cartes.size() ;i++) {
            if(straight.size()<5) {
                if (cartes.get(i - 1).valeur == (cartes.get(i).valeur + 1)) {
                    straight.add(cartes.get(i));
                } else if(cartes.get(i - 1).valeur == (cartes.get(i).valeur )) {
                // regarder si les couleurs sont égales à celles déjà ajoutées
                    if(straight.get(0).couleur == cartes.get(i).couleur){
                        straight.add(cartes.get(i));
                    }
                }else {
                    straight.removeAll(straight);
                    straight.add(cartes.get(i));
                }
            }

        }

        if(straight.size() <5) {
            straight = null;
            Collections.sort(cartes,Collections.reverseOrder());
            if(cartes.getLast().valeur==1){ // si pas de straight 12345, rechanger la valeur du ace pour ne pas gener les autres methodes
                cartes.getLast().valeur = 14;
            }
        }


        return straight;
    }


    private LinkedList<Carte> straight(LinkedList<Carte> flush){
        // meme methode mais en utilisant flush a la place de cartes, appelle juste dans straightFlush()
        LinkedList<Carte> straight = new LinkedList<Carte>();
        if(!isRoyalStraightPossible() && flush.get(0).valeur==14){
            flush.get(0).valeur=1;
            if(flush.get(1).valeur==13) {
                flush.get(1).valeur = 1;
            }
            Collections.sort(flush, Collections.reverseOrder());
            // si un troisieme ace dans la hand, Royal Straight false.
        }
        straight.add(flush.get(0));
        for(int i =1; i<flush.size() ;i++) {
            if(straight.size()<5) {
                if (flush.get(i - 1).valeur == (flush.get(i).valeur + 1)) {
                    straight.add(flush.get(i));
                } else if(flush.get(i - 1).valeur == (flush.get(i).valeur )) {
                    // regarder si les couleurs sont égales à celles déjà ajoutées
                    if(straight.get(0).couleur == flush.get(i).couleur){
                        straight.add(flush.get(i));
                    }
                }else {
                    straight.removeAll(straight);
                    straight.add(flush.get(i));
                }
            }

        }
        for(Carte c : flush){
            if(c.valeur==1){
                c.valeur=14;
            }
        }
        if(straight.size() <5) {
            straight = null; // pas besoin de rechanger la valeur parce qu'on utilise flush et pas cartes
        }
        return straight;
    }

    private boolean isRoyalStraightPossible(){
        boolean possibilite = false;
        Carte ace = new Carte(14, 't');
        Carte roi = new Carte(13, 't');
        Carte dame = new Carte(12, 't');
        //Vérifier si les trois premieres cartes sont Ace, Roi et dame
        if(cartes.get(0).compareTo(ace)==0){
            if(cartes.get(1).compareTo(ace)==0 ||cartes.get(1).compareTo(roi)==0 ) {
                if(cartes.get(2).compareTo(roi)==0||cartes.get(2).compareTo(dame)==0){
                    possibilite = true;
                }
            }

        }
        return possibilite;
    }

    /*
    RETORNA UMA LISTA COM OS valeurES DO STRAIGHT FLUSH EM ORDEM DESCRESCENTE
     */
    private LinkedList<Carte> straightFlush() {
        // verif de flush()!=null et staight()!=null pas necessaire, car deja ne sera appellee si les deux sont dif de null
        /* Idée de la méthode: verifier si la liste flush() contient un Straight
         */
        if(flush()!=null){
            return straight(flush());
        }
        else{
            return null;
        }
    }

    /*
    RETORNA TRUE SE HOUVER UM ROYAL STRAIGHT FLUSH NA HAND E FALSE SE NAO HOUVER
     */
    private boolean royalStraightFlush(){
            return straightFlush()!=null && straightFlush().get(0).valeur == 14;
    }

    public int compareTo(Object h2) {
        int comparaison = 0;
        if (valeurHand > ((Hand)h2).valeurHand) {
            comparaison = 1;
        } else if (valeurHand < ((Hand)h2).valeurHand) {
            comparaison = -1;
        }
        return comparaison;
    }

    public void calculerValeurHand(){
        LinkedList<Carte> result = new LinkedList<>();
            if(royalStraightFlush()){
                result.addAll(flush());
                description = "Royal straight flush";
                valeurHand = 500000;
            }
            else if(straightFlush()!=null){
                result.addAll(straightFlush());
                description = "Straight flush, carte haute "+result.getFirst().description(false);
                valeurHand = 400000 + result.getFirst().valeur;
            }
            else if(fourOfAKind()!=null){
                result.addAll(fourOfAKind());
                valeurHand = 300000 + result.getFirst().valeur;
                description = "Carre de "+result.getFirst().description(true);
            }
            else if(fullHouse()!=null){
                result.addAll(fullHouse());
                valeurHand = 200000 + 10*result.getFirst().valeur + result.getLast().valeur;
                description = "Full House, trois "+result.getFirst().description(true)+" et deux "+result.getLast().description(true);
            }
            else if(flush()!=null){
                result.addAll(flush());
                valeurHand = 10000*result.getFirst().valeur + 1000*result.get(1).valeur + 100*result.get(2).valeur + 10*result.get(3).valeur + result.get(4).valeur;
                description = "Flush, carte haute "+result.getFirst().description(false);
            }
            else if(straight()!=null){
                result.addAll(straight());
                valeurHand = 15000 + result.getFirst().valeur;
                description = "Suite, carte haute "+result.getFirst().description(false);
            }
            else if(threeOfAKind()!=null){
                result.addAll(threeOfAKind());
                valeurHand = 1000*result.getFirst().valeur;
                description = "Brelan de "+result.getFirst().description(true);
            }
            else if(pairs()!=null){
                result.addAll(pairs());
                if (result.size()==2){
                    valeurHand = 10*result.getFirst().valeur;
                    description = "Pair de "+result.getFirst().description(true);
                }
                else if (result.size()==4){
                    valeurHand = 100*result.getFirst().valeur + result.getLast().valeur;
                    description = "Deux pairs, "+result.getFirst().description(true)+" et "+result.getLast().description(true);
                }
            }
            else {
                result.add(highCard());
                valeurHand = result.getFirst().valeur;
                description = "Carte haute, "+result.getFirst().description(false);
            }
            hand=result;
    }
}


