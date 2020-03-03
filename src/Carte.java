import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Carte extends Object implements Comparable{

    protected int valeur; //2=2,3=3,...,J=11,Q=12,K=13,A=14
    protected char couleur; //trefles=t, coeurs=c; piques=p; carreaux,diamant=d
    protected ImageIcon icon; /* Archive contenant l'image de la Carte dans la forme valeur_couleur.jpg Ex: 4_c.jpg (4 coeurs) */
    protected boolean tournée;
    /*
    Attributs en protected pour faciliter l'access sans le besoin de getters
     */

    public Carte(int valeur, char couleur){
        this.valeur=valeur;
        this.couleur=couleur;
        this.icon=new ImageIcon("src/res/back.png");
        redimensionar(70,93);
        tournée=true;
    }

    public void tournerCarte(){
        icon=new ImageIcon("src/res/back.png");
        redimensionar(70,93);
        tournée=true;
    }

    public void montrerCarte(){
        icon=new ImageIcon("src/res/"+valeur+"_"+couleur+".png");
        redimensionar(70,93);
        tournée=false;
    }

    public boolean equals(Carte c){
        if(valeur==c.valeur && couleur==c.couleur){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean equalsvaleur(Carte c){
        if(valeur==c.valeur && !equals(c)){
            return true;
        }
        else{
            return false;
        }
    }

    private void redimensionar(int w, int h){
        Image img = icon.getImage();
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();

        icon = new ImageIcon(resizedImg);
    }
    public String toString(){
        return "Carte:"+valeur+" "+couleur;
    }

    public int compareTo(Object o) {
        int comparaison =0;
        Carte c = (Carte)o;
        if(valeur < c.valeur){
            comparaison = -1;
        }else if(valeur > c.valeur){
            comparaison = 1;
        }
        return comparaison;
    }

}
