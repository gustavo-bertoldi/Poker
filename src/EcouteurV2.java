
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurV2 implements ActionListener {

    private FenetreJeuV3 fenetre;
    private String fun;

    public EcouteurV2(FenetreJeuV3 fenetre, String fonction){
        this.fenetre=fenetre;
        this.fun=fonction;
    }

    public void actionPerformed(ActionEvent e){
        if(fun.equals("restart")){
            //sauvegarder highscore, reouvrir menu.
        }else if(fun.equals("Check")){
            try {
                fenetre.setActionJoueurHumain(0);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else if(fun.equals("Call")){
            try {
                fenetre.setActionJoueurHumain(1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else if(fun.equals("Raise")){
            try {
                fenetre.setActionJoueurHumain(fenetre.getValeurRaiseSlider());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else if(fun.equals("ProchaineTournee")){
            try {
                fenetre.nouvelleTournee();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else if(fun.equals("Fold")){
            fenetre.foldJoueurHumain();
            try {
                fenetre.setActionJoueurHumain(-1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
