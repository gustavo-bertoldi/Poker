import java.util.LinkedList;

public class Jogador {

    private LinkedList<Carta> hand;
    protected String nome;
    private static int nJogadores=0;
    private int dinheiro;

    public Jogador(String nome){
        this.nome=nome;
        this.dinheiro=0;
    }

    public Jogador(){
        this.dinheiro=0;
        this.nome="Jogador "+nJogadores;
        nJogadores++;
    }

    public LinkedList<Carta> getHand(){
        return hand;
    }

    public void apostar(int q){
        dinheiro=dinheiro-q;
    }

    public void adicionarDinheiro(int q){
        dinheiro=dinheiro+q;
    }

    public int getDinheiro(){
        return dinheiro;
    }

    public void setHand(LinkedList<Carta> cartas){
        this.hand=cartas;
    }

    public String mao(){
        String mao="";
        for(Carta c: hand){
            mao=mao+c.toString()+"\n";
        }
        return mao;
    }

}
