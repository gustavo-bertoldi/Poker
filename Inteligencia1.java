import java.util.ArrayList;
import java.util.LinkedList;


public class Inteligencia1{
	
 private int niveau;
 private int decision;
 private Joueur joueur;
 
 
 public Inteligencia1 (Joueur j){
	 
	 niveau = 0;
	 decision = 0;
	 joueur = j;
	 
 }
 
 public int getDecision(int pariActuel){
	 
	if(joueur.jeu().moment() == 0){ //quand le joueur a seulement ses 2 cartes à lui
		
		if(joueur.bigBlind() == true){ //si le joueur est le Big Blind, il va toujours passer au moment 1
			if(pariActuel == 0) { //si personne ne paye aucune somme en plus de l´argent du Big Blind (quota pour jouer), le joueur ne paye rien de plus.
				return 0; 
			} else{ //si quelqu´un paye en plus, le Big Blind paye le pari
				return 1; 
			}
		} 
		
	if(joueur.getHand().getValeurHand() > 14){ //si le joueur a au moins une paire, il va doubler le pari
		return 2*pariActuel;
	}
	
	if(pariActuel < (1/6)*joueur.getArgent()){ //si le pari actuel est peu important en comparaison à l´argent du joueur, le joueur paye toujours le pari
		return 1;
	
	} else{ //si le pari actuel est assez important
		
		if(joueur.getHand().getValeurHand() >= 8){ //si le joueur a une carte supérieure ou égale à 8 (et pas de paires ni rien meilleur), il paye le parie. Sinon, il arrête de jouer.
			return 1; 
		} else{
			return -1;
		}	
	}	
}
}

	if(joueur.jeu().moment() == 1){ //quand les 3 premières cartes sont révélées
		
		if(joueur.getHand().getValeurHand() < 8 && pariActuel > 0){ //si le joueur était le Big Blind mais il n´a que des cartes inférieures à 8 (et pas de paires ni rien meilleur), il arrête de jouer
			return -1 ;
		}
		
		if(joueur.getHand().getValeurHand() < 60){ //si le joueur a une paire de 5 ou une Hand moins importante, il paye le pari actuel. Si il a une Hand plus importante, il augmente de 500 le pari actuel.
			return 1;
		} else {
			return pariActuel+500;
		}
		
		int valeurMoment1 = joueur.getHand().getValeurHand(); //variable intermédiaire pour stocker la valeur du Hand au moment 1
	}
	
		if(joueur.jeu().moment() == 2){ //quand il y a 4 cartes révélées
			
			if(joueur.getHand().getValeurHand() == valeurMoment1){ //si le joueur n´a pas une meilleure Hand qu´au moment précédent, le joueur paye le pari actuel
				return 1;
			
			} else {
				return (pariActuel+200); //sinon (sa valeur de Hand a augmenté), il augmente de 200 le pari actuel
			}
			int valeurMoment2 = joueur.getHand().getValeurHand(); //variable intermédiaire pour stocker la valeur du Hand au moment 2
		}
	
		if(joueur.jeu().moment() == 3){ //quand toutes les cartes ont été révélées
			
			if(joueur.getHand().getValeurHand() == valeurMoment2){ //si le joueur n´a pas une meilleure Hand qu´au moment précédent, le joueur paye le pari actuel
				return 1;
			} else {
				return pariActuel+100; //sinon (sa valeur de Hand a augmenté), il augmente de 200 le pari actuel
			}
		}
		
	}
	
	
		
	

