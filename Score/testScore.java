import java.io.*;
import java.io.*;
import java.util.*;

public class testScore{
	 public static void main(String[] args) throws IOException{
        Score a= new Score("kari",200,"12-04");
        Score b = new Score ( "Carlo",1000,"11-06");
        Score c = new Score ( "Carlo",1005,"11-06");
        LinkedList<Score> HighScore= new LinkedList<>();
        HighScore.add(a);
        HighScore.add(c);
        HighScore.add(b);
        Score.organiser(HighScore);
        Score.createfile(HighScore);

    }
	}
