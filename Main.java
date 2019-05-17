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
            	for(int a = 0; a < 20; a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        
        Runnable task2 = new Runnable(){
            @Override
            public void run(){
            	for(int a = 20; a < 40; a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        
        Runnable task3 = new Runnable(){
            @Override
            public void run(){
            	for(int a = 40; a < 60; a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        
        Runnable task4 = new Runnable(){
            @Override
            public void run(){
            	for(int a = 60; a < 70; a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        
        Runnable task5 = new Runnable(){
            @Override
            public void run(){
            	for(int a = 70; a < 80; a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        
        Runnable task6 = new Runnable(){
            @Override
            public void run(){
            	for(int a = 80; a < 100; a++) {
            		computeMatrixInit(a, sequence1, sequence2, matchScore, mismatchScore, indelScore);
                }  
            }
        };
        // Hizalama matrisinin hesaplanmasi
    		
		Thread t1 = new Thread(task1);
		Thread t2 = new Thread(task2);
		Thread t3 = new Thread(task3);
		Thread t4 = new Thread(task4);
		Thread t5 = new Thread(task5);
		Thread t6 = new Thread(task6);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		
		tempList.sort(Comparator.comparing(Score::getScore).reversed());
		
		for(Score obj:tempList) {
        	
        	if(counter < 20) {
        		System.out.println(counter + 1 + ". a: " + obj.getA() + " b: " + obj.getB() + " score: " + obj.getScore());
        	}
        	
        	counter++;
        }
		
		 long endTime = System.currentTimeMillis();
		 long estimatedTime = endTime - startTime; // Geçen süreyi milisaniye cinsinden elde ediyoruz
		 double seconds = (double)estimatedTime / 1000; // saniyeye çevirmek için 1000'e bölüyoruz.
		    
		 System.out.println("Geçen süre: " + seconds + " ms");
        
        // En yüksek skor bilgisine göre sýralama iþlemi 
        /*scoreList.sort(Comparator.comparing(Score::getScore).reversed());
        
        System.out.println("Max Scores:");
                
        // En iyi ilk 20 skorun listelenmesi
        for(Score obj:scoreList) {
        	
        	if(counter < 20) {
        		System.out.println(counter + 1 + ". a: " + obj.getA() + " b: " + obj.getB() + " score: " + obj.getScore());
        	}
        	
        	counter++;
        }
        
        System.out.println("#############################");*/
        
	} catch (Exception e) {
		
		System.out.println(e.getMessage());
		
	}
  }
  
  public static void computeMatrixInit(int a, String sequence1, String sequence2, float matchScore, float mismatchScore, float indelScore) {
	  	      	
      	for(int b = a+1; b < 100; b++) {
      		
      		  sequence1 = sequencesList.get(a);
      		  sequence2 = sequencesList.get(b);
      	
      		  NeedlemanWunsch needlemanWunsch = new NeedlemanWunsch(sequence1, sequence2, matchScore, mismatchScore, indelScore);
    	  
	      	  float[][] computedMatrix = needlemanWunsch.computeMatrix();
	      	  
	      	  score = computedMatrix[computedMatrix.length - 1][computedMatrix[0].length - 1];
	      	  scoreObj = new Score();
	      	  scoreObj.setA(a);
	      	  scoreObj.setB(b);
	      	  scoreObj.setScore(score);
	      	  scoreList.add(scoreObj);
	      	  System.out.println("Thread Name: " + Thread.currentThread().getName() + " a: " + scoreObj.getA() + " b: "+ scoreObj.getB() + " score: " + scoreObj.getScore());
  		}
      }
}
