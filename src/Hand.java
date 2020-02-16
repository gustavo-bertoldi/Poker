import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Hand {

    private LinkedList<Carta> cartas;


    public Hand() {

    }

    public void setHand(){
    }

    /*
    Verifica a quantidade de pares em uma hand e os retorna em uma lista com seus respectivos valores.
    O tamanho da lista é a quantidade de pares encontrados na hand.
     */
    public ArrayList<Integer> pares(){
        int q=0; //quantidade de valores iguais em cartas para cada valor i
        ArrayList<Integer> valoresEncontrados = new ArrayList<Integer>();

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
        ArrayList<Integer> valoresEncontrados = new ArrayList<Integer>();

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
        return valoresEncontrados.get(0); //Retorna o primeiro valor da lista valoreEncontrados, ou seja, a maior trinca na hand.

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

        return valorEncontrado; //Retorna o valor da quadra encontrada

    }

    public ArrayList<Integer> flush(){
        int qp=0; //QUANTIDADE DE CARTAS DE PAUS NA HAND
        int qc=0; //QUANTIDADE DE CARTAS DE COPAS NA HAND
        int qe=0; //QUANTIDADE DE CARTAS DE ESPADAS NA HAND
        int qo=0; //QUANTIDADE DE CARTAS DE OUROS NA HAND
        ArrayList<Integer> flush=new ArrayList<Integer>();

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
        Collections.sort(flush, Collections.reverseOrder()); //ORDENA A LISTA DE VALORES EM ORDEM DECRESCENTE
        return flush;
    }

    public ArrayList<Integer> straight(){
        ArrayList<Integer> straight = new ArrayList<Integer>();
        int q=0;
        for(int i=0;i<cartas.size();i++){
            for(Carta c:cartas){
                if(c.valor==cartas.get(i).valor+1 && cartas.get(i).valor!=14){
                    i=cartas.indexOf(c);
                    straight.add(cartas.get(i).valor);
                    q++;
                }
                else if (cartas.get(i).valor==14){
                    if(c.valor==2){
                        i=cartas.indexOf(c);
                        straight.add(cartas.get(i).valor);
                        q++;
                    }
                }
            }
            if(q<5){
                q=0;
                straight = new ArrayList<Integer>();
            }
            else{break;}
        }
        Collections.sort(straight, Collections.reverseOrder());
        return straight;
    }


}

