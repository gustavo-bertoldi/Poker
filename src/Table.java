import java.util.LinkedList;

public class Table {

    private LinkedList<Carte> mesa= new LinkedList<>();
    private Paquet paquet;
    private int pot;

    public Table(Paquet paquet){
        this.paquet=paquet;
    }

    public void distribuirCartes(){
        Distributeur.distribuirCartesMesa(paquet,mesa);
        pot=0;
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

    public void ajouterAuPot(int q){pot=pot+q;}

    public int getPot(){
        return pot;
    }

    public void turn(){
        mesa.get(3).montrerCarte();
    }

    public void river(){
        mesa.get(4).montrerCarte();
    }
}
