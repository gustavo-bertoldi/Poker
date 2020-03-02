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
    Verifica a quantidade de pares em uma hand e os retorna em uma lista com seus respectivos valores.
    O tamanho da lista é a quantidade de pares encontrados na hand.
     */
    public LinkedList<Carta> pairs(){
        int q=0; //Quantité de valeurs égaux trouvées
        LinkedList<Carta> valeursTrouvees = new LinkedList<>();

        for(int i=2;i<=14;i++){ // i répresente chaque valuer possible des cartes
            LinkedList<Carta> candidats = new LinkedList<>();
            for(Carta c : cartas){ //Pour chaque carte dans hand on compare sa valeur avec chaque valeur possible, si égaux, on aout cette carte à la liste candidats
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
        int q=0; //quantidade de valores iguais em cartas para cada valor i
        LinkedList<Carta> valeursTrouvees = new LinkedList<Carta>();

        for(int i=2;i<=14;i++){ //i representa cada valor possivel de carta
            LinkedList<Carta> candidats = new LinkedList<>();
            for(Carta c : cartas){ //para cada carta na lista cartas, compara-se seu valor com i, se for igual q aumenta 1
                if(c.valor==i){
                    q++;
                    candidats.add(c);
                }
            }
            if(q==3){ //Se q=3 significa que temos uma trinca, o valor da trinca é adicionado a lista
                valeursTrouvees.addAll(candidats);
                candidats=new LinkedList<Carta>();
            }
            q=0; //fim da boucle, a quantidade de cartas iguais e reiniciada para um novo valor de i
        }
        if(valeursTrouvees.size()>0){
            Collections.sort(valeursTrouvees,Collections.reverseOrder());
            return valeursTrouvees;
        }
        else{return null;}
    }

    public LinkedList<Carta> fourOfAKind(){
        int q=0; //quantidade de valores iguais em cartas para cada valor i
        LinkedList<Carta> valeursTrouvees = new LinkedList<>();

        for(int i=2;i<=14;i++){ //i representa cada valor possivel de carta
            LinkedList<Carta> candidats = new LinkedList<>();
            for(Carta c : cartas){ //para cada carta na lista cartas, compara-se seu valor com i, se for igual q aumenta 1
                if(c.valor==i){
                    q++;
                    candidats.add(c);
                }
            }
            /* Se q=4 significa que temos uma quadra,
             o valor e atribuido a valorEncontrado e como so existe uma quadra por hand, a boucle e parada
             */
            if(q==4){
                valeursTrouvees.addAll(candidats);
                break;
            }
            q=0; //fim da boucle, a quantidade de cartas iguais e reiniciada para um novo valor de i
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
    public LinkedList<Integer> straightFlush() {
        LinkedList<Integer> straightFlush = new LinkedList<Integer>();
        if (straight() != null && flush() != null){
            LinkedList<Carta> cartasStraight=straightListaCartas();
            for(int i=1;i<cartasStraight.size();i++){
                if(cartasStraight.get(i).naipe==cartasStraight.get(i-1).naipe){
                    straightFlush.add(cartasStraight.get(i).valor);
                }
            }
        }
        if(straightFlush.size()>=5){
            Collections.sort(straightFlush , Collections.reverseOrder());
            return straightFlush;
        }
        else{
            return null;
        }
    }
    // si straight
    public LinkedList<Carta> straight(){
        // 5 das sete cartas sejam em sequencia // caso a parte pro As 2 3 4 5
        LinkedList<Carta> straight = new LinkedList<Carta>();
        // int compteurDEchec = 0; peut nous couter plus qu'un simple if apres la boucle
        straight.add(cartas.get(0));
        for(int i =1; i<cartas.size() ;i++) {
            if(straight.size()<5) {
                if (cartas.get(i - 1).valor == (cartas.get(i).valor + 1)) {
                    straight.add(cartas.get(i));
                } else if(cartas.get(i - 1).valor == (cartas.get(i).valor )) {
                //    ne rien faire
                }else {
                    straight.removeAll(straight);
                    straight.add(cartas.get(i));
                    //compteurDEchec++;
                }
            }

        }

        if(straight.size() <5) {
            straight = null;
        }
           /* if(compteurDEchec>=3) { REMPLACE PAR LE IF D'AU DESSUS
                straight = null;
                break;
            } */
        return straight;
    }
    public boolean isRoyalStraightPossible(){
        boolean possibilite = false;
        Carta ace = new Carta(14, 'p');
        Carta roi = new Carta(13, 'p');
        Carta dame = new Carta(12, 'p');
        if(cartas.get(0).compareTo(ace)==0){

        }
        return possibilite;

    }
        // sort hand
        //parcourir hand depuis la premiere carte
        //stocker les valeurs dans une liste si valeur(i+1) = valeur(i) +1 (TYPE?)
        //sinon, enlever la valeur
        // prendre size.
        //si >= 5; true, sinon false

    public LinkedList<Carta> straightListaCartas(){
        return null;
    }

    /*
    RETORNA TRUE SE HOUVER UM ROYAL STRAIGHT FLUSH NA HAND E FALSE SE NAO HOUVER
     */
    private boolean royalStraightFlush(){
        if(straightFlush().get(0)==14){
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

