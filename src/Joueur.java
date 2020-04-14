import java.util.LinkedList;

public class Joueur implements Comparable {

    protected Hand hand = new Hand();
    private LinkedList<Carte> cartesSurMain;

    protected String nom;
    private int argent=0;
    protected String coup="";

    //Attributs à utiliser pour déroulement du jeu
    protected boolean dealer=false;
    protected boolean dansJeu=false;
    protected boolean bigBlind=false;
    protected boolean smallBlind=false;
    protected boolean playing = false;
    protected boolean humain;
    protected int dernierValeurPariee=0;
    protected int action=-1;

    public Joueur (String nom){
        this.nom = nom;
        humain=true;
    }

    public void setAction(int action, int valeurPariActuelle) throws Exception {
        if(action==0){
            coup="Fold";
        }
        else if(action==1 && valeurPariActuelle==0){
            coup="Check";
        }
        else if(action==1 && valeurPariActuelle!=0){
            coup="Call "+valeurPariActuelle;
            parier(valeurPariActuelle);
        }
        else{
            coup="Raise "+action;
            parier(action);
        }
        this.action=action;
    }

    public void parier(int quantite) throws Exception{
        if(quantite>argent){
            throw new Exception("Joueur "+nom+": Pari supérieur a la quantite d'argent.");
        }
        else{
            argent=argent-quantite;
        }
    }

    public int getArgent(){
        return argent;
    }

    public void ajouterArgent(int argentAAjouter){
        argent = argent + argentAAjouter;
    }

    public void setCartesSurMain(LinkedList<Carte> cartesSurMain){
        this.cartesSurMain= new LinkedList<>(cartesSurMain);
    }

    public void setHand (LinkedList<Carte> cartesSurTable){
        hand.setHand(cartesSurMain,cartesSurTable);
    }

    public LinkedList<Carte> getCartesSurMain(){
        assert cartesSurMain!=null : "Cartes sur main du joueur "+nom+" n'ont pas été distribuées";
        return cartesSurMain;
    }

    public void setArgent(int argent){
        this.argent=argent;
    }

    public int getValeurHand(){
        return hand.getValeurHand();
    }

    public Hand getHand(){
        return hand;
    }

    public void jouer(int pariActuel) throws Exception {setAction(1,pariActuel);}



    public int compareTo(Object j2) {
        int comparaison = 0;
        if (hand.getValeurHand() > ((Joueur)j2).hand.getValeurHand()) {
            comparaison = 1;
        } else if (hand.getValeurHand() < ((Joueur)j2).hand.getValeurHand()) {
            comparaison = -1;
        }
        return comparaison;
    }

    public boolean equals(Object o){
        Joueur j = (Joueur)o;
        return j.nom.equals(this.nom);
    }

    public String toString(){
        StringBuilder s = new StringBuilder("Joueur : "+nom+". Argent : "+argent+"\n");
        if(bigBlind){
            s.append(" || big blind");
        }
        if (smallBlind){
            s.append(" || small blind");
        }
        if (dealer) {
            s.append(" || dealer");
        }
        if(playing){
            s.append(" || playing");
        }
        if(!dansJeu){
            s.append(" || hors jeu");
        }
        s.append("\n Dernier valeur pariee : ").append(dernierValeurPariee).append("\nAction : ").append(action);
        System.out.println(s.toString());
        return s.toString();
    }
}