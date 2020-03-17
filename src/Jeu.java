import com.sun.jdi.connect.spi.TransportService;

import java.util.Collections;
import java.util.LinkedList;

public class Jeu {

    private LinkedList<Joueur> joueurs = new LinkedList<Joueur>(); //Les joueurs dans le jeu - remplie dans le constructeur
    private Paquet paquet; //Le paquet du jeu - remplie dans le constructeur
    private Table mesa;
    private int valeurCall=200; //La valeur minimale de pari pour jouer, defini en fonction des paris des joueurs
    private int tourDealer=0; //L'indice du joueur qui sera le dealer dans la ll joueurs
    private int tourBig; //L'indice du jouer qui sera le big blind dans la ll joueurs
    private int tourSmall; //L'indice du joueur qui sera le small blind dans la ll joueurs
    private int nJoueurs; //Le numero actuel de joueurs dans le jeu
    protected int joueurActif; //L'indice du joueur qui sera le prochain a jouer
    private int smallBlind; //La valeur du small blind actuel
    private int bigBlind; //La valeur du big blind actuel (2*smallBlind)

    /*
    Jeu a deux constructeurs, un prend en parametre:
    @param int nJoueurs - numero de joueurs
    @param int smallBlind - la valeur de la première small blind
    @param int niveau - pour controler l'intelligence de l'ordinateur

    Le constructeur cree un joueur human, et nJoueurs-1 ordinateurs, un paquet de cartes
    une table et fait la distribution des cartes et de l'argent entre les joueurs et la table

    Il change aussi l'icon des cartes du joueur pour q'elles soient affichées dans l'interface graphique
     */
    public Jeu(int nJoeurs, int niveau){
        this.nJoueurs=nJoeurs;
        joueurs.add(new Joueur(niveau));
        for(int i=1;i<nJoueurs;i++){
            joueurs.add(new Ordinateur(niveau));
        }
        paquet= new Paquet();
        mesa = new Table(paquet);
        distribuerCartes();
        distribuerArgent(1500);
        montrerCartesJoueurActif();
    }

    /*
    Le deuxieme constructeur fait essentiellement la meme chose que le premier, mais
    on met le nombre joueurs en 6 par default.
     */
    public Jeu(int niveau){
        this.nJoueurs=6;
        joueurs.add(new Joueur(niveau));
        for(int i=1;i<6;i++){
            joueurs.add(new Ordinateur(niveau));
        }
        paquet= new Paquet();
        mesa = new Table(paquet);
        distribuerCartes();
        distribuerArgent(1500);
        montrerCartesJoueurActif();
    }

    /*
    Retourne une ll avec les cartes dans la table
     */
    public LinkedList<Carte> getCartesMesa(){
        return mesa.getMesa();
    }

    /*
    Retourne la table
     */
    public Table getMesa(){
        return mesa;
    }

    /*
    Retourne une ll avec les joueurs dans le jeu
     */
    public LinkedList<Joueur> getJoueurs(){
        return joueurs;
    }

    /*
    Retourne la valeur minimale de pari pour continuer le jeu, valeur de call
     */
    public int getValeurCall(){
        return valeurCall;
    }

    /*
    Permet de changer la valeur minimale pour jouer
    @param int valeurCall - valeur a donner
     */
    public void setValeurCall(int valeurCall){
        this.valeurCall=valeurCall;
    }

    /*
    Permet de faire un joueur parier une certaine quantité
    @param int q - quantité a parier par le joueur
    @param int indiceJoueur - l'indice du joueur qui va parier dans la ll joueurs
     */
    public void parier(int q, int indiceJoueur){
        if(joueurs.get(indiceJoueur).parier(q)) {
            mesa.ajouterAuPot(q);
        }
    }

    /*
    Fait appel a la methode statique dans Distributeur pour distribuer deux cartes a chaque joueur de maniere
    aleatoire, bien comme 5 cartes qui sera la table
     */
    public void distribuerCartes(){
        Distributeur.distribuirCartesJogadores(paquet,joueurs);
        mesa.distribuirCartes();
    }

