public class Ordinateur extends Joueur {

    private int niveau;

    public Ordinateur (String nom, int niveau){
        super(nom, false);
    }

    public void jouer(int pariActuel, Jeu jeu) throws Exception {
        //Cas de l'intelligence aléatoire
        if(niveau==0){
            if(allIn){
                super.setAction(1,pariActuel,jeu);
            }

            else if((pariActuel-derniereValeurPariee)==0){
                int r = (int) (Math.random() * 30);
                if(r<=5){
                    int raiseRange = (int) (50 * Math.random());
                    int raise;
                    if (raiseRange <= 1) {
                        raise = super.getArgent();
                    } else if (raiseRange < 40) {
                        raise = 50;
                    } else {
                        raise = 100;
                    }
                    super.setAction(raise, pariActuel, jeu);
                }
                else{
                    super.setAction(1,pariActuel,jeu);
                }
            }

            else {
                int r = (int) (Math.random() * 30);
                if (r <= 10) {
                    super.setAction(0,pariActuel, jeu);
                } else if (r <= 26) {
                    super.setAction(1, pariActuel, jeu);
                } else {
                    if (jeu.pariActuel <= getArgent() / 10) {
                        int raiseRange = (int) (50 * Math.random());
                        int raise;
                        if (raiseRange <= 2) {
                            raise = super.getArgent();
                        } else if (raiseRange < 35) {
                            raise = 2 * pariActuel;
                        } else {
                            raise = 4 * pariActuel;
                        }
                        super.setAction(raise, pariActuel, jeu);
                    } else {
                        super.setAction(1, pariActuel, jeu);
                    }
                }
            }
        }
    }
}
