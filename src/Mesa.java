import java.util.LinkedList;

public class Mesa {

    private LinkedList<Carta> mesa= new LinkedList<>();
    private Baralho baralho;

    public Mesa(Baralho baralho){
        this.baralho=baralho;
    }

    public void distribuirCartas(){
        Distribuidor.distribuirCartasMesa(baralho,mesa);
    }

    public LinkedList<Carta> getMesa(){
        return mesa;
    }

    public String visualizarMesa(){
        String s="";
        for(Carta c: mesa){
            s=s+c.toString()+"\n";
        }
        return s;
    }

    public void flop(){
        for(int i=0;i<3;i++){
            mesa.get(i).montrerCarte();
        }
    }

    public void turn(){
        mesa.get(3).montrerCarte();
    }

    public void river(){
        mesa.get(4).montrerCarte();
    }
}
