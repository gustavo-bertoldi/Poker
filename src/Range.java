import java.util.Collections;
import java.util.LinkedList;

public class Range {

    private boolean suited;
    private boolean paired;

    private int valeurCarteMain1;
    private int valeurCarteMain2;

    private LinkedList<Carte> cartesSurTable;
    private LinkedList<Carte> cartesVisibles;

    private int valeurHandMoment;

    private char type;

    protected double oddsRaise;
    /*   Types -> doit être calculé à chaque fin de tourneeDeParis
    Monster = 'm'
    Raise = 'r'
    Call/Raise = 'k'
    Call/fold = 'f'
     */

    public Range(LinkedList<Carte> surMain, LinkedList<Carte> surTable){
        cartesSurTable = new LinkedList<>();
        valeurCarteMain1 = surMain.getFirst().valeur;
        valeurCarteMain2 = surMain.get(1).valeur;

        setCartesSurTable(surTable);
        cartesVisibles = new LinkedList<>();
        type = 'f';
    }
    /*
    Methode a etre appellee a chaque fin de tournee
     */
    public void resetRange(LinkedList<Carte> surMain, LinkedList<Carte> surTable){
        valeurCarteMain1 = surMain.getFirst().valeur;
        valeurCarteMain2 = surMain.get(1).valeur;
        setCartesSurTable(surTable);
        valeurHandMoment = 0;
        cartesVisibles = new LinkedList<>();
        type = 'f';
    }

    public char getType(){
        return type;
    }

    public double getOddsRaise() {
        return oddsRaise;
    }

    public boolean isPaired(LinkedList<Carte> handMoment) {
        for (int i = 2; i <= 14; i++) {
            int q = 0;
            for (Carte c : handMoment) {
                if (c.valeur == i) {
                    q++;
                }
            }
            if (q == 2) {
                paired = true;
                break;
            }
        }
        return paired;
    }

    public boolean isSuited(LinkedList<Carte> handMoment, int moment){
        boolean suited = false;
        int compteurT = 0;
        int compteurC = 0;
        int compteurD = 0;
        int compteurP = 0;
        for (Carte c: handMoment ) {
            if(c.couleur=='t'){
                compteurT++;
            }else if(c.couleur =='c'){
                compteurC++;
            }else if(c.couleur=='d'){
                compteurD++;
            }else{
                compteurP++;
            }
        }
        if(moment==0){
            suited = compteurC==2 || compteurD ==2 || compteurP==2 || compteurT==2;
        }else if(moment==1){
            suited = compteurC>=3 ||compteurD>=3 || compteurP>=3 || compteurT>=3;
        }else if(moment==2){
            suited = compteurC>=4 || compteurD >=4 || compteurP>=4 || compteurT>=4;
        }else{
            suited = compteurC>=5 || compteurD >=5 || compteurP>=5 || compteurT>=5;
        }
        return suited;
    }

    public boolean isTableSuited(LinkedList<Carte> cartesVisibles, int moment){
        return isSuited(cartesVisibles, moment);
    }

    public void setCartesSurTable(LinkedList<Carte> cartesSurTable) {
        this.cartesSurTable.addAll(cartesSurTable);
    }

    public void mettreAJour(int moment, LinkedList<Carte> handMoment, int valeurHandMoment){
        this.valeurHandMoment = valeurHandMoment;
        setType(isSuited(handMoment, moment), isPaired(handMoment), moment);

    }

