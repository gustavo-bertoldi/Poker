import javax.swing.*;
import java.awt.*;

public class JanelaTeste extends JFrame {

    protected Baralho baralho=new Baralho();

    private JPanel principal = new JPanel(new BorderLayout());
    private JPanel cartasJogador = new JPanel(new FlowLayout());
    private JPanel mesa = new JPanel(new FlowLayout());



    public JanelaTeste(){
        super("Poker");
        this.setSize(1200 ,1200);

        JPanel c = new JPanel(new FlowLayout());
        for(Carta car:baralho.baralho){
            c.add(new JLabel(car.icone));
        }

        this.add(c);

        this.setVisible(true);

    }

    public static void main(String[] args){
        JanelaTeste j = new JanelaTeste();
    }
}
