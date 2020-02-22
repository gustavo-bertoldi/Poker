import java.util.LinkedList;

public class Jogo {

    private LinkedList<Jogador> jogadores = new LinkedList<Jogador>();
    private Baralho baralho;
    private Mesa mesa;

    public Jogo(int nJogadores){
        for(int i=0;i<nJogadores;i++){
            jogadores.add(new Jogador());
        }
        baralho= new Baralho();
        mesa = new Mesa(baralho);
    }

    public LinkedList<Carta> getMesa(){
        return mesa.getMesa();
    }

    public LinkedList<Jogador> getJogadores(){
        return jogadores;
    }



    public Jogo(){
        for(int i=0;i<6;i++){
            jogadores.add(new Jogador());
        }
        baralho= new Baralho();
        mesa = new Mesa(baralho);
    }

    public void distribuirCartas(){
        Distribuidor.distribuirCartasJogadores(baralho,jogadores);
        mesa.distribuirCartas();
    }

    public String maosJogadores(){
        String s="";
        for(Jogador j: jogadores){
            s=s+j.mao()+"\n";
        }
        return s;
    }

    public String visualizarMesa(){
        return mesa.visualizarMesa();
    }

    public String visualizarBaralho(){
        String s="";
        for(Carta c : baralho.baralho){
            s=s+c.toString()+"\n";
        }
        return s;
    }
}
