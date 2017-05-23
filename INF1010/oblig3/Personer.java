// 2014.02.06: Fjernet "nasjonale" tegn
// rettet startindeks i samlerAv-kallene

public class Personer {

    
    private String[] oblig3Navn = {
	    "Karl", "Kenneth", "Kim", "Kjell", "Kjetil", "Knut",
	    "Simen", "Sindre", "Siri", "Stian", "Thanh", "Thomas",
	    "Elena", "Endre", "Erik", "Erlend", "Espen", "Fredrik",
	    "Gard", "Halvard", "Hanna", "Hans", "Henning", "Henrik",
	    "William", "Eystein", "Gandalf", "Aasmund",
	    "Herman", "Haakon", "Ingrid", "Jan", "Jarl", "Joakim",
	    "Alex", "Alexander", "Alexandra", "Anders", "Andreas", "Anton",
	    "Johan", "Johannes", "Jon", "Jonas", "Julia", "Georg",
	    "Konstantin", "Kristine", "Kristoffer", "Lars", "Lasse", "Linda",
	    "Line", "Mads", "Magnus", "Maria", "Marius", "Marte",
	    "Audun", "Bendik", "Benjamin", "Ursus", "Bo", "Baard",
	    "Martin", "Martine", "Mathias", "Mats", "Minh", "Mohammed",
	    "Ole", "Peter", "Petter", "Robin", "Rune", "Sebastian",
	    "Morten", "Natalia", "Nicolay", "Odd", "Ola", "Olav",
	    "Tom", "Tor", "Torarin", "Torgeir", "Vegard", "Vilde",
	    "Carl", "Christian", "Christine", "Christoffer", "Daniel", "Eirik"
    } ;

    

    private int antall = oblig3Navn.length;
    private int nestePersonNr = 0;

    private Person[] persTabell = new Person[antall];

    Personer() {

	// oppretter personene. Forutsetter at det finnes en konstruktÃ¸r
	// i Person som har personens navn som parameter (String)
	
	for (int i=0; i<antall; i++)
	    persTabell[i] = new Person(oblig3Navn[i]);

	// Setter opp kjennskapsforbindelsene

	for (int i=1; i<antall; i++)
	    for (int j=1; j<i+1; j++)
		persTabell[i].blirKjentMed(persTabell[i-j]);

	for (int i=0; i<20; i++)
	    for (int j=1; j<15; j++)
		persTabell[i].blirKjentMed(persTabell[i+j]);

	// Oppretter noen uvennskap

	for (int i=7; i<antall; i++) {
	    persTabell[i].blirUvennMed(persTabell[i-7]);
	    persTabell[i].blirUvennMed(persTabell[i-6]);
	    if (i > 24) persTabell[i].blirUvennMed(persTabell[i-20]);
	    if (i > 34) persTabell[i].blirUvennMed(persTabell[i-30]);
	    if (i > 62) persTabell[i].blirUvennMed(persTabell[i-60]);
	    if (i > 92) persTabell[i].blirUvennMed(persTabell[i-90]);
	}

	// forelskelser og sammen med

	for (int i=6; i<antall; i=i+2) 
	    if (persTabell[i].erKjentMed(persTabell[i-2]))
		persTabell[i].blirForelsketI(persTabell[i-2]);

	for (int i=7; i<antall; i=i+3) 
	    if (persTabell[i].erKjentMed(persTabell[i-2]))
		persTabell[i].blirSammenMed(persTabell[i-2]);
		
	// oppretter interesser for gaver

	for (int i=0; i<antall; i=i+3)
	    persTabell[i].samlerAv("bok", 12);
	for (int i=1; i<antall; i=i+3)
	    persTabell[i].samlerAv("vin", 5);
	for (int i=2; i<antall; i=i+3)
	    persTabell[i].samlerAv("cd", 7);
	for (int i=1; i<antall; i=i+7)
	    persTabell[i].samlerAv("ingenting", 1);
	
    }


    // Denne metoden kalles for Ã¥ fÃ¥ en peker til en og en person
    // Returnerer null nÃ¥r det ikke er flere personer igjen

    public Person hentPerson() { 
	Person nestePerson = null;
	if (nestePersonNr < antall) {
	    nestePerson = persTabell[nestePersonNr];
	    nestePersonNr++;
	}
	return nestePerson;
    }

    // Metode som returnerer navnene pÃ¥ alle personene

    public String[] hentPersonnavn() { return oblig3Navn; }

}
