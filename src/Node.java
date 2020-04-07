public class Node  {

    protected Joueur joueur;
    protected Node prochainNode;

    public Node(Joueur j){
        this.joueur = j;
    }

    public boolean equals(Object o){
        Node n = (Node)o;
        boolean egaux = false;
        egaux = n.joueur.equals(this.joueur); //  les noms étant imposes differents, ça suffit
        return egaux;
    }
}
