import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Hand {

    private LinkedList<Carta> cartas;


    public Hand(LinkedList<Carta> cartas){
        this.cartas=cartas;
    }

    public void setHand(LinkedList<Carta> cartas){
        this.cartas=cartas;
    }

    /*
    Verifica a quantidade de pares em uma hand e os retorna em uma lista com seus respectivos valores.
    O tamanho da lista é a quantidade de pares encontrados na hand.
     */
    public LinkedList<Integer> pares(){
        int q=0; //quantidade de valores iguais em cartas para cada valor i
        LinkedList<Integer> valoresEncontrados = new LinkedList<Integer>();

        for(int i=2;i<=14;i++){ //i representa cada valor possivel de carta
            for(Carta c : cartas){ //para cada carta na lista cartas, compara-se seu valor com i, se for igual q aumenta 1
                if(c.valor==i){
                    q++;
                }
            }
            if(q==2){ //Se q=2 significa que temos um par, o valor do par é adicionado a lista
                valoresEncontrados.add(i);
            }
            q=0; //fim da boucle, a quantidade de cartas iguais e reiniciada para um novo valor de i
        }
        return valoresEncontrados;

    }

    public int trinca(){
        int q=0; //quantidade de valores iguais em cartas para cada valor i
        LinkedList<Integer> valoresEncontrados = new LinkedList<Integer>();

        for(int i=2;i<=14;i++){ //i representa cada valor possivel de carta
            for(Carta c : cartas){ //para cada carta na lista cartas, compara-se seu valor com i, se for igual q aumenta 1
                if(c.valor==i){
                    q++;
                }
            }
            if(q==3){ //Se q=3 significa que temos uma trinca, o valor da trinca é adicionado a lista
                valoresEncontrados.add(i);
            }
            q=0; //fim da boucle, a quantidade de cartas iguais e reiniciada para um novo valor de i
        }
        Collections.sort(valoresEncontrados, Collections.reverseOrder()); //Coloca os valores das trincas encontrados em ordem decrescente
        if(valoresEncontrados.size()==0){
            return 0; //se nao houver trinca na hand retorna 0;
        }
        else {
            return valoresEncontrados.get(0); //Retorna o primeiro valor da lista valoreEncontrados, ou seja, a maior trinca na hand.
        }

    }

    public int quadra(){
        int q=0; //quantidade de valores iguais em cartas para cada valor i
        int valorEncontrado=0;

        for(int i=2;i<=14;i++){ //i representa cada valor possivel de carta
            for(Carta c : cartas){ //para cada carta na lista cartas, compara-se seu valor com i, se for igual q aumenta 1
                if(c.valor==i){
                    q++;
                }
            }
            /* Se q=4 significa que temos uma quadra,
             o valor e atribuido a valorEncontrado e como so existe uma quadra por hand, a boucle e parada
             */
            if(q==4){
                valorEncontrado=i;
                break;
            }
            q=0; //fim da boucle, a quantidade de cartas iguais e reiniciada para um novo valor de i
        }

        return valorEncontrado; //Retorna o valor da quadra encontrada, SENAO RETORNA 0

    }

    /*
    SE HOUVER UM FLUSH NA HAND RETORNA UMA LISTA COM OS VALORES DO FLUSH, CASO CONTRARIO RETORNA NULL.
     */
    public LinkedList<Integer> flush(){
        int qp=0; //QUANTIDADE DE CARTAS DE PAUS NA HAND
        int qc=0; //QUANTIDADE DE CARTAS DE COPAS NA HAND
        int qe=0; //QUANTIDADE DE CARTAS DE ESPADAS NA HAND
        int qo=0; //QUANTIDADE DE CARTAS DE OUROS NA HAND
        LinkedList<Integer> flush=new LinkedList<Integer>();

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
                    flush.add(c.valor);
                }
            }
        }
        else if(qc>=5){
            for(Carta c:cartas){
                if(c.naipe=='c'){
                    flush.add(c.valor);
                }
            }
        }
        else if(qe>=5){
            for(Carta c:cartas){
                if(c.naipe=='e'){
                    flush.add(c.valor);
                }
            }
        }
        else if(qo>=5){
            for(Carta c:cartas){
                if(c.naipe=='o'){
                    flush.add(c.valor);
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
    public int cartaAlta(){
       LinkedList<Integer> sorted = new LinkedList<Integer>();
       for(Carta c:cartas){
           sorted.add(c.valor);
       }
       Collections.sort(sorted, Collections.reverseOrder());
       return sorted.get(0);
    }

    public LinkedList<Integer> fullHouse(){
        LinkedList<Integer> fullHouse = new LinkedList<Integer>();
        if(pares().size()>=1 && trinca()>0){
            LinkedList<Integer> pares = pares();
            Collections.sort(pares,Collections.reverseOrder()); //ORDENA OS VALORES DOS PARES EM ORDEM DECRESCENTE
            fullHouse.add(pares.get(0)); // ADICIONA O VALOR DO PAR SEGUIDO DO VALOR DA TRINCA NA LISTA A RETORNAR
            fullHouse.add(trinca());
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

    public String toString(LinkedList<Integer> lista){
        String result="";
        if(lista==null){
            result= "Lista vazia";
        }
        else {
            for(int i=0;i<lista.size();i++){
                result=result+"Valor carta: "+lista.get(i)+"\n";
            }
        }
        return result;
    }

    public String toString(int t){
        return "valor: "+t;
    }

    public LinkedList<Integer> straight(){
        LinkedList<Integer> cartasNaoRepetidas=removerRepetidas();
        LinkedList<Integer> straight = new LinkedList<Integer>();
        int q=0;
        int valorAtual=0;
        if(!cartasNaoRepetidas.contains(14)){
            for(int i =0; i<cartasNaoRepetidas.size();i++){
                valorAtual=cartasNaoRepetidas.get(i)+1;
                if(cartasNaoRepetidas.get(i)==valorAtual){
                    straight.add(cartasNaoRepetidas.get(i));
                }
            }
        }

        return straight;
    }



    public LinkedList<Integer> removerRepetidas() {
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

