import javax.swing.*;
import java.awt.*;

public class JanelaTeste extends JFrame {

    protected Baralho baralho=new Baralho();

    public JanelaTeste(){
        super("Poker");
        this.setSize(800,800);

        JPanel c = new JPanel(new FlowLayout());
        for(Carta car:baralho.baralho){
            c.add(new JButton(car.icone));
        }

        this.add(c);

        this.setVisible(true);

    }

    public static void main(String[] args){
        JanelaTeste j = new JanelaTeste();
    }
}
