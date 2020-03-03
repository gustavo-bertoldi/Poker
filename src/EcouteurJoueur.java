import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurJoueur implements ActionListener {

    private JanelaTeste j;
    private char fun;

    public EcouteurJoueur (JanelaTeste j,char fun){
        this.j=j;
        this.fun=fun;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(fun=='f'){
            j.fold();
            j.revalidate();
            j.repaint();
        }
        else if(fun=='c'){
            j.call();
            j.revalidate();
            j.repaint();
        }
        else if(fun=='r'){
            j.raise();
            j.revalidate();
            j.repaint();
        }
        j.getJouerActif().dejaJoue=true;
    }
}