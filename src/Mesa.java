import java.util.LinkedList;

public class Mesa {

    private LinkedList<Carta> mesa=new LinkedList<Carta>();
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
}
