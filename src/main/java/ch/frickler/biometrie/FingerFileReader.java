package ch.frickler.biometrie;

import java.io.InputStream;
import java.util.Scanner;

public class FingerFileReader {

	public FingerFileReader() {
		super();

		
		InputStream is = getClass().getResourceAsStream( "/test.txt" );
		
		Scanner input = new Scanner(is);

		while(input.hasNext()) {
		    String nextToken = input.next();
		    System.out.println(nextToken);
		}

		input.close();
	}
	
	

}
