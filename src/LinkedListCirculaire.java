import java.util.LinkedHashMap;
import java.util.LinkedList;

public class LinkedListCirculaire {

    private LinkedHashMap<Joueur, Node> nodes;
    protected Node tete;
    protected Node queue;
    private int size;

    public LinkedListCirculaire() {
        nodes = new LinkedHashMap<>();
        tete=null;
        queue=null;
        size=0;
    }

    public LinkedListCirculaire(LinkedList<Joueur> joueurs){
        nodes = new LinkedHashMap<>();
        tete=null;
        queue=null;
        size=0;
        joueurs.forEach(this::add);
    }

    public void add(Joueur joueur){
        Node nouveauJoueur = new Node(joueur);
        nodes.put(joueur, nouveauJoueur);

        if (tete == null){
            tete = nouveauJoueur;
        }
        else {
            queue.prochainNode = nouveauJoueur;
        }
        queue = nouveauJoueur;
        queue.prochainNode = tete;
        size++;
    }

    public boolean remove(Joueur joueurAEnlever){
        if(joueurAEnlever==null){
            return  false;
        }
        else if (nodes.containsKey(joueurAEnlever)){
            if(size==1){
                tete=null;
                queue=null;
                nodes.remove(joueurAEnlever);
                size--;
            }

            else if(joueurAEnlever.equals(tete.joueur)){
                tete=tete.prochainNode;
                nodes.remove(joueurAEnlever);
                queue.prochainNode=tete;
                size--;
            }
            else if(joueurAEnlever.equals(queue.joueur)){
                Node current=tete;
                while (!current.prochainNode.joueur.equals(joueurAEnlever)){
                    current=current.prochainNode;
                }
                queue=current;
                queue.prochainNode=tete;
                nodes.remove(joueurAEnlever);
                size--;

            }
            else {
                Node current = tete;
                while (!current.prochainNode.joueur.equals(joueurAEnlever)){
                    current=current.prochainNode;
                }
                current.prochainNode=nodes.get(joueurAEnlever).prochainNode;
                nodes.remove(joueurAEnlever);
                size--;
            }
            return true;
        }
        else{return false;}
    }

    public Node getNodeAnterieur (Node n){
        if (nodes.containsValue(n)) {
            Node current = tete;
            while (current.prochainNode != n) {
                current = current.prochainNode;
            }
            return current;
        }
        else {
            System.out.println("getNodeAnterieur : Node fourni n'est pas dans la liste");
            return null;
        }
    }

    public Node getNodeAnterieur (Joueur j){
        if (nodes.containsKey(j)) {
            Node current = tete;
            while (!current.prochainNode.joueur.equals(j)) {
                current = current.prochainNode;
            }
            return current;
        }
        else {
            System.out.println("getNodeAnterieur : Node fourni n'est pas dans la liste");
            return null;
        }
    }

    public int size(){
        return size;
    }

    public Node getNode(Joueur joueur) {
        if (nodes.containsKey(joueur)) {
            return nodes.get(joueur);
        } else {
            System.out.println("Joueur " + joueur.nom + " n'est pas dans la liste");
            return null;
        }
    }

    public Node getFirst(){
        return tete;
    }

    public Node getNodeBigBlind() throws Exception {
        return nodes.values().stream().filter(node -> node.joueur.bigBlind).findFirst()
                .orElseThrow(()-> new Exception("Il n'y a pas de joueur big blind dans la liste"));
    }

    public Node getNodeSmallBlind() throws Exception{
        return nodes.values().stream().filter(node -> node.joueur.smallBlind).findFirst()
                .orElseThrow(()-> new Exception("Il n'y a pas de joueur small blind dans la liste"));
    }

    public Node getNodeDealer() throws Exception{
        return nodes.values().stream().filter(node -> node.joueur.dealer).findFirst()
                .orElseThrow(()-> new Exception("Il n'y a pas de joueur dealer dans la liste"));
    }

    public Node getNodePlaying() throws Exception{
        return nodes.values().stream().filter(node -> node.joueur.playing).findFirst()
                .orElseThrow(()-> new Exception("Il n'y a pas de joueur playing dans la liste"));
    }

    public Joueur getJoueurHumain(){
        return nodes.keySet().stream().filter(joueur -> joueur.humain).findFirst().orElse(null);
    }

    public Node getNodeHumain(){
        return nodes.values().stream().filter(node -> node.joueur.humain).findFirst().orElse(null);
    }

    public LinkedList<Joueur> getJoueurs(){
        return new LinkedList<>(nodes.keySet());
    }

    public String toString(){
        if(size>0) {
            StringBuilder s = new StringBuilder("Joueurs dans la liste: \n");
            Node current = tete;
            for(int i=0; i<size;i++) {
                s.append(current.joueur.nom).append(". Prochain node: ").append(current.prochainNode.joueur.nom).append("\n");
                current=current.prochainNode;
            }
            System.out.println(s.toString());
            return s.toString();
        }
        else {
            System.out.println("Liste vide");
            return "Liste vide";
        }
    }

    public static void main(String[] args) throws Exception {
        LinkedList<Joueur> cl = new LinkedList<>();
        Joueur almir = new Joueur("almir",true);
        Ordinateur gus = new Ordinateur("gus",0);
        Ordinateur maria = new Ordinateur("maria",0);
        Ordinateur xing = new Ordinateur("xing",0);
        Ordinateur pri = new Ordinateur("pri",0);
        Ordinateur matheus = new Ordinateur("matheus",0);
        gus.bigBlind=true;
        matheus.playing=true;
        pri.dealer=true;
        maria.smallBlind=true;
        cl.add(almir);
        cl.add(gus);
        cl.add(maria);
        cl.add(xing);
        cl.add(pri);
        cl.add(matheus);
        LinkedListCirculaire circ = new LinkedListCirculaire(cl);


        circ.toString();
        circ.remove(matheus);
        circ.toString();
        circ.remove(almir);
        circ.toString();
    }

}