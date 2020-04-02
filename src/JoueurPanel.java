import javax.swing.*;
import java.awt.*;

public class JoueurPanel extends JPanel {

    private JPanel attributs;
    private JPanel cartes;
    private JPanel position; // juste pour tester les changements de position, mais peut etre utile de mettre les boutons de decision

    public JoueurPanel(){
        attributs = new JPanel(new GridLayout(2,1,0,0));
        cartes = new JPanel();
        position = new JPanel();

    }

    public JPanel getAttributs() {
        return attributs;
    }

    public JPanel getCartes() {
        return cartes;
    }

    public JPanel getPosition() {
        return position;
    }

    public void setAttributs(JPanel attributs) {
        this.attributs = attributs;
    }

    public void setCartes(JPanel cartes) {
        this.cartes = cartes;
    }

    public void setPosition(JPanel position) {
        this.position = position;
    }
}
