public class Range {

    private boolean suited;
    private boolean paired;

    private int valeur1;
    private int valeur2;


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
        setType(suited, paired);
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

    private void setType(boolean couleursEgaux, boolean pair){
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
    }





}
