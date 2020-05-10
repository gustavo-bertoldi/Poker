import javax.swing.*;
import java.util.*;


public class Hand  implements Comparable {

    private LinkedList<Carte> surMain;

    private LinkedList<Carte> surTable;
    private LinkedList<Carte> handMoment;
    private LinkedList<Carte> hand;

    private String description = ""; //Description textuelle de chaque hand Ex: Pair de dames

    private Range rangePreFlop;
    private int valeurHandMoment = -1; // Utilisée pour l'inteligence lors de la prise de decisions
    private int valeurHandFinale = -1; // Utilisée pour la comparaison entre joueurs, donc, pour trouver le gagnant lors de la fin de la tournee

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

    public Hand() {
        handMoment = new LinkedList<>();
        hand = new LinkedList<>();
        surMain = new LinkedList<>();
        surTable = new LinkedList<>();

    }

    public LinkedList<Carte> getToutesCartes() {
        return hand;
    }

    public LinkedList<Carte> getSurMain() {
        return surMain;
    }

    public LinkedList<Carte> getSurTable() {
        return surTable;
    }

    public void resetHand(){
        //surMain = new LinkedList<>();
        surTable = new LinkedList<>();
        handMoment = new LinkedList<>();
        hand = new LinkedList<>();
        description = null;
        rangePreFlop = null;
        valeurHandMoment = -1;

    }

    public LinkedList<Carte> getCartesHand() {
        return handMoment;
    }

    public int getValeurHandFinale(){
        return valeurHandFinale;
    }

    public String getDescription() {
        return description;
    }

    public void ajouterDescription(String aAjouter) {
        description += aAjouter;
    }


    public void ajouterCarteKicker(Carte kicker) {
        hand.add(kicker);
        valeurHandMoment = valeurHandMoment + kicker.valeur;
    }

    public int getValeurHandMoment() {
        return valeurHandMoment;
    }

    public void definirSurMainEtSurTable(LinkedList<Carte> cartesSurMain, LinkedList<Carte> cartesTable) {
        surTable.addAll(cartesTable);

        cartesSurMain.sort(Collections.reverseOrder());

        surMain.addAll(cartesSurMain);
        int i=0;
        while(i<surMain.size()){
            System.out.print(surMain.get(i).toString() + " ");
            i++;
        }
        i=0;

        handMoment.addAll(cartesSurMain);

        System.out.println(" ");
        hand.addAll(cartesSurMain);
        hand.addAll(cartesTable);
        calculerValeurHandMoment(handMoment);
        calculerValeurHandFinale(hand);

    }
    /*
    Met à jour la hand en fonction du moment du jeu:
    à la fin de la méthode, la hand est celle de la valeur la plus grande avec les cartes du joueur à ce moment == reuslt(calculerhand)
     */

    public void setHandMoment(int moment) {
        // pour moment == 0, deja fait sur definirSurMainEtSurTable
        if (moment == 1) {
            handMoment.add(surTable.getFirst());
            handMoment.add(surTable.get(1));
            handMoment.add(surTable.get(2));

        } else if (moment == 2) {
            handMoment.add(surTable.get(3));
        } else if (moment == 3) {
            handMoment = hand;
        }
        handMoment.sort(Collections.reverseOrder());
        calculerValeurHandMoment(handMoment);

    }
    /*
    Verifie la quantite de pairs dans une hand et retourne une LL avec les cartes trouvées
     */
    private LinkedList<Carte> pairs(LinkedList<Carte> cartes) {
        LinkedList<Carte> pairs = new LinkedList<>();
        for (int i = 2; i <= 14; i++) {
            int q = 0;
            LinkedList<Carte> cartesTrouvees = new LinkedList<>();
            for (Carte c : cartes) {
                if (c.valeur == i) {
                    q++;
                    cartesTrouvees.add(c);
                }
            }
            if (q == 2) {
                pairs.addAll(cartesTrouvees);
            }
        }
        pairs.sort(Collections.reverseOrder());
        if (pairs.size() >= 2) {
            while (pairs.size() > 4) {
                pairs.removeLast();
            }
            return pairs;
        } else {
            return null;
        }
    }

