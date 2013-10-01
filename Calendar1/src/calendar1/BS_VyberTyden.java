/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package calendar1;

/**
 *
 * @author jey
 */
public class BS_VyberTyden extends BS_Base{

    BS_Tyden [] polozky;
    int maxPole=30;
    int pocetAktualizace=5;

    public BS_VyberTyden(){
        polozky=new BS_Tyden[5];
        active=1;
        firstSeen=1;
        for(int i=0; i<5; i++){
            polozky[i]=new BS_Tyden();
            polozky[i].st="a"+i;
        }
    }

    public void setVert(){
        pocetSeen=3;
        vert=true;
        if(polozky.length>3)
            lastSeen=(firstSeen+2)%polozky.length;
        else
            lastSeen=firstSeen+polozky.length-1;
        countPocetShown();
    }

    public void setHor(){
        vert=false;
    }

    public boolean posunDolu(){
        if(active<maxShownIndex)
            active++;
        else{
            if(lastSeen==polozky.length-1){
                return false;
            }
            firstSeen++;
            set();
        }
        return true;
    }

    public boolean posunNahoru(){
        if(active>0)
            active--;
        else{
            if(firstSeen==0){
                return false;
            }
            firstSeen--;
            set();
        }
        return true;
    }
}