    public void setType(boolean couleursEgaux, boolean pair, int moment){
        oddsRaise=0;
        type ='f';
        if(moment==0){
            double oddsValeur1 = valeurCarteMain1/100.0;
            double oddsValeur2 = valeurCarteMain2/100.0;
            if(pair){
                if(valeurCarteMain1 > 12){
                    type = 'm';
                    oddsRaise = oddsValeur1 + 0.3;
                }
                if(valeurCarteMain1>5){
                    type = 'r';
                    oddsRaise = oddsValeur1+ 0.22;
                }else{
                    type='r';
                    oddsRaise = oddsValeur1 + 0.1;
                }
            }else if(couleursEgaux){
                if(valeurCarteMain1==14){
                    if(valeurCarteMain2 >10){
                        type ='m';
                        oddsRaise = oddsValeur2 +0.2;
                    }else {
                        type = 'r';
                        oddsRaise = oddsValeur2 + 0.15;
                    }
                }else if(valeurCarteMain1==13) {
                    if(valeurCarteMain2>10){
                        type = 'm';
                        oddsRaise = 0.12 + oddsValeur2;
                    }
                    else if(valeurCarteMain2>4){
                        type = 'r';
                        oddsRaise = 0.10 + oddsValeur2;
                    } else{
                        type = 'k';
                    }
                }else if(valeurCarteMain1 == 12){
                    if(valeurCarteMain2>=10){
                        type = 'r';
                        oddsRaise = oddsValeur1 +0.05;
                    }else if(valeurCarteMain2 >=8){
                        type = 'k';
                    }
                } else if(valeurCarteMain1 == 11){
                    if(valeurCarteMain2>7){
                        type = 'r';
                        oddsRaise = oddsValeur1;
                    }
                } else if( valeurCarteMain1 == 10){
                    if(valeurCarteMain2>7){
                        type = 'k';
                        oddsRaise = oddsValeur2;
                    }
                } else if( valeurCarteMain1 == 9){
                    if(valeurCarteMain2 > 6){
                        type = 'k';
                        oddsRaise = 0.05;
                    }
                } else if(valeurCarteMain1 == 8){
                    if(valeurCarteMain2==7){
                        type = 'k';
                        oddsRaise = 0.05;
                    }
                }
            }else{
                if(valeurCarteMain1==14){
                    if(valeurCarteMain2==13){
                        type ='m';
                        oddsRaise = oddsValeur2 + 0.20;
                    }else if(valeurCarteMain2 >= 10){
                        type = 'r';
                        oddsRaise = oddsValeur2 + 0.15;
                    } else if(valeurCarteMain2 > 7){
                        type = 'k';
                    }
                }else if(valeurCarteMain1==13){
                    if(valeurCarteMain2>=9){
                        type = 'r';
                        oddsRaise = 0.10 + oddsValeur2;
                    }
                }else if(valeurCarteMain1 == 12){
                    if(valeurCarteMain2>=10){
                        type = 'k';
                        oddsRaise = oddsValeur1;
                    }
                } else if(valeurCarteMain1 == 11){
                    if(valeurCarteMain2>=7){
                        type = 'k';
                        oddsRaise = oddsValeur1;
                    }
                } else if( valeurCarteMain1 == 10){
                    if(valeurCarteMain2==9){
                        type = 'k';
                        oddsRaise = oddsValeur2;
                    }
                } else if( valeurCarteMain1 == 9){
                    if(valeurCarteMain2 == 8){
                        type = 'k';
                        oddsRaise = 0.05;
                    }
                } else if(valeurCarteMain1 == 8){
                    if(valeurCarteMain2==7){
                        type = 'k';
                        oddsRaise = 0.05;
                    }
                }
            }
        }else if(moment==1){
            cartesVisibles.add(cartesSurTable.get(0));
            cartesVisibles.add(cartesSurTable.get(1));
            cartesVisibles.add(cartesSurTable.get(2));
            cartesVisibles.sort(Collections.reverseOrder());
            if(valeurHandMoment >=200) {
                type = 'm';
                oddsRaise = valeurHandMoment/2000.0 + 0.2;
                if(oddsRaise>0.5){
                    oddsRaise = 0.55;
                }
            }else if(pair||couleursEgaux){
                if(valeurCarteMain1>=10) {
                    type = 'r';
                    oddsRaise = valeurCarteMain1/100.0 + 0.1;
                }else{
                    type ='k';
                }
            }else{
                if(valeurCarteMain1>=12 && valeurCarteMain1>=cartesVisibles.get(0).valeur){
                    type = 'r';
                    oddsRaise = valeurCarteMain1/100.0;
                }else if(valeurCarteMain1>8 && valeurCarteMain1>=cartesVisibles.get(0).valeur){
                    type = 'k';
                }
            }
        } else if(moment==2){
            cartesVisibles.add(cartesSurTable.get(3));
            cartesVisibles.sort(Collections.reverseOrder());
            if(valeurHandMoment >=500) {
                type = 'm';
                oddsRaise = valeurHandMoment/2000.0 + 0.2;
                if(oddsRaise>0.5){
                    oddsRaise = 0.6;
                }
            }else if(valeurHandMoment >= 120||couleursEgaux){
                if(valeurCarteMain1>=10) {
                    type = 'r';
                    oddsRaise = valeurCarteMain1/100.0 + 0.1;
                }else{
                    type ='k';
                }
            }else{
                if(valeurCarteMain1>=12 && valeurCarteMain1>=cartesVisibles.get(0).valeur){
                    type = 'k';
                    oddsRaise = valeurCarteMain1/100.0;
                }
            }
        }else{
            cartesVisibles.add(cartesSurTable.get(4));
            cartesVisibles.sort(Collections.reverseOrder());
            if(valeurHandMoment >=14000) {
                type = 'm';
                oddsRaise = valeurHandMoment/2000.0 + 0.2;
                if(oddsRaise>0.5){
                    oddsRaise = 0.65;
                }
            }else if(valeurHandMoment >= 120){
                    type = 'r';
                    oddsRaise = valeurCarteMain1/100.0 + 0.1;
                }else{
                if(valeurCarteMain1>=12 && valeurCarteMain1>=cartesVisibles.get(0).valeur){
                    type = 'k';
                }
            }
        }
        if(!couleursEgaux && isTableSuited(cartesVisibles,moment)){
            oddsRaise = oddsRaise-0.20;
        }

    }
}