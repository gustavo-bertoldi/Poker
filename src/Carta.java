import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Carta extends Object implements Comparable{

    protected int valor; //2=2,3=3,...,J=11,Q=12,K=13,A=14
    protected char naipe; //paus=p, copas=c; espadas=e; ouros=o
    protected ImageIcon icone; /* Arquivo da carta da forma valor_naipe.jpg Ex: 4_c.jpg (4 copas) */
    protected boolean tournée;
    /*
    Atributos em protected para facilitar o acesso nas outras classes sem a necessidade de getters
     */

    public Carta(int valor, char naipe){
        this.valor=valor;
        this.naipe=naipe;
        this.icone=new ImageIcon("/Users/gustavobertoldi/Documents/IntelliJ Projects/Poker/src/res/back.png");
        redimensionar(70,93);
        tournée=true;
    }

    public void tournerCarte(){
        icone=new ImageIcon("/Users/gustavobertoldi/Documents/IntelliJ Projects/Poker/src/res/back.png");
        redimensionar(70,93);
        tournée=true;
    }

    public void montrerCarte(){
        icone=new ImageIcon("/Users/gustavobertoldi/Documents/IntelliJ Projects/Poker/src/res/"+valor+"_"+naipe+".png");
        redimensionar(70,93);
        tournée=false;
    }

    public boolean equals(Carta c){
        if(valor==c.valor && naipe==c.naipe){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean equalsValor(Carta c){
        if(valor==c.valor && !equals(c)){
            return true;
        }
        else{
            return false;
        }
    }

    private void redimensionar(int w, int h){
        Image img = icone.getImage();
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();

        icone = new ImageIcon(resizedImg);
    }
    public String toString(){
        return "Carta:"+valor+" "+naipe;
    }

    public int compareTo(Object o) {
        int comparaison =0;
        Carta c = (Carta)o;
        if(valor < c.valor){
            comparaison = -1;
        }else if(valor > c.valor){
            comparaison = 1;
        }
        return comparaison;
    }
}
