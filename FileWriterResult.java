import java.io.FileWriter;
import java.io.IOException;

public class FileWriterResult {
	
	public static void usingFileWriter() throws IOException
	{
	    String fileContent = "Hello Learner !! Welcome to howtodoinjava.com.";
	     
	    FileWriter fileWriter = new FileWriter("C:/Users/CasperPc/Desktop/ender.txt");
	    fileWriter.write(fileContent);
	    fileWriter.close();
	}
}
