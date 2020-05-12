
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Score implements Comparable {
    protected String nom;
    protected int score;

    public Score (String nom, int score){
    }

    public int getScore(){
        return score;
    }

    public static void ajouterScore(String nom, int score) throws Exception {
        mettreAJourCSV(new Score(nom,score));
    }

    public static void mettreAJourCSV(Score newScore) throws Exception {
        LinkedList<Score> scores = new LinkedList<>();
        ecrireScore(newScore.nom,newScore.score);
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("./highscores/highscores.csv")));
        while (input.readLine()!=null){
            String[] values = input.readLine().split(";");
            scores.add(new Score(values[0],Integer.parseInt(values[1])));
        }
        input.close();
        scores.sort(Collections.reverseOrder());
        while(scores.size()>20){
            scores.removeLast();
        }

        File file = new File("./highscores/highscores.csv");
        file.delete();

        for(Score s : scores){
            ecrireScore(s.nom,s.score);
        }
    }

    public static void ecrireScore(String nom,int score) throws IOException {
        String date = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).format(new Date());
        BufferedWriter writer = new BufferedWriter(new FileWriter("./highscores/highscores.csv",true));
        writer.newLine();
        writer.write(nom+";"+score+";"+date+";");
        writer.close();
    }

    public static void main (String[] args) throws Exception {
        Score.ajouterScore("Gustavo", 300);
        Score.ajouterScore("Montse", 350);
    }

    public int compareTo(Object s) {
        Score score = (Score)s;
        if(this.score == score.getScore()){
            return 0;
        }
        else if (this.score > score.getScore()){
            return 1;
        }
        else{
            return -1;
        }
    }
}