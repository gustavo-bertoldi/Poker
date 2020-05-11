import java.util.LinkedList;

public class Range {

    private boolean suited;
    private boolean paired;

    private int valeur1;
    private int valeur2;

    private LinkedList<Carte> cartesSurTable;

    private char type;

    private double oddsRaise;

    /*   Types
    Raise = 'r'
    Call/Raise = 'k'
    Call/fold = 'f'
     */

    public Range(Carte c1, Carte c2){
        valeur1 = c1.valeur;
        valeur2 = c2.valeur;
        paired = valeur1 == valeur2;
        suited = (c1.couleur == c2.couleur);
        type = 'f';
        setType(suited, paired, 0);
    }
    public Range(Hand hand){
        valeur1 = hand.getSurMain().getFirst().valeur;
        valeur2 = hand.getSurMain().get(1).valeur;

        this.cartesSurTable = hand.getSurTable();
        type = 'f';
        setType(isSuited(hand.getCartesHand(),1), isPaired(hand.getCartesHand()),1);
    }


    public char getType(){
        return type;
    }

    public int getValeur1(){
        return valeur1;
    }

    public int getValeur2() {
        return valeur2;
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
        if(moment==1){
            suited = compteurC>=3 ||compteurD>=3 || compteurP>=3 || compteurT>=3;
        }else if(moment==2){
            suited = compteurC>=4 || compteurD >=4 || compteurP>=4 || compteurT>=4;
        }else{
            suited = compteurC>=5 || compteurD >=5 || compteurP>=5 || compteurT>=5;
        }
        return suited;
    }

    public void setCartesSurTable(LinkedList<Carte> cartesSurTable) {
        this.cartesSurTable = cartesSurTable;
    }

    private void setType(boolean couleursEgaux, boolean pair, int moment){
        if(moment==0){
            double oddsValeur1 = valeur1/100.0;
            double oddsValeur2 = valeur2/100.0;
            if(pair){
                if(valeur1>5){
                    type = 'r';
                    oddsRaise = oddsValeur1+ 0.22;
                }else{
                    type='r';
                    oddsRaise = oddsValeur1 + 0.1;
                }
            }else if(couleursEgaux){
                if(valeur1==14){
                    type = 'r';
                    oddsRaise = oddsValeur2 + 0.15;
                }else if(valeur1==13){
                    if(valeur2>4){
                        type = 'r';
                        oddsRaise = 0.10 + oddsValeur2;
                    } else{
                        type = 'k';
                    }
                }else if(valeur1 == 12){
                    if(valeur2>=10){
                        type = 'r';
                        oddsRaise = oddsValeur1 +0.05;
                    }else if(valeur2 >=8){
                        type = 'k';
                    }
                } else if(valeur1 == 11){
                    if(valeur2>7){
                        type = 'r';
                        oddsRaise = oddsValeur1;
                    }
                } else if( valeur1 == 10){
                    if(valeur2>7){
                        type = 'k';
                        oddsRaise = oddsValeur2;
                    }
                } else if( valeur1 == 9){
                    if(valeur2 > 6){
                        type = 'k';
                        oddsRaise = 0.05;
                    }
                } else if(valeur1 == 8){
                    if(valeur2==7){
                        type = 'k';
                        oddsRaise = 0.05;
                    }
                }
            }else{
                if(valeur1==14){
                    if(valeur2 >= 10){
                        type = 'r';
                        oddsRaise = oddsValeur2 + 0.15;
                    } else if(valeur2 > 7){
                        type = 'k';
                    }
                }else if(valeur1==13){
                    if(valeur2>=9){
                        type = 'r';
                        oddsRaise = 0.10 + oddsValeur2;
                    }
                }else if(valeur1 == 12){
                    if(valeur2>=10){
                        type = 'k';
                        oddsRaise = oddsValeur1;
                    }
                } else if(valeur1 == 11){
                    if(valeur2>=7){
                        type = 'k';
                        oddsRaise = oddsValeur1;
                    }
                } else if( valeur1 == 10){
                    if(valeur2==9){
                        type = 'k';
                        oddsRaise = oddsValeur2;
                    }
                } else if( valeur1 == 9){
                    if(valeur2 == 8){
                        type = 'k';
                        oddsRaise = 0.05;
                    }
                } else if(valeur1 == 8){
                    if(valeur2==7){
                        type = 'k';
                        oddsRaise = 0.05;
                    }
                }
            }
        }else if(moment>=1){
            type ='k';
            if(pair||couleursEgaux){
                if(valeur1>=10) {
                    type = 'r';
                }else{
                    type ='k';
                }
            }else{
                if(valeur1>=12 && valeur1>=cartesSurTable.get(0).valeur){
                    type = 'r';
                }else if(valeur1>8 && valeur1>=cartesSurTable.get(0).valeur){
                    type = 'k';
                }else{
                    type = 'f';
                }
            }

        }

    }





}