    /*
    Fait appel a la methode statique dans Distributeur pour distribuer une quantité q d'argent à chaque
    joueur dans la ll joueur
    @param int q - quantite d'argent a distribuer
     */
    public void distribuerArgent(int q){
        Distributeur.distributeurDArgentDebut(joueurs, q);
    }

    /*
    Methode de test
     */
    public String maosJogadores(){
        String s="";
        for(Joueur j: joueurs){
            s=s+j.mao()+"\n";
        }
        return s;
    }

    /*
    Methode de test
     */
    public String visualizarMesa(){
        return mesa.visualizarMesa();
    }

    /*
    Methode de test
     */
    public String visualizarBaralho(){
        String s="";
        for(Carte c : paquet.paquet){
            s=s+c.toString()+"\n";
        }
        return s;
    }

    /*
    Change l'icon des cartes de joueur humain pour les afficher dans
    l'interface graphique
     */
    private void montrerCartesJoueurActif(){
        for(Carte c:joueurs.get(0).getHand().getCartes()){
            c.montrerCarte();
        }
    }

    /*
    METHODE INCOMPLETE
    Cette methode controle la distribution du dealer, du small blind et du big blind dans
    la liste de joueurs
     */
    public void prochainJoueur(){
        //IL FAUT REMPLACER nJoueurs PAR LE NUMERO DE JOUEURS ACTIFS DANS LA TOURNEE - CREER METHODE POUR COMPTER
        for(Joueur j: joueurs){
            j.resetAll();
        }
        if(nJoueurs<3){//Dans le cas ou il n'y a que deux joueurs dans actifs
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
        else{ //Cas ou il y au moins 3 joueurs actifs
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

    /*
    Retourne la valeur de small blind Actuelle
     */
    public int getSmallBlind() {
        return smallBlind;
    }

    /*
    Retourne la valeur de big blind actuelle
     */
    public int getBigBlind(){
        return bigBlind;
    }

    /*
    Permet de changer les valeurs du small blind et du big blind actuelles
    @param int smallBlind - valeur a prendre
     */
    public void setSmallBlind(int smallBlind){
        this.smallBlind=smallBlind;
        this.bigBlind=2*smallBlind;
    }

    /*
    Quand un joueur perd, permet de le sortir du jeu bien comme de la ll de joueurs
    @param int indice- indice du joueur a sortir
     */
    public void sortirJoueur(int indice) {
        joueurs.remove(indice);
        nJoueurs--;
    }

    /*
    Methode private, retourne une ll avec les hands des joueurs dea triee
     */
    private LinkedList<Hand> getAllHands(){
        LinkedList<Hand> hands = new LinkedList<>();
        for(Joueur j:joueurs){
            hands.add(j.getHand());
        }
        Collections.sort(hands,Collections.reverseOrder());
        return hands;
    }

    /*
    Retourne la hand gagnante, si split pot, retou
     */
    public LinkedList<Hand> handsGagnantes(){
        LinkedList<Hand> handsGagnantes = new LinkedList<>();
        LinkedList<Hand> allHands = getAllHands();
        handsGagnantes.add(allHands.getFirst());
        for(int i=1;i<allHands.size();i++){
            if(allHands.get(i)==allHands.get(0)){
                handsGagnantes.add(allHands.get(i));
            }
        }
        return handsGagnantes;
    }

    private void distribuerCartesJoueurs(){
        for (Joueur j : joueurs){
            LinkedList<Carte> cartesJoueur = new LinkedList<>();
            int i = (int) ((paquet.size()) * Math.random());
            cartesJoueur.add(paquet.get(i));
            paquet.remove(i);
            i = (int) ((paquet.size()) * Math.random());
            cartesJoueur.add(paquet.get(i));
            paquet.remove(i);
        }
    }




}
