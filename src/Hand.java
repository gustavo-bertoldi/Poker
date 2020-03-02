//import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Hand {

    private LinkedList<Carta> cartas;


    public Hand(LinkedList<Carta> cartas){
        this.cartas=cartas;
        Collections.sort(cartas, Collections.reverseOrder());
    }

    public void setHand(LinkedList<Carta> cartas){
        this.cartas=cartas;
    }

    /*
    Verifie la quantite de pairs dans une hand et retourne une LL avec les cartes trouvées
     */
    public LinkedList<Carta> pairs(){
        int q=0; //Quantité de valeurs égales trouvées
        LinkedList<Carta> valeursTrouvees = new LinkedList<>();

        for(int i=2;i<=14;i++){ // i répresente chaque valuer possible des cartes
            LinkedList<Carta> candidats = new LinkedList<>();
            for(Carta c : cartas){ //Pour chaque carte dans hand on compare sa valeur avec chaque valeur possible, si égales, on aout cette carte à la liste candidats
                if(c.valor==i){
                    q++;
                    candidats.add(c);
                }
            }
            if(q==2){ //Si q=2, on a trouvé un pair, les cartes correspondants sont ajoutées à la liste de valeurs trouvées
                valeursTrouvees.addAll(candidats);
                candidats=new LinkedList<Carta>();
            }
            q=0; //Fin de la boucle, le compteur de cartes égales est mis à 0
        }
        if(valeursTrouvees.size()>0) {
            Collections.sort(valeursTrouvees, Collections.reverseOrder());
            return valeursTrouvees;
        }
        else{return null;}
    }

    public LinkedList<Carta> threeOfAKind(){
        int q=0; //Quantité de valeurs égales trouvées
        LinkedList<Carta> valeursTrouvees = new LinkedList<>();

        for(int i=2;i<=14;i++){ //i c'est chaque valeur possible des cartes
            LinkedList<Carta> candidats = new LinkedList<>(); //liste avec les candidats d'un trinome de cartes égales
            for(Carta c : cartas){ //pour chaque carte dans hand on compare sa valeur avec i, si égales, q augmente 1 et on ajoute cette carte a candidats
                if(c.valor==i){
                    q++;
                    candidats.add(c);
                }
            }
            if(q==3){ //Si q=3, on a trouvé un trinome de cartes égales, on les ajoute a valeursTrouvees et on reinicialise candidats
                valeursTrouvees.addAll(candidats);
                candidats=new LinkedList<Carta>();
            }
            q=0; //fin de la boucle, la quantite de cartes égales est reinitialisee pour une nouvelle valeur de i.
        }
        if(valeursTrouvees.size()>0){ //S'il y a un trinome ou plus on les trie en ordre decroissante et les retourne
            Collections.sort(valeursTrouvees,Collections.reverseOrder());
            return valeursTrouvees;
        }
        else{return null;}
    }

    public LinkedList<Carta> fourOfAKind(){
        int q=0; //Quantité de valeurs égales trouvées
        LinkedList<Carta> valeursTrouvees = new LinkedList<>();

        for(int i=2;i<=14;i++){ //i c'est chaque valeur possible des cartes
            LinkedList<Carta> candidats = new LinkedList<>(); //liste avec les candidats d'un trinome de cartes égales
            for(Carta c : cartas){ //para cada carta na lista cartas, compara-se seu valor com i, se for igual q aumenta 1
                if(c.valor==i){
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
    SE HOUVER UM FLUSH NA HAND RETORNA UMA LISTA COM OS VALORES DO FLUSH, CASO CONTRARIO RETORNA NULL.
     */
    public LinkedList<Carta> flush(){
        int qp=0; //QUANTIDADE DE CARTAS DE PAUS NA HAND
        int qc=0; //QUANTIDADE DE CARTAS DE COPAS NA HAND
        int qe=0; //QUANTIDADE DE CARTAS DE ESPADAS NA HAND
        int qo=0; //QUANTIDADE DE CARTAS DE OUROS NA HAND
        LinkedList<Carta> flush=new LinkedList<>();

        /*
        A boucle conta a quantidade de cartas de cada naipe presentes na hand
         */
        for(Carta c:cartas){
            if(c.naipe=='p'){
                qp++;
            }
            else if(c.naipe=='c'){
                qc++;
            }
            else if(c.naipe=='e'){
                qe++;
            }
            else if(c.naipe=='o'){
                qo++;
            }
        }
        /*
        Verifica se para cada um dos naipes contou-se mais de 5 cartas, nesse caso, adiciona as cartas do devido naipe
        na lista flush
         */
        if(qp>=5){
            for(Carta c:cartas){
                if(c.naipe=='p'){
                    flush.add(c);
                }
            }
        }
        else if(qc>=5){
            for(Carta c:cartas){
                if(c.naipe=='c'){
                    flush.add(c);
                }
            }
        }
        else if(qe>=5){
            for(Carta c:cartas){
                if(c.naipe=='e'){
                    flush.add(c);
                }
            }
        }
        else if(qo>=5){
            for(Carta c:cartas){
                if(c.naipe=='o'){
                    flush.add(c);
                }
            }
        }
        if(flush.size()>=5) {
            Collections.sort(flush, Collections.reverseOrder()); //ORDENA A LISTA DE VALORES EM ORDEM DECRESCENTE
            return flush;
        }
        else{
            return null;
        }
    }

    /*
    RETORNA UM INT COM O VALOR DA CARTA MAIS ALTA
     */
    public Carta highCard(){
       LinkedList<Carta> triees = new LinkedList<Carta>();
       triees.addAll(cartas);
       Collections.sort(triees, Collections.reverseOrder());
       return triees.get(0);
    }

    public LinkedList<Carta> fullHouse(){
        LinkedList<Carta> fullHouse = new LinkedList<Carta>();
        if(pairs().size()>=2 && threeOfAKind().size()>=1){
            LinkedList<Carta> pairs = pairs();
            Collections.sort(pairs,Collections.reverseOrder()); //ORDENA OS VALORES DOS PARES EM ORDEM DECRESCENTE
            fullHouse.add(pairs.get(0));
            fullHouse.add(pairs.get(1));// ADICIONA O VALOR DO PAR SEGUIDO DO VALOR DA TRINCA NA LISTA A RETORNAR
            fullHouse.addAll(threeOfAKind());
            return fullHouse;
        }
        else{
            return null;
        }
    }

    /*
    RETORNA UMA LISTA COM OS VALORES DO STRAIGHT FLUSH EM ORDEM DESCRESCENTE
     */
    public LinkedList<Carta> straightFlush(LinkedList<Carta> flush) {
        // verif de flush()!=null et staight()!=null pas necessaire, car deja ne sera appellee si les deux sont dif de null
        /* Idée de la méthode: verifier si la liste flush() contient un Straight
        */
        LinkedList<Carta> straightF = straight(flush);
        return straightF;
    }
    // si straight
    public LinkedList<Carta> straight(){
        // 5 das sete cartas sejam em sequencia // caso a parte pro As 2 3 4 5
        LinkedList<Carta> straight = new LinkedList<Carta>();
        if(!isRoyalStraightPossible() && cartas.get(0).valor==14){
            cartas.get(0).valor=1;
            if(cartas.get(1).valor==13) {
                cartas.get(1).valor = 1;
            }
            Collections.sort(cartas, Collections.reverseOrder());
            // si un troisieme ace dans la hand, Royal Straight false.
        }
        straight.add(cartas.get(0));
        for(int i =1; i<cartas.size() ;i++) {
            if(straight.size()<5) {
                if (cartas.get(i - 1).valor == (cartas.get(i).valor + 1)) {
                    straight.add(cartas.get(i));
                } else if(cartas.get(i - 1).valor == (cartas.get(i).valor )) {
                // regarder si les couleurs sont égales à celles déjà ajoutées
                    if(straight.get(0).naipe == cartas.get(i).naipe){
                        straight.add(cartas.get(i));
                    }
                }else {
                    straight.removeAll(straight);
                    straight.add(cartas.get(i));
                }
            }

        }

        if(straight.size() <5) {
            straight = null;
            if(cartas.get(7).valor==1){ // si pas de straight 12345, rechanger la valeur du ace pour ne pas gener les autres methodes
                cartas.get(7).valor = 14;
            }
        }

        return straight;
    }
    public LinkedList<Carta> straight(LinkedList<Carta> flush){
        // meme methode mais en utilisant flush a la place de cartas, appelle juste dans straightFlush()
        LinkedList<Carta> straight = new LinkedList<Carta>();
        if(!isRoyalStraightPossible() && flush.get(0).valor==14){
            flush.get(0).valor=1;
            if(flush.get(1).valor==13) {
                flush.get(1).valor = 1;
            }
            Collections.sort(flush, Collections.reverseOrder());
            // si un troisieme ace dans la hand, Royal Straight false.
        }
        straight.add(flush.get(0));
        for(int i =1; i<flush.size() ;i++) {
            if(straight.size()<5) {
                if (flush.get(i - 1).valor == (flush.get(i).valor + 1)) {
                    straight.add(flush.get(i));
                } else if(flush.get(i - 1).valor == (flush.get(i).valor )) {
                    // regarder si les couleurs sont égales à celles déjà ajoutées
                    if(straight.get(0).naipe == flush.get(i).naipe){
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
        Carta ace = new Carta(14, 'p');
        Carta roi = new Carta(13, 'p');
        Carta dame = new Carta(12, 'p');
        //Vérifier si les trois premieres cartes sont Ace, Roi et dame
        if(cartas.get(0).compareTo(ace)==0){
            if(cartas.get(1).compareTo(ace)==0 ||cartas.get(1).compareTo(roi)==0 ) {
                if(cartas.get(2).compareTo(roi)==0||cartas.get(2).compareTo(dame)==0){
                    possibilite = true;
                }
            }

        }
        return possibilite;

    }


    public LinkedList<Carta> straightListaCartas(){
        return null;
    }

    /*
    RETORNA TRUE SE HOUVER UM ROYAL STRAIGHT FLUSH NA HAND E FALSE SE NAO HOUVER
     */
    private boolean royalStraightFlush(LinkedList<Carta> straightFlush){
        if(straightFlush.get(0).valor ==14){
            return true;
        }
        else{
            return false;
        }
    }

    public String toString(LinkedList<Carta> lista){
        String result="";
        if(lista==null){
            result= "Lista vazia";
        }
        else {
            for(int i=0;i<lista.size();i++){
                result=result+"Valor carta: "+lista.get(i).valor +"\n";
            }
        }
        return result;
    }

    public String toString(int t){
        return "valor: "+t;
    }

    public LinkedList<Integer> removerRepetidas(){
        LinkedList<Integer> semRepetidas=new LinkedList<Integer>();
        for(Carta c:cartas){
            semRepetidas.add(c.valor);
        }
        Set<Integer> hashSet = new HashSet<Integer>(semRepetidas);
        semRepetidas.clear();
        semRepetidas.addAll(hashSet);
        Collections.sort(semRepetidas,Collections.reverseOrder());
        return semRepetidas;
    }

}

