import java.util.Collections;
import java.util.LinkedList;


public class Hand implements Comparable {

    private LinkedList<Carte> cartesSurMain= new LinkedList<>();
    private LinkedList<Carte> cartesApresFlop = new LinkedList<>();
    private LinkedList<Carte> cartesApresTurn = new LinkedList<>();
    private LinkedList<Carte> toutesCartes = new LinkedList<>();
    private LinkedList<Carte> hand;
    private String description = ""; //Description textuelle de chaque hand Ex: Pair de dames

    private int valeurHand = -1;
    private int valeurHandSurMain = -1;
    private int valeurHandApresFlop = -1;
    private int valeurHandApresTurn = -1;

    public Hand(){}

    public void setHand(LinkedList<Carte> cartesSurMain, LinkedList<Carte> cartesSurTable){
        this.cartesSurMain.addAll(cartesSurMain);

        this.cartesApresFlop.addAll(cartesSurMain);
        this.cartesApresFlop.add(cartesSurTable.get(0));
        this.cartesApresFlop.add(cartesSurTable.get(1));
        this.cartesApresFlop.add(cartesSurTable.get(2));

        this.cartesApresTurn.addAll(cartesApresFlop);
        this.cartesApresTurn.add(cartesSurTable.get(3));

        this.toutesCartes.addAll(cartesSurMain);
        this.toutesCartes.addAll(cartesSurTable);

        calculerValeurHand();


    }

    public int getValeurHand(){
        assert valeurHand != -1 : "Valeur hand n'a pas pu être calculée";
        return valeurHand;
    }

    public int getValeurHandSurMain(){
        assert valeurHandSurMain != -1 : "Valeur hand sur main n'a pas pu être calculée";
        return valeurHandSurMain;
    }

    public int getValeurHandApresFlop(){
        assert valeurHandApresFlop != -1 : "Valeur hand après flop n'a pas pu être calculée";
        return valeurHandApresFlop;
    }

    public int getValeurHandApresTurn(){
        assert valeurHandApresTurn != -1 : "Valeur hand après turn n'a pas pu être calculée";
        return valeurHandApresTurn;
    }

    public LinkedList<Carte> getToutesCartes(){
        return toutesCartes;
    }

    public void ajouterDescription(String descriptionAAjouter){
        description = description + descriptionAAjouter;
    }

    public LinkedList<Carte> getCartesHand(){
        return hand;
    }

    public String getDescription(){
        return description;
    }

