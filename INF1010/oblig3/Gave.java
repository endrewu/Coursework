public interface Gave {
  String kategori();
    // Returnerer en tekststreng som gjÃ¸r det mulig Ã¥ vite hva slags gave
    // dette er, f.eks. Â«bokÂ», Â«plateÂ», Â«vinÂ», Â«skoÂ», Â«sjokoladeÂ», Â«bilÂ»...

  String gaveId();
    // Returnerer en tekststreng som identifiserer gaven. To gaver med
    // lik {\em kategori} og {\em gaveId} er samme gave (gjenstand).
}
