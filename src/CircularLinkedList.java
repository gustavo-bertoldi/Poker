    import java.util.LinkedList;
public class CircularLinkedList {
    //LinkedListCirculaire
    private LinkedList<Node> joueurs = new LinkedList<>();
    protected Node head = null;
    protected Node tail = null;

    public CircularLinkedList(int numJoueurs){
        for(int i =0;i<numJoueurs;i++){
            String nom = "joueur" + i;
            Joueur j = new Joueur(nom);
            addNode(j);
        }
    }
    public CircularLinkedList(LinkedList<Joueur> linkedListJoueurs){
        for(Joueur j :linkedListJoueurs){
            addNode(j);
        }
    }
    public void addNode(Joueur joueurAjoute) {
        Node nouveauJoueur = new Node(joueurAjoute);

        if (head == null) { //si liste vide, la tete sera le joueurAjoute
            head = nouveauJoueur;
        } else { // si elle n'est pas vide, on fait en sorte que le dernier de la liste pointe vers le joueurAjoutee
            tail.prochainNode = nouveauJoueur;
        }

        tail = nouveauJoueur; //le joueur ajoutee devient alors le dernier de la liste
        tail.prochainNode = head; // étant le dernier, il doit pointer vers la tete de la liste
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
            System.out.println();
        }
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

    }

    public Node get(Joueur joueur){
        Node current = head;
        do{
            current = current.prochainNode;
        }while(!current.joueur.equals(joueur));
        return current;
    }



    public static void main(String[] args) {
        CircularLinkedList cl = new CircularLinkedList(6);
        //Adds data to the list

        cl.display();
    }
}
