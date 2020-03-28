import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurTable implements ActionListener {

    private FenetreJeu f; //La fenetre principale
    private char fun; //Un char qui definira la fonction a executer selon le bouton appuyé

    public EcouteurTable (FenetreJeu f,char fun){
        this.f=f;
        this.fun=fun;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(fun=='f'){
            f.flop();
        }
        else if(fun=='t'){
            f.turn();
        }
        else if(fun=='r'){
            f.river();
        }
        else if(fun=='x'){
            f.restart();
        }
        else if(fun == 'd'){
            f.dealer();
        }
        else if(fun =='j'){
           // f.jeu.jouer();
        }
        else if(fun =='g'){
            f.gagnant();
        }
    }
}
