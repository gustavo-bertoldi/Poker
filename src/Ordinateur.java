public class Ordinateur extends Joueur {

    private int niveau;

    public Ordinateur (String nom, int niveau){
        super(nom, niveau);
    }

    public void jouer(int pariActuel){
        //Cas de l'intelligence aléatoire
        if(niveau==0){
            int r = (int) (Math.random() * 30);
            if(r<=5){
                super.setAction(0,pariActuel);
            }

            else if(r>5 && r<=22){
                super.setAction(1,pariActuel);
            }

            else{
                int raise = pariActuel + (int)((super.getArgent()+1)*Math.random());
                super.setAction(raise, pariActuel);
            }
        }
    }
}
