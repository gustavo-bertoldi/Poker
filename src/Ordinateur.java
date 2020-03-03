
public class Ordinateur extends Joueur {

    protected String nom;
    private static int nOrdinateurs=0;

    public Ordinateur(int niveau){
        super(niveau);
        nOrdinateurs++;
        this.nom="Ordinateur "+nOrdinateurs;
    }




}
