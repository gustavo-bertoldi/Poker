import com.sun.jdi.connect.spi.TransportService;

import java.util.LinkedList;

public class Jeu {

    private LinkedList<Joueur> joueurs = new LinkedList<Joueur>();
    private Paquet paquet;
    private Table mesa;
    private int pariMin;
    private int tourDealer=0;
    private int tourBig;
    private int tourSmall;
    private int nJoueurs;
    protected int joueurActif;
    private int smallBlind;
    private int bigBlind;

    public Jeu(int nJoeurs, int smallBlind, int niveau){
        this.smallBlind=smallBlind;
        this.bigBlind=2*smallBlind;
        this.nJoueurs=nJoeurs;
        joueurs.add(new Joueur(niveau));
        for(int i=1;i<nJoueurs;i++){
            joueurs.add(new Ordinateur(niveau));
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

    public LinkedList<Joueur> getJoueurs(){
        return joueurs;
    }

    public int getPariMin(){
        return pariMin;
    }

    public void setPariMin(int pariMin){
        this.pariMin=pariMin;
    }

    public void parier(int q, int indiceJoueur){
        if(joueurs.get(indiceJoueur).parier(q)) {
            mesa.ajouterAuPot(q);
        }
    }

    public Jeu(int smallBlind, int niveau){
        this.smallBlind=smallBlind;
        this.bigBlind=2*smallBlind;
        this.nJoueurs=6;
        joueurs.add(new Joueur(niveau));
        for(int i=1;i<6;i++){
            joueurs.add(new Ordinateur(niveau));
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

    public void prochainJoueur(){
        for(Joueur j: joueurs){
            j.resetAll();
        }
        if(nJoueurs<3){
            if(tourDealer==0){
                joueurs.get(0).setDealer();
                joueurs.get(1).setSmallBlind();
                joueurs.get(0).setBigBlind();
                tourDealer=1;
                tourSmall=0;
                tourBig=1;
            }
            else{
                joueurs.get(1).setDealer();
                joueurs.get(0).setSmallBlind();
                joueurs.get(1).setBigBlind();
                tourDealer=0;
                tourSmall=1;
                tourBig=0;
            }
        }
        else{
            if(tourDealer==nJoueurs){
                joueurs.get(tourDealer).setDealer();
                joueurs.get(tourBig).setBigBlind();
                joueurs.get(tourSmall).setSmallBlind();
                tourDealer=0;
                tourBig++;
                tourSmall++;
            }
            else if(tourDealer==nJoueurs-1){
                joueurs.get(tourDealer).setDealer();
                joueurs.get(tourSmall).setBigBlind();
                joueurs.get(tourBig).setSmallBlind();
                tourDealer++;
                tourBig++;
                tourSmall=0;
            }
            else if(tourDealer==nJoueurs-2){
                joueurs.get(tourDealer).setDealer();
                joueurs.get(tourSmall).setSmallBlind();
                joueurs.get(tourBig).setBigBlind();
                tourDealer++;
                tourSmall++;
                tourBig=0;
            }
            else{
                joueurs.get(tourDealer).setDealer();
                joueurs.get(tourSmall).setSmallBlind();
                joueurs.get(tourBig).setBigBlind();
                tourDealer++;
                tourSmall++;
                tourBig++;
            }
        }
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind(){
        return bigBlind;
    }

    public void setSmallBlind(int smallBlind){
        this.smallBlind=smallBlind;
        this.bigBlind=2*smallBlind;
    }

    public void sortirJoueur(int indice){
        joueurs.remove(indice);
        nJoueurs--;
    }

   /* public Joueur demarrerTournee() {
        LinkedList<Joueur> joueursDansLeJeu = joueurs;
        prochainJoueur();
        joueurs.get(tourBig).parier(bigBlind);
        joueurs.get(tourSmall).parier(smallBlind);
        while (joueursDansLeJeu.size() > 1) {
            for (int i = 0; i < joueursDansLeJeu.size(); i++) {
                if (joueurActif == joueursDansLeJeu.size() - 1) {
                    //joueursDansLeJeu.get(joueurActif).jouer();
                    joueurActif = 0;
                } else {
                    //joueursDansLeJeu.get(joueurActif).jouer();
                    joueurActif++;
                }
                //joueursDansLeJeu.get(joueurActif).jouer();
                joueursDansLeJeu.removeIf(j -> !j.isDansJeu());
            }
        }
        return joueursDansLeJeu.get(0);
    }*/

}
