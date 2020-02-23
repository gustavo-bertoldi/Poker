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
        distribuirCartas();
        distribuerArgent(1500);
        montrerCartesJoueurActif();
    }

    public LinkedList<Carta> getCartasMesa(){
        return mesa.getMesa();
    }

    public Mesa getMesa(){
        return mesa;
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
        distribuirCartas();
        distribuerArgent(1500);
        montrerCartesJoueurActif();
    }

    public void distribuirCartas(){
        Distribuidor.distribuirCartasJogadores(baralho,jogadores);
        mesa.distribuirCartas();
    }

    public void distribuerArgent(int q){
        Distribuidor.distributeurDArgentDebut(jogadores, q);
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

    private void montrerCartesJoueurActif(){
        for(Carta c:jogadores.get(0).getHand()){
            c.montrerCarte();
        }

    }
}
