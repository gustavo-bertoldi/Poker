import javax.xml.catalog.Catalog;
import java.util.Collections;
import java.util.LinkedList;

public class Intelligence{

    public static int getDecision(Jeu jeu, Joueur joueur, int niveau){
        if(niveau==0){
            return intelligenceAleatoire(jeu,joueur);
        }
        else if(niveau==1){
            return intelligenceNiveau1(jeu,joueur);
        }
        else if(niveau==2){
            return intelligenceNiveau2(jeu,joueur);
       } else{
            return -2;
        }
    }

    private static int intelligenceAleatoire(Jeu jeu, Joueur joueur){
        int decision;
        if(joueur.allIn){
            decision=1;
        }

        else if((jeu.pariActuel-joueur.derniereValeurPariee)==0){
            int r = (int) (Math.random() * 30);
            if(r<=5){
                int raiseRange = (int) (50 * Math.random());
                int raise;
                if (raiseRange <= 1) {
                    raise = joueur.getArgent();
                } else if (raiseRange < 40) {
                    raise = 50;
                } else {
                    raise = 100;
                }
                decision=raise;
            }
            else{
                decision=1;
            }
        }

        else {
            int r = (int) (Math.random() * 30);
            if (r <= 10) {
                decision=0;
            } else if (r <= 26) {
                decision=1;
            } else {
                if (jeu.pariActuel <= joueur.getArgent() / 10) {
                    int raiseRange = (int) (50 * Math.random());
                    int raise;
                    if (raiseRange <= 2) {
                        raise = joueur.getArgent();
                    } else if (raiseRange < 35) {
                        raise = 2 * jeu.pariActuel;
                    } else {
                        raise = 4 * jeu.pariActuel;
                    }
                    decision=raise;
                } else {
                    decision=1;
                }
            }
        }
        return decision;
    }

    private static int intelligenceNiveau1 (Jeu jeu, Joueur joueur){
        int decision=0;
        int valeurHandMoment;
        int pot = jeu.potActuel;
        int pariAct = jeu.pariActuel;
        boolean checkPossible = jeu.pariActuel == 0;
        char typeRange = joueur.getHand().calculerRangePreFlop().getType();
        LinkedList<Carte> cartesSurMain = joueur.getCartesSurMain();
        LinkedList<Carte> cartesSurTable = joueur.getHand().getSurTable();
        int[] bets = new int[6];
        if(pariAct!=0){
            bets[0] = 2*pariAct;
            bets[1] = 3*pariAct;
            bets[2] = 4*pariAct;
        }else{
            bets[0] = pot;
            bets[1] = 2*pot;
            bets[2] = 3*pot;
        }
        bets[3] = pot;
        bets[4] = 2*pot;
        bets[5] = 3*pot;

        cartesSurTable.sort(Collections.reverseOrder());
        cartesSurMain.sort(Collections.reverseOrder());

        int pariActuel = jeu.pariActuel;

        // decision preFlop
        if(jeu.moment==0){
            double pourcentage = joueur.getHand().getRangePreFlop().getOddsRaise();
            if(typeRange =='r'){
                if(Math.random()<=pourcentage){
                    int indexBet = (int)(Math.random()*6);
                    decision = bets[indexBet];
                }else{
                    decision = 1;
                }
            }else if(typeRange == 'k'){
                if(joueur.getArgent()/3 >(pariActuel-joueur.derniereValeurPariee)){ //peut etre qu'il soit necessaire ajouter comparaison entre pari et pariJoueur
                    decision = 1;
                }else{
                    decision = -1;
                }
            }else{
                decision = -1;
            }
        }else if(jeu.moment == 1){
            if(typeRange =='r'){

            } else if(typeRange =='k') {

            } else if(typeRange =='f'){

            }
        }
        if(!checkPossible && decision == 0){
            decision =-1;
        }
        return decision;
    }

    private static int intelligenceNiveau2 (Jeu jeu, Joueur joueur){
        int decision=0;
        int valeurMoment2=0;
        int valeurHand = joueur.getHand().getValeurHandMoment();

        if (!jeu.flop) { //quand le joueur a seulement ses 2 cartes à lui

           if (valeurHand > 14) { //si le joueur a au moins une paire, il va doubler le pari
                decision =  2 * jeu.pariActuel;
           }

           else if (joueur.bigBlind) { //si le joueur est le Big Blind, il va toujours passer au moment 1
               decision = 1;
           }

           else if (jeu.pariActuel < (1 / 10) * joueur.getArgent()) { //si le pari actuel est peu important en comparaison à l´argent du joueur, le joueur paye toujours le pari
                decision = 1;

           }
           /*
           else { //si le pari actuel est assez important
                if (joueur.getHand().getValeurHandSurMain() >= 8) { //si le joueur a une carte supérieure ou égale à 8 (et pas de paires ni rien meilleur), il paye le parie. Sinon, il arrête de jouer.
                    decision = 1;
                } else {
                    decision = 0;
                }
           }
        }


        else if (!jeu.turn) { //quand les 3 premières cartes sont révélées

            if (valeurHand < 8 && jeu.pariActuel > 0) { //si le joueur était le Big Blind mais il n´a que des cartes inférieures à 8 (et pas de paires ni rien meilleur), il arrête de jouer
                decision = 0;
            }

            if (joueur.getHand().getValeurHandApresFlop() < 60) { //si le joueur a une paire de 5 ou une Hand moins importante
                
                if(jeu.pariActuel > 0.5*joueur.getArgent()){ //si le pari est supérieur à la moitié de l´argent que possède le joueur
					decision = -1; // il arrête de jouer
					} else { //sinon (le pari est inférieur à la moitié de son argent)
					decision = 1; //il paye le pari actuel
					}
                
            } else { //Si il a une Hand plus importante, il augmente de 300 le pari actuel.
                decision = jeu.pariActuel + 300;
            }

         
        }

        else if (!jeu.river) { //quand il y a 4 cartes révélées
            if(joueur.allIn){
                decision=1;
            }

            else if (joueur.getHand().getValeurHandApresTurn() < 230 && jeu.pariActuel > 0) { // si le joueur n´a pas au moins 2 paires, il arrête de jouer
				decision = -1;
					
			} else if (joueur.getHand().getValeurHandApresTurn() < 650){ //sinon (le joueur a au moins deux paires), si les 2 paires sont moyennement fortes, il paye le pari actuel
				decision = 1; 

            } else { //si le joueur a au moins 2 paires fortes, il augmente de 200 le pari actuel
                decision = (jeu.pariActuel + 200);  
            }
            valeurMoment2 = joueur.getHand().getValeurHandApresTurn(); //variable intermédiaire pour stocker la valeur du Hand au moment 2
        }

        else { //quand toutes les cartes ont été révélées
            if(joueur.allIn){
                decision=1;
            }
            else if (joueur.getHand().getValeurHandApresRiver() == valeurMoment2) { //si le joueur n´a pas une meilleure Hand qu´au moment précédent, le joueur paye le pari actuel
                decision = 1;
            } else {
                decision =  jeu.pariActuel + 200; //sinon (sa valeur de Hand a augmenté), il augmente de 200 le pari actuel
            }*/
        }

        return decision;
    }


}
