interface Pille {//oppretter interfaces for de forskjellige legemiddeltypene
    int antall();

    //returnerer antall piller i et brett
}

interface Injeksjon {
    int dose();

    //returnerer antall mg i en dose
}

interface Liniment {
    int mengde();

    //returnerer mengden i cm^3
}

class Legemiddel {//generelle klassen som vi utfyller der det trengs med nye variabler etc.
    protected String navn, form;
    protected char type;
    protected int nummer, pris;

    public void skriv(){//Skriver ut aktuell info.
        System.out.print("Navn: " + navn + " Form: " + form + " Type: " + type + " Nummer: " + nummer + " Pris: " + pris + " ");
    }
	
}

class LegemiddelA extends Legemiddel {//to klasser med ekstra variabler
    protected int styrke;
}

class LegemiddelB extends Legemiddel {
    protected int vanedannende;
}


class LegemiddelAInjeksjon extends LegemiddelA implements Injeksjon {//oppretter 9 forskjellige subklasser med variablene de skal ha
    private int dose;

    LegemiddelAInjeksjon(int nummer, String navn,String form, char type, int pris, int mengde, int styrke) {
        this.navn = navn;
        this.nummer = nummer;
	this.form = form;
	this.type = type;
        this.pris = pris;	
        this.styrke = styrke;
        this.dose = mengde;
    }
    public void skriv(){//metoder som skriver ut/returnerer det vi spør om
        super.skriv();
        System.out.println("Styrke : " + styrke + " dose: " + dose);
    }

    public int dose(){
	return dose;
    }
}

class LegemiddelAPille extends LegemiddelA implements Pille{
    private int antall;
    
    LegemiddelAPille(int nummer, String navn,String form, char type, int pris, int mengde, int styrke) {
        this.navn = navn;
        this.nummer = nummer;
	this.form = form;
	this.type = type;
        this.pris = pris;
        this.styrke = styrke;
        this.antall = mengde;
    }
    
    
    public void skriv(){
        super.skriv();
        System.out.println("Styrke : " + styrke + " antall piller: " + antall);
    }

    public int antall(){
	return antall;
    }
}

class LegemiddelALiniment extends LegemiddelA implements Liniment{
    private int mengde;

    LegemiddelALiniment(int nummer, String navn,String form, char type, int pris, int mengde, int styrke) {
        this.navn = navn;
        this.nummer = nummer;
	this.form = form;
	this.type = type;
        this.pris = pris;
        this.styrke = styrke;
        this.mengde = mengde;
    }
    public void skriv(){
        super.skriv();
        System.out.println("Styrke : " + styrke + " mengde: " + mengde);
    }	
    public int mengde(){
	return mengde;
    }
}

class LegemiddelBInjeksjon extends LegemiddelB implements Injeksjon{
    private int dose;

    LegemiddelBInjeksjon(int nummer, String navn,String form, char type, int pris, int mengde, int styrke) {
        this.navn = navn;
        this.nummer = nummer;
	this.form = form;
	this.type = type;
        this.pris = pris;
        this.vanedannende = styrke;
        this.dose = mengde;
    }
    public void skriv(){
        super.skriv();
        System.out.println("Styrke : " + vanedannende + " dose: " + dose);
    }
    public int dose(){
	return dose;
    }
}

class LegemiddelBPille extends LegemiddelB implements Pille{
    private int antall;

    LegemiddelBPille(int nummer, String navn,String form, char type, int pris, int mengde, int styrke) {
        this.navn = navn;
        this.nummer = nummer;
	this.form = form;
	this.type = type;
        this.pris = pris;
        this.vanedannende = styrke;
        this.antall = mengde;
    }
    public void skriv(){
        super.skriv();
        System.out.println("Styrke : " + vanedannende + "antall piller: " + antall);
    }	
    public int antall(){
	return antall;
    }
}

class LegemiddelBLiniment extends LegemiddelB implements Liniment{
    private int mengde;

    LegemiddelBLiniment(int nummer, String navn,String form, char type, int pris, int mengde, int styrke) {
        this.navn = navn;
        this.nummer = nummer;
	this.form = form;
	this.type = type;
        this.pris = pris;
        this.vanedannende = styrke;
        this.mengde = mengde;
    }
    public void skriv(){
        super.skriv();
        System.out.println("Styrke : " + vanedannende + " mengde: " + mengde);
    }
    public int mengde(){
	return mengde;
    }
}

class LegemiddelInjeksjon extends Legemiddel implements Injeksjon{
    private int dose;

    LegemiddelInjeksjon(int nummer, String navn,String form, char type, int pris, int mengde) {
        this.navn = navn;
        this.nummer = nummer;
	this.form = form;
	this.type = type;
        this.pris = pris;
        this.dose = mengde;
    }
    public void skriv(){
        super.skriv();
        System.out.println("dose: " + dose);
    }
    public int dose(){
	return dose;
    }
}

class LegemiddelPille extends Legemiddel implements Pille{
    private int antall;

    LegemiddelPille(int nummer, String navn,String form, char type, int pris, int mengde) {
        this.navn = navn;
        this.nummer = nummer;
	this.form = form;
	this.type = type;
        this.pris = pris;
        this.antall = mengde;
    }
    public void skriv(){
        super.skriv();
        System.out.println("antall piller: " + antall);
    }
    public int antall(){
	return antall;
    }
}

class LegemiddelLiniment extends Legemiddel implements Liniment{
    private int mengde;

    LegemiddelLiniment(int nummer, String navn,String form, char type, int pris, int mengde) {
        this.navn = navn;
        this.nummer = nummer;
	this.form = form;
	this.type = type;
        this.pris = pris;
        this.mengde = mengde;
    }
    public void skriv(){
        super.skriv();
        System.out.println("mengde: " + mengde);
    } 
    public int mengde(){
	return mengde;
    }
}
