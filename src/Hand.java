import javax.swing.*;
import java.util.*;


public class Hand  implements Comparable{

    private LinkedList<Carte> surMain;
    private LinkedList<Carte> apresFlop;
    private LinkedList<Carte> apresTurn;
    private LinkedList<Carte> apresRiver;
    private LinkedList<Carte> hand;

    private String description = ""; //Description textuelle de chaque hand Ex: Pair de dames
    private int valeurHandApresRiver = -1;
    private int valeurHandSurMain = -1;
    private int valeurHandApresFlop = -1;
    private int valeurHandApresTurn = -1;
    /*
    Idée derrière valeurHand:
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

    public LinkedList<Carte> getToutesCartes(){return apresRiver;}

    public LinkedList<Carte> getSurMain() {
        return surMain;
    }

    public LinkedList<Carte> getCartesHand() {
        if (hand.isEmpty()) {
            calculerValeurHand(apresRiver);
            return hand;
        }
        else{
            return hand;
        }
    }

    public String getDescription(){
        return description;
    }

    public void ajouterDescription(String aAjouter){
        description+=aAjouter;
    }


    public void ajouterCarteKicker(Carte kicker){
        hand.add(kicker);
        valeurHandApresRiver=valeurHandApresRiver+kicker.valeur;
    }

    public void setHand(LinkedList<Carte> cartesSurMain, LinkedList<Carte> cartesTable){
        surMain= new LinkedList<>();
        apresFlop = new LinkedList<>();
        apresTurn = new LinkedList<>();
        apresRiver = new LinkedList<>();
        hand = new LinkedList<>();
        valeurHandSurMain = -1;
        valeurHandApresFlop = -1;
        valeurHandApresTurn = -1;
        valeurHandApresRiver = -1;
        Collections.sort(cartesSurMain,Collections.reverseOrder());
        this.surMain = cartesSurMain;
        //On définit la liste de cartes après flop étant les 2 sur main + les 3 premières de la table.
        this.apresFlop.addAll(cartesSurMain);
        this.apresFlop.add(cartesTable.getFirst());
        this.apresFlop.add(cartesTable.get(1));
        this.apresFlop.add(cartesTable.get(2));
        Collections.sort(apresFlop, Collections.reverseOrder());
        //On définit la liste de cartes après turn étant les 2 sur main + les 4 premières de la table.
        this.apresTurn.addAll(apresFlop);
        this.apresTurn.add(cartesTable.get(3));
        Collections.sort(apresTurn, Collections.reverseOrder());
        //On définit la liste de cartes après river étant les 2 sur main + les 5 sur table.
        this.apresRiver.addAll(cartesTable);
        this.apresRiver.addAll(surMain);
        Collections.sort(apresRiver, Collections.reverseOrder());
    }

    public int getValeurHandApresRiver(){
        if(valeurHandApresRiver!=-1) {
            return valeurHandApresRiver;
        }
        else{
            calculerValeurHand(apresRiver);
            return valeurHandApresRiver;
        }
    }


    public int getValeurHandSurMain() {
        if(valeurHandSurMain!=-1) {
            return valeurHandSurMain;
        }
        else{
            calculerValeurHand(surMain);
            return valeurHandSurMain;
        }
    }

    public int getValeurHandApresFlop(){
        if(valeurHandApresFlop!=-1) {
            return valeurHandApresFlop;
        }

        else{
            calculerValeurHand(apresFlop);
            return valeurHandApresFlop;
        }
    }

    public int getValeurHandApresTurn(){
        if(valeurHandApresTurn!=-1) {
            return valeurHandApresTurn;
        }
        else{
            calculerValeurHand(apresTurn);
            return valeurHandApresTurn;
        }
    }

    /*
    Verifie la quantite de pairs dans une hand et retourne une LL avec les cartes trouvées
     */
    private LinkedList<Carte> pairs(LinkedList<Carte> cartes){
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

    private LinkedList<Carte> threeOfAKind(LinkedList<Carte> cartes){
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
        threeOfAKind.sort(Collections.reverseOrder());
        if(threeOfAKind.size()>=3) {
            while (threeOfAKind.size() > 3) {
                threeOfAKind.removeLast();
            }
            return threeOfAKind;
        }
        else{return null;}
    }


    private LinkedList<Carte> fourOfAKind(LinkedList<Carte> cartes){
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
    private LinkedList<Carte> flush(LinkedList<Carte> cartes){
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
            // flush() renvoie la totalite des cartes de la meme couleur, 5, 6 ou 7. (important pour straightFlush()), mais valeur calculé avec les 5 plus fortes
            return flush;
        }
        else{
            return null;
        }
    }

    /*
        Retourne la carte de valeur plus haute dans la hand
     */
    private Carte highCard(LinkedList<Carte> cartes){
        Collections.sort(cartes, Collections.reverseOrder());
        return cartes.getFirst();
    }

    /*
    Verifie s'il y a un full house dans la hand et retourne une liste avec
    les cartes du full house, sinon retourne null
     */
    private LinkedList<Carte> fullHouse(LinkedList<Carte> cartes){
        LinkedList<Carte> fullHouse = new LinkedList<Carte>();
        if(pairs(cartes)!=null && threeOfAKind(cartes)!=null && pairs(cartes).size()>=2 && threeOfAKind(cartes).size()>=1){ //possibilité d'avoir deux pairs et une TOAK
            LinkedList<Carte> pair = pairs(cartes);
            LinkedList<Carte> brelan = threeOfAKind(cartes);
            fullHouse.addAll(brelan);
            fullHouse.add(pair.get(0));
            fullHouse.add(pair.get(1));
            return fullHouse;
        }
        else{
            return null;
        }
    }

    private LinkedList<Carte> straight(LinkedList<Carte> cartes){
        LinkedList<Carte> candidats = new LinkedList<>(cartes);
        candidats.sort(Collections.reverseOrder());
        int i = 0;
        int enlevees = 0;
        while(i+1 < candidats.size()){
            if (candidats.get(i).valeur-1 != candidats.get(i+1).valeur) {
                if (candidats.get(i).valeur == candidats.get(i+1).valeur){
                    candidats.remove(i+1);
                    enlevees++;
                }
                else{
                        int j = i;
                        while (j >= 0) {
                            candidats.remove(j);
                            j--;
                            enlevees++;
                        }
                }
            }
            else{
                i++;
            }
            //Straight n'est pas possible.
            if((cartes.size()==5 && enlevees>1) || (cartes.size()==6 && enlevees>2) || (cartes.size()==7 && enlevees>3)){
                break;
            }
        }
        //Si une suite n'a pas été trouvé, on cherche si on peut avoir une commençant par As, et on change la valeur de l'As de 14 à 1.
        if (candidats.size()<5){
            //Une suite commençant par As n'est possible que s'il y a un As et un Deux dans les cartes
            LinkedList<Carte> verification = new LinkedList<>(cartes);
            verification.sort(Collections.reverseOrder());
            if(verification.getFirst().valeur==14 && verification.getLast().valeur==2) {
                candidats = new LinkedList<>(cartes);
                candidats.forEach(carte -> {
                    if (carte.valeur == 14) {
                        carte.valeur = 1;
                    }
                });
                i=0;
                enlevees=0;
                candidats.sort(Collections.reverseOrder());
                while(i+1 < candidats.size()){
                    if (candidats.get(i).valeur-1 != candidats.get(i+1).valeur) {
                        if (candidats.get(i).valeur == candidats.get(i+1).valeur){
                            candidats.remove(i+1);
                            enlevees++;
                        }
                        else{
                                int j = i;
                                while (j >= 0) {
                                    candidats.remove(j);
                                    j--;
                                    enlevees++;
                                }
                        }
                    }
                    else{
                        i++;
                    }
                    //Straight n'est pas possible.
                    if((cartes.size()==5 && enlevees>=1) || (cartes.size()==6 && enlevees>=2) || (cartes.size()==7 && enlevees>=3)){
                        break;
                    }
                }
                Collections.sort(candidats);
                cartes.forEach(carte -> {
                    if(carte.valeur==1){
                        carte.valeur=14;
                    }
                });
            }
        }
        if(candidats.size()<5){
            return null;
        }

        else{
            while(candidats.size()>5){
                candidats.removeFirst();
            }
            return candidats;
        }

    }

    /*
    RETORNA UMA LISTA COM OS valeurES DO STRAIGHT FLUSH EM ORDEM DESCRESCENTE
     */
    private LinkedList<Carte> straightFlush(LinkedList<Carte> cartes) {
        // verif de flush()!=null et staight()!=null pas necessaire, car deja ne sera appellee si les deux sont dif de null
        /* Idée de la méthode: verifier si la liste flush() contient un Straight
         */
        if(flush(cartes)!=null){
            return straight(flush(cartes));
        }
        else{
            return null;
        }
    }

    /*
    RETORNA TRUE SE HOUVER UM ROYAL STRAIGHT FLUSH NA HAND E FALSE SE NAO HOUVER
     */
    private boolean royalStraightFlush(LinkedList<Carte> cartes){
        return straightFlush(cartes)!=null && straightFlush(cartes).getLast().valeur==14;
    }

    public int compareTo(Object h2) {
        int comparaison = 0;
        if (valeurHandApresRiver > ((Hand)h2).valeurHandApresRiver) {
            comparaison = 1;
        } else if (valeurHandApresRiver < ((Hand)h2).valeurHandApresRiver) {
            comparaison = -1;
        }
        return comparaison;
    }


    public void calculerValeurHand(LinkedList<Carte> cartesDeLaHand) {
        //Valeurs Han Sur Main
        if (cartesDeLaHand.size() == 2) {
            if (pairs(cartesDeLaHand) != null) {
                LinkedList<Carte> pairs = pairs(cartesDeLaHand);
                if (pairs.size() == 2) {
                    valeurHandSurMain = 10 * pairs.getFirst().valeur;
                } else if (pairs.size() == 4) {
                    valeurHandSurMain = 100 * pairs.getFirst().valeur + pairs.getLast().valeur;
                }
            } else {
                valeurHandSurMain = highCard(surMain).valeur;
            }
        }
        //Calcul de la valeur de la hand après flop, turn et river.
        else {
            LinkedList<Carte> result = new LinkedList<>();
            if (royalStraightFlush(cartesDeLaHand)) {
                result.addAll(flush(cartesDeLaHand));
                //Si cartesDeLaHand.size()==5, c'est-à-dire que c'est après le flop.
                if(cartesDeLaHand.size()==5){
                    valeurHandApresFlop = 500000;
                }
                //Si cartesDeLaHand.size()==6, c'est-à-dire que c'est après le turn.
                else if(cartesDeLaHand.size()==6){
                    valeurHandApresTurn = 500000;
                }
                //Si cartesDeLaHand.size()==7, c'est-à-dire que c'est après le river.
                else{
                    description = "Royal straight flush";
                    valeurHandApresRiver = 500000;
                }
            } else if (straightFlush(cartesDeLaHand) != null) {
                result.addAll(straightFlush(cartesDeLaHand));
                if(cartesDeLaHand.size()==5){
                    valeurHandApresFlop = 400000 + result.getFirst().valeur;
                }
                else if(cartesDeLaHand.size()==6){
                    valeurHandApresTurn = 400000 + result.getFirst().valeur;
                }
                else{
                    description = "Straight flush, carte haute " + result.getFirst().description(false);
                    valeurHandApresRiver = 400000 + result.getFirst().valeur;
                }
            }

            else if (fourOfAKind(cartesDeLaHand) != null) {
                result.addAll(fourOfAKind(cartesDeLaHand));
                if(cartesDeLaHand.size()==5){
                    valeurHandApresFlop = 300000 + result.getFirst().valeur;
                }
                else if(cartesDeLaHand.size()==6) {
                    valeurHandApresTurn = 300000 + result.getFirst().valeur;
                }
                else{
                    valeurHandApresRiver = 300000 + result.getFirst().valeur;
                    description = "Carre de " + result.getFirst().description(true);
                }
            }

            else if (fullHouse(cartesDeLaHand) != null) {
                result.addAll(fullHouse(cartesDeLaHand));
                if(cartesDeLaHand.size()==5){
                    valeurHandApresFlop = 200000 + 10 * result.getFirst().valeur + result.getLast().valeur;
                }
                else if(cartesDeLaHand.size()==6) {
                    valeurHandApresTurn = 200000 + 10 * result.getFirst().valeur + result.getLast().valeur;
                }
                else{
                    valeurHandApresRiver = 200000 + 10 * result.getFirst().valeur + result.getLast().valeur;
                    description = "Full House, trois " + result.getFirst().description(true) + " et deux " + result.getLast().description(true);
                }
            }

            else if (flush(cartesDeLaHand) != null) {
                result.addAll(flush(cartesDeLaHand));
                while (result.size() > 5) {
                    result.removeLast();
                }
                if(cartesDeLaHand.size()==5){
                    valeurHandApresFlop = 10000 * result.getFirst().valeur + 1000 * result.get(1).valeur + 100 * result.get(2).valeur + 10 * result.get(3).valeur + result.get(4).valeur;
                }
                else if(cartesDeLaHand.size()==6) {
                    valeurHandApresTurn = 10000 * result.getFirst().valeur + 1000 * result.get(1).valeur + 100 * result.get(2).valeur + 10 * result.get(3).valeur + result.get(4).valeur;
                }
                else{
                    valeurHandApresRiver = 10000 * result.getFirst().valeur + 1000 * result.get(1).valeur + 100 * result.get(2).valeur + 10 * result.get(3).valeur + result.get(4).valeur;
                    description = "Flush, carte haute " + result.getFirst().description(false);
                }
            }

            else if (straight(cartesDeLaHand) != null) {
                result.addAll(straight(cartesDeLaHand));
                if(cartesDeLaHand.size()==5){
                    valeurHandApresFlop = 15000 + result.getLast().valeur;
                }
                else if(cartesDeLaHand.size()==6) {
                    valeurHandApresTurn = 15000 + result.getLast().valeur;
                }
                else{
                    valeurHandApresRiver = 15000 + result.getLast().valeur;
                    description = "Suite, carte haute " + result.getLast().description(false);
                }
            }

            else if (threeOfAKind(cartesDeLaHand) != null) {
                result.addAll(threeOfAKind(cartesDeLaHand));
                if(cartesDeLaHand.size()==5){
                    valeurHandApresFlop = 1000 * result.getFirst().valeur;
                }
                else if(cartesDeLaHand.size()==6) {
                    valeurHandApresTurn = 1000 * result.getFirst().valeur;
                }
                else{
                    valeurHandApresRiver = 1000 * result.getFirst().valeur;
                    description = "Brelan de " + result.getFirst().description(true);
                }
            }

            else if (pairs(cartesDeLaHand) != null) {
                result.addAll(pairs(cartesDeLaHand));
                if(cartesDeLaHand.size()==5){
                    if (result.size() == 2) {
                        valeurHandApresFlop = 10 * result.getFirst().valeur;
                    } else if (result.size() == 4) {
                        valeurHandApresFlop = 100 * result.getFirst().valeur + result.getLast().valeur;
                    }
                }
                else if(cartesDeLaHand.size()==6) {
                    if (result.size() == 2) {
                        valeurHandApresTurn = 10 * result.getFirst().valeur;
                    } else if (result.size() == 4) {
                        valeurHandApresTurn = 100 * result.getFirst().valeur + result.getLast().valeur;
                    }
                }
                else{
                    if (result.size() == 2) {
                        valeurHandApresRiver = 10 * result.getFirst().valeur;
                        description = "Pair de " + result.getFirst().description(true);
                    } else if (result.size() == 4) {
                        valeurHandApresRiver = 100 * result.getFirst().valeur + result.getLast().valeur;
                        description = "Deux pairs, " + result.getFirst().description(true) + " et " + result.getLast().description(true);
                    }
                }
            }

            else {
                result.add(highCard(cartesDeLaHand));
                if(cartesDeLaHand.size()==5){
                    valeurHandApresFlop = result.getFirst().valeur;
                }
                else if(cartesDeLaHand.size()==6) {
                    valeurHandApresTurn = result.getFirst().valeur;
                }
                else{
                    valeurHandApresRiver = result.getFirst().valeur;
                    description = "Carte haute, " + result.getFirst().description(false);
                }
            }

            if (cartesDeLaHand.size()==7){
                hand=result;
            }
        }
    }

    public static void main(String[] args){
        LinkedList<Carte> cartes = new LinkedList<>();
        LinkedList<Carte> main = new LinkedList<>();
        main.add(new Carte(14,'c'));
        main.add(new Carte(2,'t'));
        cartes.add(new Carte(5,'c'));
        cartes.add(new Carte(3,'d'));
        cartes.add(new Carte(4,'p'));
        cartes.add(new Carte(8,'t'));
        cartes.add(new Carte(9,'t'));
        Hand h = new Hand();
        h.setHand(main,cartes);
        if(h.straight(h.apresRiver)!=null) {
            System.out.println(h.straight(h.apresRiver).toString());
        }

        System.out.println(h.getValeurHandApresRiver());
        System.out.println(h.getToutesCartes().toString());
    }
}


