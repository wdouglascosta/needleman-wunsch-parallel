/**
 * Algoritma, basitçe iki dizgi (string) arasýndaki yaslama durumunu bulmayý amaçlar. 
 * Buna göre iki dizgiden oluþturulan bir ölçüm deðeri ile (metric) dizgiler karþýlaþtýrýlýr 
 * ve farklýlýklar bulunur.
 */
public class NeedlemanWunsch {
	
	  String sequence1 = "";
	  String sequence2 = ""; 
	  float matchScore = 0.0f; 
	  float mismatchScore = 0.0f; 
	  float indelScore = 0.0f;
	
	  public NeedlemanWunsch(String sequence1, String sequence2, float matchScore, float mismatchScore, float indelScore) {
		  this.sequence1 = sequence1;
		  this.sequence2 = sequence2;
		  this.matchScore = matchScore;
		  this.mismatchScore = mismatchScore;
		  this.indelScore = indelScore;
	  }
	  
	  /**
	   * Global maksimum benzerlik matrisini hesaplayan fonksiyon.
	   *
	   * @param sequence1 Ýlk amino asit dizisini temsil eden bir String.
	   * @param sequence2 Ýkinci amino asit dizisini temsil eden bir String.
	   * @param matchScore Eþleþmelere atanan puaný temsil eden bir float degeri.
	   * @param mimatchScore Uyuþmazlýklara verilen puaný temsil eden bir float degeri.
	   * @param indelScore Eklemeler ve silmelere verilen puaný temsil eden bir float degeri.
	   */
	  public float[][] computeMatrix() {
		  
	    sequence1 = "-" + sequence1;
	    sequence2 = "-" + sequence2;
	
	    float[][] resultMatrix = new float[sequence1.length()][sequence2.length()];
	
	    for (int i = 0; i < sequence1.length(); i++) {
	      resultMatrix[i][0] = i * indelScore;
	    }
	    
	    for (int j = 0; j < sequence2.length(); j++) {
	      resultMatrix[0][j] = resultMatrix[0][j] = j * indelScore;
	    }
	    
	    for (int i = 1; i < sequence1.length(); i++) {
	      for (int j = 1; j < sequence2.length(); j++) {
	    	  
	        resultMatrix[i][j] = Math.max(resultMatrix[i - 1][j] + indelScore,
	        Math.max(resultMatrix[i][j - 1] + indelScore, resultMatrix[i - 1][j - 1] +
	        (sequence1.charAt(i) == sequence2.charAt(j) ? matchScore : mismatchScore)));
	        
	      }
	    }
	
	    return resultMatrix;
	  }
}