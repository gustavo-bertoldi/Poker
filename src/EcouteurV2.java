
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurV2 implements ActionListener {

    private FenetreJeuV2 fenetre;
    private String fun;

    public EcouteurV2(FenetreJeuV2 fenetre, String fonction){
        this.fenetre=fenetre;
        this.fun=fonction;
    }

    public void actionPerformed(ActionEvent e){
        if(fun == "restart"){
            fenetre.restart();
        }
        else if(fun == "commencer"){
            fenetre.changerDealer();
        }
        else if(fun == "flop"){
            fenetre.flop();
        }
        else if(fun == "turn"){
            fenetre.turn();
        }
        else if(fun == "river"){
            fenetre.river();
        }
    }
}
