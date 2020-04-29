import java.io.*;
import java.nio.*;
import java.util.*;

public class Score implements Comparable{
	private String nom;
	private int score;
	private String date;

	public Score(String nom, int score,String date){
		this.nom =nom;
		this.score =score;
		this.date =date;
	}

	public int getScore(){
		return score;
	}

	public int compareTo(Object s){
		Score score1 = (Score)s; // permet de savoir la position du score  par rapport a un autre de la liste
		if (score1.getScore()>this.score){
			return -1;
		}
		if (score1.getScore()==this.score){
			return 0;
		}
		else{
			return 1;
		}
	}
	public static void organiser(LinkedList<Score> HighScore){
		HighScore.sort(Collections.reverseOrder());
	}

	public String toString(){
		return (nom +" "+score+" " +date);
	}

	public static void createfile(LinkedList<Score> HighScore) throws IOException{
		File file = new File("/test.txt");// introduire le chemain du fichier ou se sauvgardera le fichier.txt et le nomer avec .txt
		FileWriter TabHS = new FileWriter(file);
		for(Score s:HighScore){
			TabHS.write(s.toString()+"\n");
		}
		TabHS.close();
	}
}
