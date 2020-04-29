public class intelligenceNiveau2 {



private static int intelligenceNiveau2 (Jeu jeu, Joueur joueur){
        int decision;
        int valeurMoment2=0;
        int valeurHand = joueur.getHand().getValeurHand();

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
                if (joueur.getHand().getValeurHand() >= 8) { //si le joueur a une carte supérieure ou égale à 8 (et pas de paires ni rien meilleur), il paye le parie. Sinon, il arrête de jouer.
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

            if (joueur.getHand().getValeurHand() < 60) { //si le joueur a une paire de 5 ou une Hand moins importante 
                
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

            else if (joueur.getHand().getValeurHand() < 230 && jeu.pariActuel > 0) { // si le joueur n´a pas au moins 2 paires, il arrête de jouer
				decision = -1;
					
			} else if (joueur.getHand().getValeurHand() < 650){ //sinon (le joueur a au moins deux paires), si les 2 paires sont moyennement fortes, il paye le pari actuel 
				decision = 1; 

            } else { //si le joueur a au moins 2 paires fortes, il augmente de 200 le pari actuel
                decision = (jeu.pariActuel + 200);  
            }
            valeurMoment2 = joueur.getHand().getValeurHand(); //variable intermédiaire pour stocker la valeur du Hand au moment 2
        }

        else { //quand toutes les cartes ont été révélées
            if(joueur.allIn){
                decision=1;
            }
            else if (joueur.getHand().getValeurHand() == valeurMoment2) { //si le joueur n´a pas une meilleure Hand qu´au moment précédent, le joueur paye le pari actuel
                decision = 1;
            } else {
                decision =  jeu.pariActuel + 200; //sinon (sa valeur de Hand a augmenté), il augmente de 200 le pari actuel
            }
        }

        return decision;
    }
    
    }
    
 
