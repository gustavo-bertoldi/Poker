import javax.swing.*;
import java.util.Collections;
import java.util.LinkedList;


public class Hand  implements Comparable{

    protected LinkedList<Carte> cartes = new LinkedList<>();
    protected LinkedList<Carte> surMain= new LinkedList<>();
    protected LinkedList<Carte> surTable= new LinkedList<>();
    private String description = ""; //Description textuelle de chaque hand Ex: Pair de dames
    private int valeurHand = -1;
    /*
    Idée derrière valeurHand: *IDEIA FODA*
    - Chaque hand aura une valeur correspondant aux cartes presentes, on pourra donc utilise Comparable/compareTo() et classer les hands sur jeu
    - VALEURS:
    Hand                           Calcul                                                                               Range                      DONE
    HC                    valeurHand = high*10.000 + second*1.000 + third*100 + fourth*10 + fifth                  [200;1.400]                     OK
    Pair                  valeurHand = valeurPair*100.000 + high*100 + 10*second + third                           [2.034;15.431]                  OK
    TwoPairs              valeurHand = valeurPair1*1.000.000 + valeurPair2*10 + high                               [20.345;153.130]                OK
    ThreeOAK              valeurHand = valeurTOAK*10.000.000 + 10*high + second                                    [200.034;1.400.100]             OK
    Straight              valeurHand = 200.000.000 + high                                                          [5.000.005;5.000.014]           OK
    Flush                 valeurHand = 300.000.000 + high*10.000 + second*1.000+ 100*third + 10*forth + fifth      [10.075.432;10.154.301]         OK  *SUGESTAO*
    FullHouse             valeurHand = 400.000.000 + valeurTOAK*10 + valeurPair                                    [20.023.000;20.153.000]         OK
    FourOAK               valeurHand = 500.000.000 + valeurFOAK*10.000 + high                                      [30.024.300;30.154.200]         OK
    StraightFlush         valeurHand = 600.000.000 + high                                                          [40.000.005;40.000.013]         OK
    RoyalStraightFlush    valeurHand = 700.000.000                                                                 700.000.000                     OK
     */
    //Methods avec Straight ne considerent pas le cas ou le straight est sur la table,...

    public Hand(){
    }

    public LinkedList<Carte> getCartes(){return cartes;}

    public void setHand(LinkedList<Carte> cartes){
        this.cartes.addAll(cartes);
        Collections.sort(this.cartes,Collections.reverseOrder());
    }

    public String getDescription(){
        return description;
    }

    public void setSurMain(LinkedList<Carte> cartesDistrib){
        surMain.addAll(cartesDistrib);
        Collections.sort(surMain, Collections.reverseOrder());
    }
    public void setSurTable(LinkedList<Carte> misesSurTable){
        surTable.addAll(misesSurTable);
        Collections.sort(surMain, Collections.reverseOrder());
    }

    public int getValeurHand(){
        return valeurHand;
    }
    /*
    Verifie la quantite de pairs dans une hand et retourne une LL avec les cartes trouvées
     */
    public LinkedList<Carte> pairs(){
        int q=0; //Quantité de valeurs égales trouvées
        LinkedList<Carte> valeursTrouvees = new LinkedList<>();
        for(int i=2;i<=14;i++){ // i répresente chaque valuer possible des cartes
            LinkedList<Carte> candidats = new LinkedList<>();
            for(Carte c : cartes){ //Pour chaque carte dans hand on compare sa valeur avec chaque valeur possible, si égales, on aout cette carte à la liste candidats
                if(c.valeur==i){
                    q++;
                    candidats.add(c);
                }
            }
            if(q==2){ //Si q=2, on a trouvé un pair, les cartes correspondants sont ajoutées à la liste de valeurs trouvées
                valeursTrouvees.addAll(candidats);
                candidats=new LinkedList<Carte>();
            }
            q=0; //Fin de la boucle, le compteur de cartes égales est mis à 0
        }
        if(valeursTrouvees.size()>2) {  //Cas 2 pairs
            Collections.sort(valeursTrouvees, Collections.reverseOrder());
            while(valeursTrouvees.size()>4) { //si plus d'un pair
                valeursTrouvees.removeLast();
            }
            return valeursTrouvees;
        }else if(valeursTrouvees.size() == 2){ // si un pair
            return valeursTrouvees;
        }else{return null;}
    }

