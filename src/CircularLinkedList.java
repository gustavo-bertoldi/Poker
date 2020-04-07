    import java.util.Collections;
    import java.util.LinkedList;

public class CircularLinkedList <T extends Joueur>  {
    //LinkedListCirculaire
    protected LinkedList<Node> joueurs = new LinkedList<>();
    protected Node head = null;
    protected Node tail = null;
    private int size=0;

    public CircularLinkedList(){
    }

    public void addNode(T joueurAjoute) {
        Node nouveauJoueur = new Node(joueurAjoute);
        joueurs.add(nouveauJoueur);
        size++;
        if (head == null) { //si liste vide, la tete sera le joueurAjoute
            head = nouveauJoueur;
        } else { // si elle n'est pas vide, on fait en sorte que le dernier de la liste pointe vers le joueurAjoutee
            tail.prochainNode = nouveauJoueur;
        }

        tail = nouveauJoueur; //le joueur ajoutee devient alors le dernier de la liste
        tail.prochainNode = head; // étant le dernier, il doit pointer vers la tete de la liste
    }

    /*
    Méthode utilisé lors de la définition du joueurs gagnant, la LinkedList peut être triée selon la valeur
    de la hand de chaque joueur.
     */
    public LinkedList<T> getLinkedListJoueurs(){
        LinkedList<T> tousJoueurs = new LinkedList<>();
        for (Node n : joueurs){
            tousJoueurs.add((T) n.joueur);
        }
        return tousJoueurs;
    }

    public void display(){
        Node current = head;
        if(head == null) {
            System.out.println("Liste vide");
        }
        else {
            System.out.println("Noeuds de la liste ");
            do{
                //Prints each node by incrementing pointer.
                System.out.print(" "+ current.joueur.toString());
                current = current.prochainNode;
            }while(current != head);
            System.out.println("\nSize: "+size);
        }
    }

    public int size(){
        return size;
    }

    public void parcourir(char cherche){
        Node current = head;
        if(cherche == 'd') {
            if (head == null) {
                System.out.println("Liste vide");
            } else {
                do { //implementer mehtode pour faire ce que l'on veut
                    //Prints each node by incrementing pointer.
                    System.out.print(" " + current.joueur.toString()); //action
                    current = current.prochainNode;
                } while (!current.joueur.dealer);// condition d'arret
                System.out.println();
            }
        }
        if(cherche == 'a'){

            do { //implementer mehtode pour faire ce que l'on veut
                //Prints each node by incrementing pointer.
                current = current.prochainNode;
                } while (!current.joueur.playing);// condition d'arret
            System.out.print(" " + current.joueur.toString());
        }
        System.out.println("Size: "+size);
    }

    public void remove(T j){
        /*
        Vérifie si la liste contient le joueur à enlever, et si ce joueur n'est pas le joueur humain(head).
         */
        if(size==1){
            head=null;
            joueurs.remove(head);
            size--;
        }
        else if(j==head.joueur){
            Node current=head;
            Node aEnlever = get(j);
            do{
                current = current.prochainNode;
            }while(current.prochainNode != aEnlever);
            current.prochainNode=aEnlever.prochainNode;
            joueurs.remove(aEnlever);
            head=head.prochainNode;
            size--;
        }
        else{
            Node current=head;
            Node aEnlever = get(j);
            do{
                current = current.prochainNode;
            }while(current.prochainNode != aEnlever);
            current.prochainNode=aEnlever.prochainNode;
            size--;
        }

    }

    public Node get(T joueur){
        Node current = head;
        do{
            current = current.prochainNode;
        }while(!current.joueur.equals(joueur));
        return current;
    }

    public Node getJoueurPlaying(){
        Node current = head;
        do{
            current = current.prochainNode;
        } while (!current.joueur.playing);
        return current;
    }

    public Node getJoueurSmallBlind(){
        Node current = head;
        do{
            current = current.prochainNode;
        } while (!current.joueur.smallBlind);
        return current;
    }

    public Node getJoueurPlaying(){
        Node current = head;
        do{
            current = current.prochainNode;
        } while (!current.joueur.playing);
        return current;
    }

    public Node getJoueurDealer(){
        Node current = head;
        do{
            current = current.prochainNode;
        } while (!current.joueur.dealer);
        return current;
    }



    public static void main(String[] args) {
        CircularLinkedList<Joueur> cl = new CircularLinkedList<>();
        //Adds data to the list

        cl.display();
    }
}
