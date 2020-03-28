
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurV2 implements ActionListener {

    private FenetreJeuV2 fenetre;
    private char fun;

    public EcouteurV2(FenetreJeuV2 fenetre, char fonction){
        this.fenetre=fenetre;
        this.fun=fonction;
    }

    public void actionPerformed(ActionEvent e){
        if(fun == 'r'){
            fenetre.restart();
        }
    }
}
