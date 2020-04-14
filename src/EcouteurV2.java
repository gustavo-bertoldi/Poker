
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
            try {
                fenetre.restart();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else if(fun == "commencer"){
            try {
                fenetre.avancerJeu();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
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
        else if(fun == "call"){
            try {
                fenetre.setActionJoueurHumain(1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else if(fun == "raise"){
            try {
                fenetre.setActionJoueurHumain(fenetre.getValeurRaiseSlider());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else if(fun == "fold"){
            try {
                fenetre.foldJoueurHumain();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                fenetre.setActionJoueurHumain(0);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
