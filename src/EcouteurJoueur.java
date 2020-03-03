import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurJoueur implements ActionListener {

    private JanelaTeste j; //La fenetre principale
    private char fun; /*Permet de executer une fonction slon le bouton appuyé
    'f'-fold, 'c'-call et 'r' - raise
    */

    public EcouteurJoueur (JanelaTeste j,char fun){
        this.j=j;
        this.fun=fun;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(fun=='f'){
            j.fold();
        }
        else if(fun=='c'){
            j.call();
        }
        else if(fun=='r'){
            j.raise();
        }
        j.revalidate();
        j.repaint();
    }
}