    private LinkedList<Carte> threeOfAKind(LinkedList<Carte> cartes) {
        LinkedList<Carte> threeOfAKind = new LinkedList<>();
        for (int i = 2; i <= 14; i++) {
            int q = 0;
            LinkedList<Carte> cartesTrouvees = new LinkedList<>();
            for (Carte c : cartes) {
                if (c.valeur == i) {
                    q++;
                    cartesTrouvees.add(c);
                }
            }
            if (q == 3) {
                threeOfAKind.addAll(cartesTrouvees);
            }
        }
        threeOfAKind.sort(Collections.reverseOrder());
        if (threeOfAKind.size() >= 3) {
            while (threeOfAKind.size() > 3) {
                threeOfAKind.removeLast();
            }
            return threeOfAKind;
        } else {
            return null;
        }
    }


    private LinkedList<Carte> fourOfAKind(LinkedList<Carte> cartes) {
        LinkedList<Carte> fourOfAKind = new LinkedList<>();
        for (int i = 2; i <= 14; i++) {
            int q = 0;
            LinkedList<Carte> cartesTrouvees = new LinkedList<>();
            for (Carte c : cartes) {
                if (c.valeur == i) {
                    q++;
                    cartesTrouvees.add(c);
                }
            }
            if (q == 4) {
                fourOfAKind.addAll(cartesTrouvees);
                break;
            }
        }
        if (fourOfAKind.size() == 4) {
            return fourOfAKind;
        } else {
            return null;
        }
    }

    /*
    S'il y a un flush dans la hand, la methode retourne une LL avec les 5 cartes du flush, sinon elle retourne null
     */
    private LinkedList<Carte> flush(LinkedList<Carte> cartes) {
        int qt = 0; //QUANTITE DE CARTES DE TREFLES
        int qc = 0; //QUANTITE DE CARTES DE COEURS
        int qp = 0; //QUANTITE DE CARTES DE PIQUES
        int qk = 0; //QUANTITR DE CARTES DE CARREAUX
        LinkedList<Carte> flush = new LinkedList<>();
        /*
        La boucle compte la quantité de cartes de chaque couleur dans la hand
         */
        for (Carte c : cartes) {
            if (c.couleur == 't') {
                qt++;
            } else if (c.couleur == 'c') {
                qc++;
            } else if (c.couleur == 'p') {
                qp++;
            } else if (c.couleur == 'd') {
                qk++;
            }
        }
        /*
        Verification s'il y a 5 ou plus cartes d'une meme couleur dans la hand,
        dans ce cas, ajoute les cartes correspondantes à la liste flush
         */
        if (qt >= 5) {
            for (Carte c : cartes) {
                if (c.couleur == 't') {
                    flush.add(c);
                }
            }
        } else if (qc >= 5) {
            for (Carte c : cartes) {
                if (c.couleur == 'c') {
                    flush.add(c);
                }
            }
        } else if (qp >= 5) {
            for (Carte c : cartes) {
                if (c.couleur == 'p') {
                    flush.add(c);
                }
            }
        } else if (qk >= 5) {
            for (Carte c : cartes) {
                if (c.couleur == 'd') {
                    flush.add(c);
                }
            }
        }
        if (flush.size() >= 5) {
            flush.sort(Collections.reverseOrder()); //TRIE LA LISTE EN VALEURS DECROISSANTES
            // flush() renvoie la totalite des cartes de la meme couleur, 5, 6 ou 7. (important pour straightFlush()), mais valeur calculé avec les 5 plus fortes
            return flush;
        } else {
            return null;
        }
    }

    /*
        Retourne la carte de valeur plus haute dans la hand
     */
    private Carte highCard(LinkedList<Carte> cartes) {
        cartes.sort(Collections.reverseOrder());
        return cartes.getFirst();
    }

    /*
    Verifie s'il y a un full house dans la hand et retourne une liste avec
    les cartes du full house, sinon retourne null
     */
    private LinkedList<Carte> fullHouse(LinkedList<Carte> cartes) {
        LinkedList<Carte> fullHouse = new LinkedList<Carte>();
        if (pairs(cartes) != null && threeOfAKind(cartes) != null && pairs(cartes).size() >= 2 && threeOfAKind(cartes).size() >= 1) { //possibilité d'avoir deux pairs et une TOAK
            LinkedList<Carte> pair = pairs(cartes);
            LinkedList<Carte> brelan = threeOfAKind(cartes);
            fullHouse.addAll(brelan);
            fullHouse.add(pair.get(0));
            fullHouse.add(pair.get(1));
            return fullHouse;
        } else {
            return null;
        }
    }

