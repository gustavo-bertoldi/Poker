import java.util.LinkedList;

public class Jeu {

    private LinkedList<Joueur> joueurs = new LinkedList<Joueur>();
    private Paquet paquet;
    private Table mesa;
    private int tourDealer=0;
    private int nJoueurs;
    private int joueurActif;

    public Jeu(int nJoeurs){
        this.nJoueurs=nJoeurs;
        this.joueurActif=0;
        joueurs.add(new Joueur());
        for(int i=1;i<nJoueurs;i++){
            joueurs.add(new Ordinateur());
        }
        paquet= new Paquet();
        mesa = new Table(paquet);
        distribuirCartes();
        distribuerArgent(1500);
        montrerCartesJoueurActif();
    }

    public LinkedList<Carte> getCartesMesa(){
        return mesa.getMesa();
    }

    public Table getMesa(){
        return mesa;
    }

    public LinkedList<Joueur> getJogadores(){
        return joueurs;
    }



    public Jeu(){
        this.nJoueurs=6;
        joueurs.add(new Joueur());
        for(int i=1;i<6;i++){
            joueurs.add(new Ordinateur());
        }
        paquet= new Paquet();
        mesa = new Table(paquet);
        distribuirCartes();
        distribuerArgent(1500);
        montrerCartesJoueurActif();
    }

    public void distribuirCartes(){
        Distributeur.distribuirCartesJogadores(paquet,joueurs);
        mesa.distribuirCartes();
    }

    public void distribuerArgent(int q){
        Distributeur.distributeurDArgentDebut(joueurs, q);
    }

    public String maosJogadores(){
        String s="";
        for(Joueur j: joueurs){
            s=s+j.mao()+"\n";
        }
        return s;
    }


    public String visualizarMesa(){
        return mesa.visualizarMesa();
    }

    public String visualizarBaralho(){
        String s="";
        for(Carte c : paquet.paquet){
            s=s+c.toString()+"\n";
        }
        return s;
    }

    private void montrerCartesJoueurActif(){
        for(Carte c:joueurs.get(0).getHand()){
            c.montrerCarte();
        }

    }
    /*
    Cette methode controle la distribution du dealer, du small blind et du big blind dans
    la liste de joueurs
     */
    public void prochaineTournee(){
        for(Joueur j: joueurs){
            j.resetAll();
        }
        if(nJoueurs<3){
            if(tourDealer==0){
                joueurs.get(0).setDealer();
                joueurs.get(0).setBigBlind();
                joueurs.get(1).setSmallBlind();
                tourDealer++;
            }
            else{
                joueurs.get(1).setDealer();
                joueurs.get(1).setBigBlind();
                joueurs.get(0).setSmallBlind();
                tourDealer=0;
            }
        }
        else {
            if (tourDealer == 0) {
                joueurs.get(tourDealer).setDealer();
                joueurs.get(nJoueurs).setSmallBlind();
                joueurs.get(nJoueurs-1).setSmallBlind();
                tourDealer++;
            } else if(tourDealer==1) {
                joueurs.get(tourDealer).setDealer();
                joueurs.get(tourDealer-1).setSmallBlind();
                joueurs.get(nJoueurs).setBigBlind();
                tourDealer++;
            }
            else{
                joueurs.get(tourDealer).setDealer();
                joueurs.get(tourDealer-1).setSmallBlind();
                joueurs.get(tourDealer-2).setBigBlind();
                if(tourDealer==nJoueurs) {
                    tourDealer=0;
                }
                else{
                    tourDealer++;
                }
            }
        }

    }

    public void demarrerTournee(){

    }

}
