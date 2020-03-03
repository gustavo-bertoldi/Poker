import com.sun.jdi.connect.spi.TransportService;

import java.util.LinkedList;

public class Jeu {

    private LinkedList<Joueur> joueurs = new LinkedList<Joueur>();
    private Paquet paquet;
    private Table mesa;
    private int tourDealer=0;
    private int tourBig;
    private int tourSmall;
    private int nJoueurs;
    private int joueurActif;
    private int smallBlind;
    private int bigBlind;

    public Jeu(int nJoeurs, int smallBlind){
        this.smallBlind=smallBlind;
        this.bigBlind=2*smallBlind;
        this.nJoueurs=nJoeurs;
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



    public Jeu(int smallBlind){
        this.smallBlind=smallBlind;
        this.bigBlind=2*smallBlind;
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
                tourBig=0;
                tourSmall=1;
                tourDealer++;
            }
            else{
                joueurs.get(1).setDealer();
                joueurs.get(1).setBigBlind();
                joueurs.get(0).setSmallBlind();
                tourBig=1;
                tourSmall=0;
                tourDealer=0;
            }
        }
        else {
            if (tourDealer == 0) {
                joueurs.get(tourDealer).setDealer();
                joueurs.get(nJoueurs).setSmallBlind();
                joueurs.get(nJoueurs-1).setBigBlind();
                tourBig=nJoueurs-1;
                tourSmall=nJoueurs;
                tourDealer++;
            } else if(tourDealer==1) {
                joueurs.get(tourDealer).setDealer();
                joueurs.get(tourDealer-1).setSmallBlind();
                joueurs.get(nJoueurs).setBigBlind();
                tourBig=nJoueurs;
                tourSmall=tourDealer-1;
                tourDealer++;
            }
            else{
                joueurs.get(tourDealer).setDealer();
                joueurs.get(tourDealer-1).setSmallBlind();
                joueurs.get(tourDealer-2).setBigBlind();
                tourBig=tourDealer-2;
                tourSmall=tourDealer-1;
                if(tourDealer==nJoueurs) {
                    tourDealer=0;
                }
                else{
                    tourDealer++;
                }
            }
        }
        if(tourBig==nJoueurs){
            joueurActif=0;
        }
        else {
            joueurActif=tourBig+1;
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
    }

    /*public void demarrerTournee() {
        int nJoueursActifs=nJoueurs;
        boolean tourneeFinie = false;
        boolean raisesFinies = false;
        LinkedList<Joueur> joueursDansLeJeu = joueurs;
        while (tourneeFinie == false) {
            joueurs.get(tourBig).payer(bigBlind);
            joueurs.get(tourSmall).payer(smallBlind);
            for(int i=0;i<nJoueursActifs;i++){
                joueursDansLeJeu.get(joueurActif).jouer();
                if(joueurActif==nJoueursActifs){
                    joueurActif=0;
                }
                else{
                    joueurActif++;
                }
            }
            while(!raisesFinies && n) {
                int p=0;
                for(Joueur j: joueursDansLeJeu ){
                    if(j.aParie()){
                        p++;
                    }
                }
                if(p==0){
                    raisesFinies=true;
                    break;
                }
                joueursDansLeJeu = new LinkedList<>();
                for (int i = 0; i < nJoueurs; i++) {
                    if (joueurs.get(i).isDansJeu()) {
                        joueursDansLeJeu.add(joueurs.get(i));
                    }
                }

            }
        }
        prochaineTournee();
    }
    */

    public void demarrerTournee(){
        LinkedList<Joueur> joueursDansLeJeu = joueurs;
        boolean unPari = false;
        boolean tourneeFinie=false;
        prochaineTournee();
        while(!tourneeFinie){
            joueurs.get(tourBig).parier(bigBlind);
            joueurs.get(tourSmall).parier(smallBlind);
            for(int i=0;i<joueursDansLeJeu.size();i++){
                if(joueurActif==joueursDansLeJeu.size()-1){
                    //joueursDansLeJeu.get(joueurActif).jouer();
                    joueurActif=0;
                }
                else{
                    //joueursDansLeJeu.get(joueurActif).jouer();
                    joueurActif++;
                }
            }
        }
    }

}
