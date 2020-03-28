public class IntelligenceAleatoire extends Joueur{

    int raise;

    public IntelligenceAleatoire(){
        super(0);
    }

    public char jouer(int pot){
        int r = (int) (Math.random() * 3);
        if(r==0){
            return 'f';
        }

        else if(r==1){
            return 'c';
        }

        else{
            int raise = pot + (int)((super.getArgent()+1)*Math.random());
            return 'r';
        }
    }

    public int getRaise() {
        return raise;
    }
}
