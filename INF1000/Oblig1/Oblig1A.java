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
        System.out.println ("I løpet av sommeren falt det en total mengde nedbør på " + RegnTotalMengde + "mm over Blindernområdet.");

        //Regne gjennomsnittlig nedbør per regndag og printe til terminal
        int RegnTotalDager = RegnMaiDager + RegnJuniDager + RegnJuliDager;
        float RegnGjennomsnitt = RegnTotalMengde / RegnTotalDager;
        System.out.println ("I løpet av totalt " + RegnTotalDager + " nedbørsdager i sommer blir dette et gjennomsnitt på " + RegnGjennomsnitt + "mm nedbør per regnværsdag.");

        //Regn ut prosent av gjennomsnittlig nedbør i juli måned og print til terminal
        float RegnBlindernNormal = 81;
        float ProsentGjennomsnitt = (RegnJuliMengde * 100) / RegnBlindernNormal;
            System.out.println ("I forhold til normal nedbørsmengde i juli har det i sommer kommet " + ProsentGjennomsnitt + " prosent nedbør, mot normalt.");


            //Regne ut gjennomsnittlig nedbør per regndag i hver måned
            float RegnMaiGjennomsnitt = RegnMaiMengde / RegnMaiDager;
            float RegnJuniGjennomsnitt = RegnJuniMengde / RegnJuniDager;
            float RegnJuliGjennomsnitt = RegnJuliMengde / RegnJuliDager;

            //Finne ut hvilken måned hadde våteste nedbørsdager og printe en tilbakemelding til terminalen
            if (RegnMaiGjennomsnitt > RegnJuniGjennomsnitt) {
                if (RegnMaiGjennomsnitt > RegnJuliGjennomsnitt) {
                System.out.println ("Mai var måneden med høyest gjennomsnittsnedbør i sommer med " + RegnMaiGjennomsnitt + "mm nedbør");
                }
            }            

            if (RegnJuniGjennomsnitt > RegnMaiGjennomsnitt) {
                if (RegnJuniGjennomsnitt > RegnJuliGjennomsnitt) {
                System.out.println ("Juni var måneden med høyest gjennomsnittsnedbør i sommer med " + RegnJuniGjennomsnitt + "mm nedbør");
                }
            }     
       
            if (RegnJuliGjennomsnitt > RegnJuniGjennomsnitt) {
                if (RegnJuliGjennomsnitt > RegnMaiGjennomsnitt) {
                System.out.println ("Juli var måneden med høyest gjennomsnittsnedbør i sommer med " + RegnJuliGjennomsnitt + "mm nedbør");
                }
            }            


     }

}