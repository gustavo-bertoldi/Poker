import java.util.LinkedList;

public class Table {

    private LinkedList<Carte> cartesTable = new LinkedList<>();

    public Table(){

    }

    public LinkedList<Carte> getTable(){
        return cartesTable;
    }


    public void setCartesTable(LinkedList<Carte> cartesTable){
        this.cartesTable=cartesTable;
    }
}