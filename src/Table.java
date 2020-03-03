import java.util.LinkedList;

public class Table {

    private LinkedList<Carte> mesa= new LinkedList<>();
    private Paquet paquet;

    public Table(Paquet paquet){
        this.paquet=paquet;
    }

    public void distribuirCartes(){
        Distributeur.distribuirCartesMesa(paquet,mesa);
    }

    public LinkedList<Carte> getMesa(){
        return mesa;
    }

    public String visualizarMesa(){
        String s="";
        for(Carte c: mesa){
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
