import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Carte extends Object implements Comparable{

    protected int valeur; //2=2,3=3,...,J=11,Q=12,K=13,A=14
    protected char couleur; //trefles=t, coeurs=c; piques=p; carreaux,diamant=d
    protected ImageIcon icon; /* Archive contenant l'image de la Carte dans la forme valeur_couleur.jpg Ex: 4_c.jpg (4 coeurs) */
    protected boolean tournée;
    private final String chemin;
    /*
    Attributs en protected pour faciliter l'access sans le besoin de getters
     */

    public Carte(int valeur, char couleur){
        this.valeur=valeur;
        this.couleur=couleur;
        this.chemin = "src/res/"+valeur+"_"+couleur+".png";
        this.icon = new ImageIcon(chemin);
        icon = redimensioner(84,112, icon);
        tournée=true;
    }


    public boolean equals(Carte c){
        return this.valeur==c.valeur && this.couleur==c.couleur;
    }

    public boolean equalsValeur(Carte c){
        if(valeur==c.valeur && !equals(c)){
            return true;
        }
        else{
            return false;
        }
    }

    public String description(boolean pluriel){
        String descp="";
        if(valeur<=10 && pluriel){
            descp=""+valeur+"s";
        }
        else if(valeur<=10 && !pluriel){
            descp=""+valeur;
        }
        else if(valeur==11){
            if(pluriel) {
                descp = "valets";
            }
            else{
                descp="valet";
            }
        }
        else if(valeur==12){
            if(pluriel) {
                descp = "dames";
            }
            else{
                descp="dame";
            }
        }
        else if(valeur==13){
            if(pluriel) {
                descp = "rois";
            }
            else{
                descp="roi";
            }
        }
        else if(valeur==14){
            descp="as";
        }
        return descp;
    }

    public static ImageIcon redimensioner(int w, int h, ImageIcon icon){
        Image img = icon.getImage();
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();

        return new ImageIcon(resizedImg);
    }
    public String toString(){
        return "Carte:"+valeur+" "+couleur+"\n"+chemin;
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
