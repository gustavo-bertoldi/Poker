import java.util.Collections;
import java.util.LinkedList;


public class Hand  implements Comparable{

    private LinkedList<Carte> cartes;
    private LinkedList<Carte> surMain;
    private LinkedList<Carte> surTable;
    private int valeurHand;
    /*
    Idée derrière valeurHand:
    - Chaque hand aura une valeur correspondant aux cartes presentes, on pourra donc utilise Comparable/compareTo() et classer les hands sur jeu
    - VALEURS:
    Hand                           Calcul                                                              Range
    HC                             valeurHand = valeur*10                                      [20;140]
    Pair                           valeurHand = (valeur)*100                                   [200;1400]
    TwoPairs                       valeurHand = Valeur1*1000 + valeur2*100 Valeur1>Valeur2     [3.200;15,300]
    ThreeOAK                       valeurHand = (valeur)*10.000                                [20.000;140.000]
    Straight                       valeurHand = (highCard)*100.000                             [500.000;1.400.000]
    Flush(sans straight)           valeurHand = (2*1.000.000) + highCard                       [2.000.006;2.000.014]
    FullHouse                      valeurHand = valeurTOAK*1.000.000 + valeurPair*100          [2.001.400;14.001.300]
    FourOAK                        valeurHand = 2*10.000.000 + valeur                          [20.000.002;20.000.014]
    StraightFlush                  valeurHand = 2*100.000.000 + highSurMain                           [200.000.005;200.000.014]
     */
    //FALTA AFETAR OS VALEURSHANDS DENTRO DOS METODOS

    public Hand(LinkedList<Carte> cartes){
        this.cartes=cartes;
        Collections.sort(cartes, Collections.reverseOrder());
    }

    public LinkedList<Carte> getCartes(){return cartes;}

    public void setHand(LinkedList<Carte> cartes){
        this.cartes=cartes;
    }

    public void addSurMain(LinkedList<Carte> cartesDistrib){
        surMain.addAll(cartesDistrib);
    }
    public void addSurTable(LinkedList<Carte> miseSurTable){
        surTable.addAll(miseSurTable);
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
        if(valeursTrouvees.size()>0) {
            Collections.sort(valeursTrouvees, Collections.reverseOrder());
            while(valeursTrouvees.size()>4) {
                valeursTrouvees.removeLast();
            }
            return valeursTrouvees;
        }
        else{return null;}
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
       LinkedList<Carte> triees = new LinkedList<Carte>();
       triees.addAll(cartes);
       Collections.sort(triees, Collections.reverseOrder());
       return triees.get(0);
    }

    /*
    Verifie s'il y a un full house dans la hand et retourne une liste avec
    les cartes du full house, sinon retourne null
     */
    public LinkedList<Carte> fullHouse(){
        LinkedList<Carte> fullHouse = new LinkedList<Carte>();
        if(pairs().size()==2 && threeOfAKind().size()>=1){
            LinkedList<Carte> pairs = pairs();
            fullHouse.addAll(pairs());
            fullHouse.addAll(threeOfAKind());
            return fullHouse;
        }
        else{
            return null;
        }
    }

    /*
    RETORNA UMA LISTA COM OS valeurES DO STRAIGHT FLUSH EM ORDEM DESCRESCENTE
     */
    public LinkedList<Carte> straightFlush(LinkedList<Carte> flush) {
        // verif de flush()!=null et staight()!=null pas necessaire, car deja ne sera appellee si les deux sont dif de null
        /* Idée de la méthode: verifier si la liste flush() contient un Straight
        */
        LinkedList<Carte> straightF = straight(flush);
        return straightF;
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
            if(cartes.get(7).valeur==1){ // si pas de straight 12345, rechanger la valeur du ace pour ne pas gener les autres methodes
                cartes.get(7).valeur = 14;
            }
        }

        return straight;
    }
    public LinkedList<Carte> straight(LinkedList<Carte> flush){
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
    private boolean royalStraightFlush(LinkedList<Carte> straightFlush){
        if(straightFlush.get(0).valeur ==14){
            return true;
        }
        else{
            return false;
        }
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


    public String toString(LinkedList<Carte> lista){
        String result="";
        if(lista==null){
            result= "Lista vazia";
        }
        else {
            for(int i=0;i<lista.size();i++){
                result=result+"valeur Carte: "+lista.get(i).valeur +"\n";
            }
        }
        return result;
    }

    public String toString(int t){
        return "valeur: "+t;
    }

}

