class Oblig1A {
    public static void main (String Args []) {
        
        //Deklarere variablene 
        float RegnMaiMengde = 59;
        float RegnJuniMengde = 94;
        float RegnJuliMengde = 149;
        int RegnMaiDager = 13;
        int RegnJuniDager = 20;
        int RegnJuliDager = 21;

        //Regne sammen mengde regn og printe til terminal
        float RegnTotalMengde = RegnMaiMengde + RegnJuniMengde + RegnJuliMengde;
        System.out.println ("I l�pet av sommeren falt det en total mengde nedb�r p� " + RegnTotalMengde + "mm over Blindernomr�det.");

        //Regne gjennomsnittlig nedb�r per regndag og printe til terminal
        int RegnTotalDager = RegnMaiDager + RegnJuniDager + RegnJuliDager;
        float RegnGjennomsnitt = RegnTotalMengde / RegnTotalDager;
        System.out.println ("I l�pet av totalt " + RegnTotalDager + " nedb�rsdager i sommer blir dette et gjennomsnitt p� " + RegnGjennomsnitt + "mm nedb�r per regnv�rsdag.");

        //Regn ut prosent av gjennomsnittlig nedb�r i juli m�ned og print til terminal
        float RegnBlindernNormal = 81;
        float ProsentGjennomsnitt = (RegnJuliMengde * 100) / RegnBlindernNormal;
            System.out.println ("I forhold til normal nedb�rsmengde i juli har det i sommer kommet " + ProsentGjennomsnitt + " prosent nedb�r, mot normalt.");


            //Regne ut gjennomsnittlig nedb�r per regndag i hver m�ned
            float RegnMaiGjennomsnitt = RegnMaiMengde / RegnMaiDager;
            float RegnJuniGjennomsnitt = RegnJuniMengde / RegnJuniDager;
            float RegnJuliGjennomsnitt = RegnJuliMengde / RegnJuliDager;

            //Finne ut hvilken m�ned hadde v�teste nedb�rsdager og printe en tilbakemelding til terminalen
            if (RegnMaiGjennomsnitt > RegnJuniGjennomsnitt) {
                if (RegnMaiGjennomsnitt > RegnJuliGjennomsnitt) {
                System.out.println ("Mai var m�neden med h�yest gjennomsnittsnedb�r i sommer med " + RegnMaiGjennomsnitt + "mm nedb�r");
                }
            }            

            if (RegnJuniGjennomsnitt > RegnMaiGjennomsnitt) {
                if (RegnJuniGjennomsnitt > RegnJuliGjennomsnitt) {
                System.out.println ("Juni var m�neden med h�yest gjennomsnittsnedb�r i sommer med " + RegnJuniGjennomsnitt + "mm nedb�r");
                }
            }     
       
            if (RegnJuliGjennomsnitt > RegnJuniGjennomsnitt) {
                if (RegnJuliGjennomsnitt > RegnMaiGjennomsnitt) {
                System.out.println ("Juli var m�neden med h�yest gjennomsnittsnedb�r i sommer med " + RegnJuliGjennomsnitt + "mm nedb�r");
                }
            }            


     }

}