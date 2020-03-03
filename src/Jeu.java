import java.util.LinkedList;

public class Jeu {

    private LinkedList<Joueur> jogadores = new LinkedList<Joueur>();
    private Paquet paquet;
    private Table mesa;

    public Jeu(int nJogadores){
        for(int i=0;i<nJogadores;i++){
            jogadores.add(new Joueur());
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
        return jogadores;
    }



    public Jeu(){
        for(int i=0;i<6;i++){
            jogadores.add(new Joueur());
        }
        paquet= new Paquet();
        mesa = new Table(paquet);
        distribuirCartes();
        distribuerArgent(1500);
        montrerCartesJoueurActif();
    }

    public void distribuirCartes(){
        Distributeur.distribuirCartesJogadores(paquet,jogadores);
        mesa.distribuirCartes();
    }

    public void distribuerArgent(int q){
        Distributeur.distributeurDArgentDebut(jogadores, q);
    }

    public String maosJogadores(){
        String s="";
        for(Joueur j: jogadores){
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
        for(Carte c:jogadores.get(0).getHand()){
            c.montrerCarte();
        }

    }
}
