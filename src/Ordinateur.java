public class Ordinateur extends Joueur {

    private int niveau;

    public Ordinateur (String nom, int niveau){
        super(nom, niveau);
    }

    public void jouer(int pariActuel){
        //Cas de l'intelligence aléatoire
        if(niveau==0){
            int r = (int) (Math.random() * 3);
            if(r==0){
                super.setAction(r);
            }

            else if(r==1){
                super.parier(pariActuel);
                super.setAction(r);
            }

            else{
                int raise = pariActuel + (int)((super.getArgent()+1)*Math.random());
                super.setAction(raise);
                super.parier(50);
            }
        }
    }
}
