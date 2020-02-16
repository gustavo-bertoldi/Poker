public class Carte {

    private int valeur;
    private char naipe;
    private boolean isOpen; // ta virada?

    public Carte(int val, char naipe){
                this.naipe = naipe;
                valeur = val;
                isOpen = false;
    }


}
