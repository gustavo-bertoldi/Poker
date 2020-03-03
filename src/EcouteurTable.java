import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurTable implements ActionListener {

    private JanelaTeste j;
    private char fun;

    public EcouteurTable (JanelaTeste j,char fun){
        this.j=j;
        this.fun=fun;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(fun=='f'){
            j.flop();
        }
        else if(fun=='t'){
            j.turn();
        }
        else if(fun=='r'){
            j.river();
        }
        else if(fun=='x'){
            j.restart();
        }
        j.revalidate();
        j.repaint();
    }
}