    public LinkedList<Carte> threeOfAKind(){
        int q=0; //Quantité de valeurs égales trouvées
        LinkedList<Carte> valeursTrouvees = new LinkedList<>();

        for(int i=2;i<=14;i++){ //i c'est chaque valeur possible des cartes
            LinkedList<Carte> candidats = new LinkedList<>(); //liste avec les candidats d'un trinome de cartes égales
            for(Carte c : cartes){ //pour chaque carte dans hand on compare sa valeur avec i, si égales, q augmente 1 et on ajoute cette carte a candidats
                if(c.valeur==i){
                    q++;
                    candidats.add(c);
                }
            }
            if(q==3){ //Si q=3, on a trouvé un trinome de cartes égales, on les ajoute a valeursTrouvees et on reinicialise candidats
                valeursTrouvees.addAll(candidats);
                candidats=new LinkedList<Carte>();
            }
            q=0; //fin de la boucle, la quantite de cartes égales est reinitialisee pour une nouvelle valeur de i.
        }
        if(valeursTrouvees.size()>0){ //S'il y a un trinome ou plus on les trie en ordre decroissante et les retourne
            Collections.sort(valeursTrouvees,Collections.reverseOrder());
            while(valeursTrouvees.size()>3){
                valeursTrouvees.removeLast();
            }
            return valeursTrouvees;
        }
        else{return null;}
    }

    public LinkedList<Carte> fourOfAKind(){
        int q=0; //Quantité de valeurs égales trouvées
        LinkedList<Carte> valeursTrouvees = new LinkedList<>();

        for(int i=2;i<=14;i++){ //i c'est chaque valeur possible des cartes
            LinkedList<Carte> candidats = new LinkedList<>(); //liste avec les candidats d'un trinome de cartes égales
            for(Carte c : cartes){ //para cada Carte na lista cartes, compara-se seu valeur com i, se for igual q aumenta 1
                if(c.valeur==i){
                    q++;
                    candidats.add(c);
                }
            }
            /*Si q=4 on a trouve un carre de cartes, on ajoute ces valeurs a la liste de valeurs trouvees
             la boucle est arretee vu qu'il n'existe q'un carre de cartes par hand.
             */
            if(q==4){
                valeursTrouvees.addAll(candidats);
                break;
            }
            q=0; //fin de la boucle, q est mis a 0
        }
        if(valeursTrouvees.size()>0){
            return valeursTrouvees;
        }
        else{return null;}
    }

