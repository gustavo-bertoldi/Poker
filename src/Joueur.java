import java.util.LinkedList;

public class Joueur {

    private LinkedList<Carte> hand;
    protected String nome;
    private static int nJogadores=0;
    private int dinheiro;
    private boolean dealer;

    public Joueur(String nome){
        nJogadores++;
        this.nome=nome;
        this.dinheiro=0;
        this.dealer=false;
    }

    public Joueur(){
        nJogadores++;
        this.dinheiro=0;
        this.nome="Jogador "+nJogadores;
        this.dealer=false;
    }

    public LinkedList<Carte> getHand(){
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

    public void setHand(LinkedList<Carte> Cartes){
        this.hand=Cartes;
    }

    public String mao(){
        String mao="";
        for(Carte c: hand){
            mao=mao+c.toString()+"\n";
        }
        return mao;
    }

}