    public void ajouterKicker(Carte kicker){
        assert kicker!=null : "Kicker null founi";
        hand.add(kicker);
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

    private LinkedList<Carte> pairs(){
        LinkedList<Carte> pairs = new LinkedList<>();
        for (int i=2;i<=14;i++){
            int q = 0;
            LinkedList<Carte> cartesTrouvees = new LinkedList<>();
            for(Carte c : toutesCartes){
                if(c.valeur==i){
                    q++;
                    cartesTrouvees.add(c);
                }
            }
            if (q==2){
                pairs.addAll(cartesTrouvees);
            }
        }
        pairs.sort(Collections.reverseOrder());
        if(pairs.size()>=2) {
            while (pairs.size() > 4) {
                pairs.removeLast();
            }
            return pairs;
        }
        else{return null;}
    }

    private LinkedList<Carte> pairSurMain(){
        if(cartesSurMain.get(0).valeur == cartesSurMain.get(1).valeur){
            return cartesSurMain;
        }
        else{
            return null;
        }
    }

    private LinkedList<Carte> threeOfAKind(){
        LinkedList<Carte> threeOfAKind = new LinkedList<>();
        for (int i=2;i<=14;i++){
            int q=0;
            LinkedList<Carte> cartesTrouvees = new LinkedList<>();
            for(Carte c : toutesCartes){
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

    private LinkedList<Carte> fourOfAKind(){
        LinkedList<Carte> fourOfAKind = new LinkedList<>();
        for (int i=2;i<=14;i++){
            int q=0;
            LinkedList<Carte> cartesTrouvees = new LinkedList<>();
            for(Carte c : toutesCartes){
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

    private LinkedList<Carte> flush(){
        int qt=0; //Nombre de cartes de trèfles.
        int qc=0; //Nombre de cartes de cœurs.
        int qp=0; //Nombre de cartes de piques.
        int qk=0; //Nombre de cartes de carreaux.
        LinkedList<Carte> flush=new LinkedList<>();
        /*
        La boucle compte la quantité de cartes de chaque couleur dans la hand.
         */
        for(Carte c:toutesCartes){
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
        dans ce cas, ajoute les cartes correspondantes à la liste flush.
         */
        if(qt>=5){
            for(Carte c:toutesCartes){
                if(c.couleur=='t'){
                    flush.add(c);
                }
            }
        }
        else if(qc>=5){
            for(Carte c:toutesCartes){
                if(c.couleur=='c'){
                    flush.add(c);
                }
            }
        }
        else if(qp>=5){
            for(Carte c:toutesCartes){
                if(c.couleur=='p'){
                    flush.add(c);
                }
            }
        }
        else if(qk>=5){
            for(Carte c:toutesCartes){
                if(c.couleur=='d'){
                    flush.add(c);
                }
            }
        }
        if(flush.size()>=5) {
            /*
            S'il y a 5 ou plus cartes de la même couleur, on renvoie une liste avec les 5 cartes de même
            couleur plus hautes, triées par valeurs décroissantes.
             */
            flush.sort(Collections.reverseOrder());
            while (flush.size()>5){
                flush.removeLast();
            }
            return flush;
        }
        else{
            return null;
        }
    }

    private LinkedList<Carte> fullHouse(){
        LinkedList<Carte> fullHouse = new LinkedList<Carte>();
        if(pairs()!=null && threeOfAKind()!=null && pairs().size()>=2 && threeOfAKind().size()==3){
            LinkedList<Carte> pair = pairs();
            LinkedList<Carte> brelan = threeOfAKind();
            fullHouse.addAll(brelan);
            fullHouse.add(pair.get(0));
            fullHouse.add(pair.get(1));
            return fullHouse;
        }
        else{
            return null;
        }
    }

    private LinkedList<Carte> straight(){
        // 5 das sete cartes sejam em sequencia // caso a parte pro As 2 3 4 5
        LinkedList<Carte> straight = new LinkedList<Carte>();
        if(!isRoyalStraightPossible() && toutesCartes.get(0).valeur==14){
            toutesCartes.get(0).valeur=1;
            if(toutesCartes.get(1).valeur==13) {
                toutesCartes.get(1).valeur = 1;
            }
            Collections.sort(toutesCartes, Collections.reverseOrder());
            // si un troisieme ace dans la hand, Royal Straight false.
        }
        straight.add(toutesCartes.get(0));
        for(int i =1; i<toutesCartes.size() ;i++) {
            if(straight.size()<5) {
                if (toutesCartes.get(i - 1).valeur == (toutesCartes.get(i).valeur + 1)) {
                    straight.add(toutesCartes.get(i));
                } else if(toutesCartes.get(i - 1).valeur == (toutesCartes.get(i).valeur )) {
                    // regarder si les couleurs sont égales à celles déjà ajoutées
                    if(straight.get(0).couleur == toutesCartes.get(i).couleur){
                        straight.add(toutesCartes.get(i));
                    }
                }else {
                    straight.removeAll(straight);
                    straight.add(toutesCartes.get(i));
                }
            }

        }

        if(straight.size() <5) {
            straight = null;
            Collections.sort(toutesCartes,Collections.reverseOrder());
            if(toutesCartes.getLast().valeur==1){ // si pas de straight 12345, rechanger la valeur du ace pour ne pas gener les autres methodes
                toutesCartes.getLast().valeur = 14;
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
        if(toutesCartes.get(0).compareTo(ace)==0){
            if(toutesCartes.get(1).compareTo(ace)==0 ||toutesCartes.get(1).compareTo(roi)==0 ) {
                if(toutesCartes.get(2).compareTo(roi)==0||toutesCartes.get(2).compareTo(dame)==0){
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

    private boolean royalStraightFlush(){
        return straightFlush()!=null && straightFlush().get(0).valeur == 14;
    }

    private Carte highCard(){
        Collections.sort(toutesCartes, Collections.reverseOrder());
        return toutesCartes.getFirst();
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
        hand = new LinkedList<>(result);
    }

    public void calculerValeurHandSurMain(){
        LinkedList<Carte> result;
        if(pairSurMain() != null){
            result = new LinkedList<>(pairSurMain());
            valeurHandSurMain = 10*result.getFirst().valeur;
        }
        else {
            cartesSurMain.sort(Collections.reverseOrder());
            result = new LinkedList<>();
            result.add(cartesSurMain.getFirst());
            valeurHandSurMain = result.getFirst().valeur;
        }
    }

    public static void main(String[] args){
        Hand h = new Hand();
        LinkedList<Carte> surMain = new LinkedList<>();
        LinkedList<Carte> surTable = new LinkedList<>();
        surMain.add(new Carte(10,'p'));
        surMain.add(new Carte(10,'t'));
        surTable.add(new Carte(11,'p'));
        surTable.add(new Carte(12,'p'));
        surTable.add(new Carte(11,'p'));
        surTable.add(new Carte(14,'p'));
        surTable.add(new Carte(10,'p'));

        h.setHand(surMain,surTable);

        System.out.println(h.straight());

    }
}
