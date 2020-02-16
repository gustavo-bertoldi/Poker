/*Reflexions
1) créer les joueurs dans la table ou les affecter à une table?
2) tableau classé de hands?
 */
/* Done
1) setDealer()
 */
public class Table {

    final private int taille = 9;

    private Joueur[] joueurs;
    private Hand[] hands;
    private double pot;
    private double buyIn;
    private double smallPrice;
    private double bigPrice;

    public Table(Joueur[] j){
        joueurs = j;
    }

    /*Méthode pour définir les roles dans la table a chaque fois.
    La position du dealer definit directement les positions du Small et du Big
     */

    public void setDealer() {
        int posDeal = (int)Math.random()*(taille);
        int posSmall =0;
        int posBig = 0;
        if(posDeal==(taille-1)) {
            posSmall = posDeal+1; // pas besoin d'affecter posBig car 0
        }else if(posDeal == taille) {
            posBig = 1;
        }else {
            posSmall = posDeal+1;
            posBig = posSmall+1;
        }

        joueurs[posDeal].devenirDealer();
        joueurs[posSmall].devenirSmall();
        joueurs[posBig].devenirBig();
    }

    //En dessous sont des methodes pour tester les affectations etc
    public void afficherTable() {
        for(int i =0; i<joueurs.length; i++) {
            System.out.println(joueurs[i]);
        }
    }

}

