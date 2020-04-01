public class Intel1 {
	private int niveau;
	private Joueur joueur;
	
	public Intel1( Joueur j){
		niveau = 1;
		joueur = j;
	}
	
	public boolean Jouable(){
		int v=0;
		v= j.getValeurHand();
			if (v<15){
			return false // sortir si on a pas mieu que hotaur as 
		}
		else {
		 return true 
		}
	}
	
	public void jouer( int pariActuel){
		int v= j.getValeurHand();
		if (j.getArgent()>= pariActuel){
			if ( j.Jouable()== true){
				if( v<14001){
					j.parier(pariActuel)
				}
				else{
					j.parier(pariActuel+100)
					}
				}
		else {
			// joueur ne joue pas ce tour et couche 
			}
		}
	}
}
