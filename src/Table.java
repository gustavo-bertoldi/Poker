import java.util.LinkedList;

public class Table {

    private LinkedList<Carte> cartesTable = new LinkedList<>();
    private int pot;

    public Table(){

    }

    public LinkedList<Carte> getMesa(){
        return cartesTable;
    }

    public void flop(){
        for(int i=0;i<3;i++){
            cartesTable.get(i).montrerCarte();
        }
    }

    public void ajouterAuPot(int q){pot=pot+q;}

    public int getPot(){
        return pot;
    }

    public void turn(){
        cartesTable.get(3).montrerCarte();
    }

    public void river(){
        cartesTable.get(4).montrerCarte();
    }

    public void setCartesTable(LinkedList<Carte> cartesTable){
        this.cartesTable=cartesTable;
    }
}
