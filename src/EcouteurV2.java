
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
        if(fun == 'x'){
            fenetre.restart();
        }
        else if(fun == 'c'){
            fenetre.call();
        }
        else if(fun == 'r'){
            fenetre.fold();
        }
        else if(fun == 'f'){
            fenetre.raise(0);
        }
        else if(fun == 's'){
            fenetre.start();
        }
    }
}