    /*
    S'il y a un flush dans la hand, la methode retourne une LL avec les 5 cartes du flush, sinon elle retourne null
     */
    public LinkedList<Carte> flush(){
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
            return flush;
        }
        else{
            return null;
        }
    }

    /*
        Retourne la carte de valeur plus haute dans la hand
     */
    public Carte highCard(){
       return cartes.getFirst();
    }

    /*
    Verifie s'il y a un full house dans la hand et retourne une liste avec
    les cartes du full house, sinon retourne null
     */
    public LinkedList<Carte> fullHouse(){
        LinkedList<Carte> fullHouse = new LinkedList<Carte>();
        if(pairs()!=null && threeOfAKind()!=null && pairs().size()>=2 && threeOfAKind().size()>=1){ //possibilité d'avoir deux pairs et une TOAK
            LinkedList<Carte> pair = pairs();
            LinkedList<Carte> brelan = threeOfAKind();
            fullHouse.addAll(pair);
            fullHouse.addAll(brelan);
            valeurHand = 400000000 + brelan.getFirst().valeur + pair.getFirst().valeur;
            description = "Full House, trois "+pairs().getFirst().description(true)+", deux "+threeOfAKind().getFirst().description(true);
            return fullHouse;
        }
        else{
            return null;
        }
    }

    /*
    RETORNA UMA LISTA COM OS valeurES DO STRAIGHT FLUSH EM ORDEM DESCRESCENTE
     */
    public LinkedList<Carte> straightFlush() {
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
    // si straight
    public LinkedList<Carte> straight(){
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

        if(straight.size() <5) {
            straight = null; // pas besoin de rechanger la valeur parce qu'on utilise flush et pas cartes
        }
        return straight;
    }

    public boolean isRoyalStraightPossible(){
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
    RETORNA TRUE SE HOUVER UM ROYAL STRAIGHT FLUSH NA HAND E FALSE SE NAO HOUVER
     */
    public boolean royalStraightFlush(){
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

    public LinkedList<Carte> calculerValeurHand(){
        LinkedList<Carte> result = new LinkedList<Carte>();
        LinkedList<Carte> kickers = cartes;
        description="";
            if(royalStraightFlush()){
                result=flush();
                description = "Royal straight flush";
                valeurHand = 700000000;
            }
            else if(straightFlush()!=null){
                result=straightFlush();
                description = "Straight flush, carte haute "+result.getFirst().description(false);
                valeurHand = 600000000 + result.getFirst().valeur;

            }
            else if(fourOfAKind()!=null){
                result=fourOfAKind();
                kickers.removeAll(result);
                Collections.sort(kickers,Collections.reverseOrder());
                valeurHand = 500000000 + 10*result.getFirst().valeur + kickers.getFirst().valeur;
                description = "Carre de "+result.getFirst().description(false)+". Kicker : "+kickers.getFirst().description(false);
            }
            else if(fullHouse()!=null){
                result=fullHouse();
                /*
                La valeurHand et la description du full house sont calculées dans la methode fullHouse()
                 */
            }
            else if(flush()!=null){
                result=flush();
                valeurHand = 300000000 + 10000*result.getFirst().valeur + 1000*result.get(1).valeur + 100*result.get(2).valeur + 10*result.get(3).valeur + result.get(4).valeur;
                description = "Flush, carte haute "+result.getFirst().description(false);
            }
            else if(straight()!=null){
                result=straight();
                valeurHand = 200000000 + result.getFirst().valeur;
                description = "Suite, carte haute "+result.getFirst().description(false);
            }
            else if(threeOfAKind()!=null){
                result=threeOfAKind();
                kickers.removeAll(result);
                Collections.sort(kickers, Collections.reverseOrder());
                valeurHand=10000000 + 10*kickers.getFirst().valeur + kickers.get(1).valeur;
                description = "Brelan de "+result.getFirst().description(false)+". Kicker : "+kickers.getFirst().description(false);
            }
            else if(pairs()!=null){
                result=pairs();
                kickers.removeAll(result);
                Collections.sort(kickers, Collections.reverseOrder());
                if(result.size()==2){
                    valeurHand = result.getFirst().valeur*100000 + kickers.getFirst().valeur*100 + kickers.get(1).valeur*10 + kickers.get(2).valeur;
                    description = "Pair de "+result.getFirst().description(false)+". Kicker : "+kickers.getFirst().description(false);
                }
                else if(result.size()==4){
                    valeurHand = 1000000*result.getFirst().valeur + 10*result.getLast().valeur + kickers.getFirst().valeur;
                    description = "Deux pairs, "+result.getFirst().description(true)+" et "+result.getLast().description(true)+". Kicker : "+kickers.getFirst().description(false);
                }
            }
            else{
                result.add(highCard());
                kickers.removeAll(result);
                valeurHand = 10000*cartes.getFirst().valeur + 1000*cartes.get(1).valeur + 100*cartes.get(2).valeur + 10*cartes.get(3).valeur + cartes.get(4).valeur;
                description+="Carte haute "+highCard().valeur+". Kicker : "+kickers.getFirst().description(false);
            }
        return result;
    }

}


