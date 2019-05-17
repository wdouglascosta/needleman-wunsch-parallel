import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {
  
    // ********************** Deðiþkenlerin tanýmlanmasý  ***********************
     
	static Score scoreObj;
	
	static int count = 0;
    static int counter = 0;
    
    static String sequence1 = "";
    static String sequence2 = "";
    
    static float matchScore = (float) 3.621354295;
    static float mismatchScore = (float) -2.451795405;
    static float indelScore = (float) -1.832482334;
    
    static float score = 0;
    static float maxScore = 0;
    
    static List<Object> listDistinct = new ArrayList<Object>();
    static List<Score> scoreList = new ArrayList<Score>();
    static List<Float> successScores = new ArrayList<Float>();
    private static final Object lock= new Object();  
    
    static HashMap<Integer, String> sequencesList = new HashMap<Integer, String>();
    
    static String fileName = "5K_Sequence";
    
    // ***************************************************************************
  public static void main(String[] args) throws FileNotFoundException {
	  
	long  startTime = System.currentTimeMillis();
	List<Score> tempList = new ArrayList<Score>();
	 
    try (Scanner sc = new Scanner(new File(fileName + ".fasta"))) {
    	
    	// Fasta dosyasindan verilerin okunmasi
    	
        while (sc.hasNextLine()) {
        	
            String line = sc.nextLine().trim();
            
            if(!line.startsWith(">") && line != null && line.trim().length() > 0) {
            	
            	sequencesList.put(count, line);
            	count++;
            }
        }
        
        Runnable task1 = new Runnable(){
            @Override
            public void run(){
            	for(int a = 0; a < 500; a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        
        Runnable task2 = new Runnable(){
            @Override
            public void run(){
            	for(int a = 500; a < 1500; a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        
        Runnable task3 = new Runnable(){
            @Override
            public void run(){
            	for(int a = 1500; a < 3000; a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        
        Runnable task4 = new Runnable(){
            @Override
            public void run(){
            	for(int a = 3000; a < sequencesList.size(); a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        
        // Hizalama matrisinin hesaplanmasi
    		
		Thread t1 = new Thread(task1);
		Thread t2 = new Thread(task2);
		Thread t3 = new Thread(task3);
		Thread t4 = new Thread(task4);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		
		scoreList.sort(Comparator.comparing(Score::getScore).reversed());
		
		 for(Score obj:scoreList) {
        	
        	if(counter < 20) {
        		System.out.println(counter + 1 + ". a: " + obj.getA() + " b: " + obj.getB() + " score: " + obj.getScore());
        	}
        	
        	counter++;
         }
		
		 long endTime = System.currentTimeMillis();
		 long estimatedTime = endTime - startTime; // Geçen süreyi milisaniye cinsinden elde ediyoruz
		 double seconds = (double)estimatedTime / 1000; // saniyeye çevirmek için 1000'e bölüyoruz.
		    
		 System.out.println("Geçen süre: " + seconds + " ms");
                
	} catch (Exception e) {
		
		System.out.println(e.getMessage());
		
	}
  }
  
  public static synchronized List<Score> computeMatrixInit(int a, String sequence1, String sequence2, float matchScore, float mismatchScore, float indelScore) {
	  	      	
      	for(int b = a+1; b < sequencesList.size(); b++) {
      		
      		  sequence1 = sequencesList.get(a);
      		  sequence2 = sequencesList.get(b);
      	
      		  NeedlemanWunsch needlemanWunsch = new NeedlemanWunsch(sequence1, sequence2, matchScore, mismatchScore, indelScore);
    	  
	      	  float[][] computedMatrix = needlemanWunsch.computeMatrix();
	      	  
	      	  score = computedMatrix[computedMatrix.length - 1][computedMatrix[0].length - 1];
	      	  scoreObj = new Score();
	      	  scoreObj.setA(a);
	      	  scoreObj.setB(b);
	      	  scoreObj.setScore(score);
	      	  
	      	  synchronized(lock){
	      		scoreList.add(scoreObj);
	      	  }
	      	 
	      	  //System.out.println("Thread Name: " + Thread.currentThread().getName() + " a: " + scoreObj.getA() + " b: "+ scoreObj.getB() + " score: " + scoreObj.getScore());
  		}
      	
      	 return scoreList;
      }
}
