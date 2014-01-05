package de.uni_leipzig.imise.diff.calculation;

public class Measures {

	
	public static float levenshtein (String s1,String s2){
		    return 1-(float)(editDistance(s1,s2))/(float)Math.max(s1.length(), s2.length());
		}


	public static int editDistance (String s1, String s2){
		int matrix[][] = new int[s1.length() + 1][s2.length() + 1];
	    for (int i = 0; i < s1.length() + 1; i++) {
	      matrix[i][0] = i;
	    }
	    for (int i = 0; i < s2.length() + 1; i++) {
	      matrix[0][i] = i;
	    }
	    for (int a = 1; a < s1.length() + 1; a++) {
	      for (int b = 1; b < s2.length() + 1; b++) {
	        int right = 0;
	        if (s1.charAt(a - 1) != s2.charAt(b - 1)) {
	          right = 1;
	        }
	        int mini = matrix[a - 1][b] + 1;
	        if (matrix[a][b - 1] + 1 < mini) {
	          mini = matrix[a][b - 1] + 1;
	        }
	        if (matrix[a - 1][b - 1] + right < mini) {
	          mini = matrix[a - 1][b - 1] + right;
	        }
	        matrix[a][b] = mini;
	        
	      }
	    }
	    return matrix[s1.length()][s2.length()];
	}
	
}
