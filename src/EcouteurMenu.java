import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurMenu implements ActionListener {

    private Menu fenetre;
    private String fun;

    public EcouteurMenu(Menu fenetre, String fonction){
        this.fenetre=fenetre;
        this.fun=fonction;
    }

    public void actionPerformed(ActionEvent e) {
        if(fun == "facile"){
            fenetre.setDifficulte(0);
        } else if(fun == "medium"){
            fenetre.setDifficulte(1);
        } else if(fun == "hard"){
            fenetre.setDifficulte(2);
        } else if(fun == "start"){
            try {
                fenetre.lancerJeu();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}