    private LinkedList<Carte> straight(LinkedList<Carte> cartes) {
        LinkedList<Carte> candidats = new LinkedList<>(cartes);
        candidats.sort(Collections.reverseOrder());
        int i = 0;
        int enlevees = 0;
        while (i + 1 < candidats.size()) {
            if (candidats.get(i).valeur - 1 != candidats.get(i + 1).valeur) {
                if (candidats.get(i).valeur == candidats.get(i + 1).valeur) {
                    candidats.remove(i + 1);
                    enlevees++;
                } else {
                    int j = i;
                    while (j >= 0) {
                        candidats.remove(j);
                        j--;
                        enlevees++;
                    }
                }
                i = 0;
            } else {
                i++;
            }
            //Straight n'est pas possible.
            if ((cartes.size() == 5 && enlevees >= 1) || (cartes.size() == 6 && enlevees >= 2) || (cartes.size() == 7 && enlevees >= 3)) {
                break;
            }
        }
        //Si une suite n'a pas été trouvé, on cherche si on peut avoir une commençant par As, et on change la valeur de l'As de 14 à 1.
        if (candidats.size() < 5) {
            //Une suite commençant par As n'est possible que s'il y a un As et un Deux dans les cartes
            LinkedList<Carte> verification = new LinkedList<>(cartes);
            verification.sort(Collections.reverseOrder());
            if (verification.getFirst() != null && verification.getFirst().valeur == 14 && verification.getLast().valeur == 2) {
                candidats = new LinkedList<>(cartes);
                candidats.forEach(carte -> {
                    if (carte.valeur == 14) {
                        carte.valeur = 1;
                    }
                });
                i = 0;
                enlevees = 0;
                candidats.sort(Collections.reverseOrder());
                while (i + 1 < candidats.size()) {
                    if (candidats.get(i).valeur - 1 != candidats.get(i + 1).valeur) {
                        if (candidats.get(i).valeur == candidats.get(i + 1).valeur) {
                            candidats.remove(i + 1);
                            enlevees++;
                        } else {
                            int j = i;
                            while (j >= 0) {
                                candidats.remove(j);
                                j--;
                                enlevees++;
                            }
                        }
                    } else {
                        i++;
                    }
                    //Straight n'est pas possible.
                    if ((cartes.size() == 5 && enlevees >= 1) || (cartes.size() == 6 && enlevees >= 2) || (cartes.size() == 7 && enlevees >= 3)) {
                        break;
                    }
                }
                cartes.forEach(carte -> {
                    if (carte.valeur == 1) {
                        carte.valeur = 14;
                    }
                });
            }
        }
        if (candidats.size() < 5) {
            return null;
        } else {
            while (candidats.size() > 5) {
                candidats.removeLast();
            }
            return candidats;
        }

    }

    /*
    Return une liste avec les cartes triées en ordre decrescente de valeur
     */
    private LinkedList<Carte> straightFlush(LinkedList<Carte> cartes) {
        // verif de flush()!=null et staight()!=null pas necessaire, car deja ne sera appellee si les deux sont dif de null
        /* Idée de la méthode: verifier si la liste flush() contient un Straight
         */
        if (flush(cartes) != null) {
            return straight(flush(cartes));
        } else {
            return null;
        }
    }



    public int compareTo(Object h2) {
        int comparaison = 0;
        if (valeurHandFinale > ((Hand) h2).valeurHandFinale) {
            comparaison = 1;
        } else if (valeurHandFinale < ((Hand) h2).valeurHandFinale) {
            comparaison = -1;
        }
        return comparaison;
    }

