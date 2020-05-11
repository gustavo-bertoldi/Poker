import java.util.LinkedList;

public class Intelligence{

    private int niveau;
    private int momentJeu;

    private boolean parieDerniereTournee;

    private Hand hand;
    private Range range;

    private double limitePlayRandomly;
    private double typeDecisionTournee;

    /*              IDÉE DERRIERE LIMITE ET TYPEDECISION
    La limitePlayRandomly est un seuil en dessous du quel le joueur prend ses decisions alléatoirement
    cette limite est aussi grande que le niveau du joueur est petit
    À la fin de chaque tournee, on définit le type de decision que le joueur va prendre lors de la prochaine tournee
    si typeDecisionTournee < limitePlayRandomly, le joueur utilise l'intelligence alleatoire pour prendre ses decisions sinon,
    il utilise celle qui correspond a son niveau;
     */

    public Intelligence(int niveau, Hand hand){
        this.niveau = niveau;
        this.hand = hand;
        range = new Range(hand);
        momentJeu = 0;
        parieDerniereTournee = false;
        if(niveau==0 ){
            limitePlayRandomly = 1;
        }else if(niveau == 1){
            limitePlayRandomly = 0.4;
        } else if(niveau == 2){
            limitePlayRandomly = 0.13;
        }
    }

    public void setRange(int moment){
        LinkedList<Carte> cartesMiseAJour = new LinkedList<>();
        if(moment == 0){
            range.resetRange(hand);
            cartesMiseAJour.addAll(hand.getSurMain());
            range.mettreAJour(moment,cartesMiseAJour, hand.getValeurHandSurMain());
        }else if(moment ==1){
            cartesMiseAJour.addAll(hand.getApresFlop());
            range.mettreAJour(moment, cartesMiseAJour, hand.getValeurHandApresFlop());
        }else if(moment == 2){
            cartesMiseAJour.addAll(hand.getApresTurn());
            range.mettreAJour(moment, cartesMiseAJour, hand.getValeurHandApresTurn());
        }else if(moment ==3){
            cartesMiseAJour.addAll(hand.getApresRiver());
            range.mettreAJour(moment, cartesMiseAJour, hand.getValeurHandApresRiver());
        }

    }

    public void setParieDerniereTournee(boolean parie){
        parieDerniereTournee = parie;
    }
    /*  Méthode à être appelée a chaque fin de tournee, pour determiner comment le joueur va jouer la prochaine tournee
     */
    public void setTypeDecisionTournee(){
        typeDecisionTournee = Math.random();
        if(parieDerniereTournee){
            typeDecisionTournee = typeDecisionTournee*2;
        }
    }

    public static int getDecision(Jeu jeu, Joueur joueur, int niveau){
        if(niveau==0){
            return intelligenceAleatoire(jeu,joueur);
        }
        else if(niveau==1){
            return intelligenceNiveau1(jeu,joueur);
        }
        else {
            return intelligenceNiveau2(jeu, joueur);
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
        int decision;
        int valeurMoment1=0;
        int valeurMoment2=0;
        int valeurHand = joueur.getHand().getValeurHandApresRiver();

        if (!jeu.flop) { //quand le joueur a seulement ses 2 cartes à lui

           if (valeurHand > 14) { //si le joueur a au moins une paire, il va doubler le pari
                decision =  2 * jeu.pariActuel;
           }

           else if (joueur.bigBlind) { //si le joueur est le Big Blind, il va toujours passer au moment 1
               decision = 1;
           }

           else if (jeu.pariActuel < (1 / 6) * joueur.getArgent()) { //si le pari actuel est peu important en comparaison à l´argent du joueur, le joueur paye toujours le pari
                decision = 1;

           }

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

            if (joueur.getHand().getValeurHandApresFlop() < 60) { //si le joueur a une paire de 5 ou une Hand moins importante, il paye le pari actuel. Si il a une Hand plus importante, il augmente de 500 le pari actuel.
                decision = 1;
            } else {
                decision = jeu.pariActuel + 500;
            }

            valeurMoment1 = joueur.getHand().getValeurHandApresFlop(); //variable intermédiaire pour stocker la valeur du Hand au moment 1
        }

        else if (!jeu.river) { //quand il y a 4 cartes révélées
            if(joueur.allIn){
                decision=1;
            }

            else if (joueur.getHand().getValeurHandApresTurn() == valeurMoment1) { //si le joueur n´a pas une meilleure Hand qu´au moment précédent, le joueur paye le pari actuel
                decision = 1;

            } else {
                decision = (jeu.pariActuel + 200); //sinon (sa valeur de Hand a augmenté), il augmente de 200 le pari actuel
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
                decision =  jeu.pariActuel + 100; //sinon (sa valeur de Hand a augmenté), il augmente de 200 le pari actuel
            }
        }

        return decision;
    }
    
    private static int intelligenceNiveau2 (Jeu jeu, Joueur joueur){
        int decision;
        int valeurMoment2=0;
        int valeurHand = joueur.getHand().getValeurHandApresRiver();

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
            }
        }

        return decision;
    }
    /*
    Reset l'intelligence au bout d'une tournee
     */
    public void reset(){
        setParieDerniereTournee(false);
        setTypeDecisionTournee();
    }
    // si typeRange == k et le pari est petit, payer, sinon fold
    // si typeRange == m,
}
