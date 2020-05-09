public class Ordinateur extends Joueur {

    private int niveau;

    public Ordinateur (String nom, int niveau){
        super(nom, false);
        this.niveau=niveau;
    }

    public void jouer(int pariActuel, Jeu jeu) throws Exception {
        //Cas de l'intelligence aléatoire
        super.setAction(Intelligence.getDecision(jeu, this, niveau), pariActuel, jeu);

    }
    public int getNiveau(){
        return niveau;
    }
}