    /*
    Calculer Hand Moment n'est utilisé que pour l'intelligence-> prise de decisions,
     */
    public void calculerValeurHandMoment(LinkedList<Carte> cartesDeLaHandMoment) {
        LinkedList<Carte> result = new LinkedList<>();
        if (cartesDeLaHandMoment.size() == 2) {
            if (pairs(cartesDeLaHandMoment) != null) {
                result.addAll(pairs(cartesDeLaHandMoment));
                valeurHandMoment = 10 * result.getFirst().valeur;
            } else {
                valeurHandMoment = highCard(surMain).valeur;
                result.add(highCard(surMain));
            }
        } else {  //Calcul de la valeur de la hand après flop, turn et river.
            if (straightFlush(cartesDeLaHandMoment) != null) {
                result.addAll(straightFlush(cartesDeLaHandMoment));
                if (result.getFirst().valeur == 14) {
                    valeurHandMoment = 500000;
                } else {
                    valeurHandMoment = 400000 + result.getFirst().valeur;
                }
            } else if (fourOfAKind(cartesDeLaHandMoment) != null) {
                result.addAll(fourOfAKind(cartesDeLaHandMoment));
                valeurHandMoment = 300000 + result.getFirst().valeur;
            } else if (fullHouse(cartesDeLaHandMoment) != null) {
                result.addAll(fullHouse(cartesDeLaHandMoment));
                valeurHandMoment = 200000 + 10 * result.getFirst().valeur + result.getLast().valeur;
            } else if (flush(cartesDeLaHandMoment) != null) {
                result.addAll(flush(cartesDeLaHandMoment));
                while (result.size() > 5) {
                    result.removeLast();
                }
                valeurHandMoment = 10000 * result.getFirst().valeur + 1000 * result.get(1).valeur + 100 * result.get(2).valeur + 10 * result.get(3).valeur + result.get(4).valeur;
            } else if (straight(cartesDeLaHandMoment) != null) {
                result.addAll(straight(cartesDeLaHandMoment));
                Collections.sort(result);
                valeurHandMoment = 15000 + result.getLast().valeur;
            } else if (threeOfAKind(cartesDeLaHandMoment) != null) {
                result.addAll(threeOfAKind(cartesDeLaHandMoment));
                valeurHandMoment = 1000 * result.getFirst().valeur;
            } else if (pairs(cartesDeLaHandMoment) != null) {
                result.addAll(pairs(cartesDeLaHandMoment));
                if (result.size() == 2) {
                    valeurHandMoment = 10 * result.getFirst().valeur;
                } else if (result.size() == 4) {
                    valeurHandMoment = 100 * result.getFirst().valeur + result.getLast().valeur;
                }
            } else {
                result.add(highCard(cartesDeLaHandMoment));
                valeurHandMoment = result.getFirst().valeur;
            }
        }
        handMoment.clear();
        handMoment.addAll(result);
    }

    public void calculerValeurHandFinale(LinkedList<Carte> cartesFin){
        LinkedList<Carte> result = new LinkedList<>();

        if (straightFlush(cartesFin) != null) {
            result.addAll(straightFlush(cartesFin));
            if (result.getFirst().valeur == 14) {
                description = "Royal straight flush";
                valeurHandFinale = 500000;
            } else {
                description = "Straight flush, carte haute " + result.getFirst().description(false);
                valeurHandFinale = 400000 + result.getFirst().valeur;
            }
        } else if (fourOfAKind(cartesFin) != null) {
            result.addAll(fourOfAKind(cartesFin));
            valeurHandFinale = 300000 + result.getFirst().valeur;
            description = "Carre de " + result.getFirst().description(true);
        } else if (fullHouse(cartesFin) != null) {
            result.addAll(fullHouse(cartesFin));
            valeurHandFinale = 200000 + 10 * result.getFirst().valeur + result.getLast().valeur;
            description = "Full House, brelan de " + result.getFirst().description(true) + " et pair de " + result.getLast().description(true);
        } else if (flush(cartesFin) != null) {
            result.addAll(flush(cartesFin));
            while (result.size() > 5) {
                result.removeLast();
            }
            valeurHandFinale = 10000 * result.getFirst().valeur + 1000 * result.get(1).valeur + 100 * result.get(2).valeur + 10 * result.get(3).valeur + result.get(4).valeur;
            description = "Flush, carte haute " + result.getFirst().description(false);
        } else if (straight(cartesFin) != null) {
            result.addAll(straight(cartesFin));
            Collections.sort(result);
            valeurHandFinale = 15000 + result.getLast().valeur;
            description = "Suite, carte haute " + result.getLast().description(false);
        } else if (threeOfAKind(cartesFin) != null) {
            result.addAll(threeOfAKind(cartesFin));
            valeurHandFinale = 1000 * result.getFirst().valeur;
            description = "Brelan de " + result.getFirst().description(true);
        } else if (pairs(cartesFin) != null) {
            result.addAll(pairs(cartesFin));
            if (result.size() == 2) {
                valeurHandFinale = 10 * result.getFirst().valeur;
                description = "Pair de " + result.getFirst().description(true);
            } else if (result.size() == 4) {
                valeurHandFinale = 100 * result.getFirst().valeur + result.getLast().valeur;
                description = "Deux pairs, " + result.getFirst().description(true) + " et " + result.getLast().description(true);
            }
        } else {
            result.add(highCard(cartesFin));
            valeurHandFinale = result.getFirst().valeur;
            description = "Carte haute, " + result.getFirst().description(false);
        }

    }

    public Range calculerRangePreFlop() {
        surMain.sort(Collections.reverseOrder());
        rangePreFlop = new Range(surMain.getFirst(), surMain.getLast());
        return rangePreFlop;
    }

    public Range getRangePreFlop() {
        return rangePreFlop;
    }
}




