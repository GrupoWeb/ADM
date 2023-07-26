package mx.gob.se.rug.masiva.util;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class FileHandlerImpl implements FileHandler {

	private InputStream finp;
	public FileHandlerImpl(String fileName) throws FileNotFoundException  {
		 File inFile = new File(fileName);
		 finp = new FileInputStream(inFile);
	}
	
	public FileHandlerImpl(InputStream fileInp) throws FileNotFoundException  {
		
		 this.finp = fileInp;
	}
	
	
	public String getChar() {
		// TODO Auto-generated method stub
		int charread;
		String charRet = "";
		InputStream in = null;
		try {
		    in =
			new BufferedInputStream(finp);
			if ((charread = in.read()) >= 0) {
				charRet = String.valueOf(charread);
			}
		} catch (IOException e) {
			try {
			   in.close();
			}
			catch(IOException ioex) {
				charRet = null;
			}
		}
		return charRet;
	}
	
	/**
	 * 
	 */
	public String getRecord() {
		// TODO Auto-generated method stub
	    String DataLine = "";
	    BufferedReader br=null;
	    try {
	      br = new BufferedReader(
	    		  new InputStreamReader(finp));
	      DataLine = br.readLine();
    	  br.close();
	    } catch (FileNotFoundException ex) {
	      return (null);
	    } catch (IOException ex) {
    		DataLine = null;
	    }
	    return (DataLine);
	}
	/**
	 * 
	 */
	public List<String> getRecords() {
		// TODO Auto-generated method stub
		ArrayList<String> listRecords = new ArrayList<String>();
	    String DataLine = "";
	    try {
	      BufferedReader br = new BufferedReader(
	    		  new InputStreamReader(finp,"8859_1"));
	      while ((DataLine = br.readLine()) != null) {
	    	  listRecords.add(DataLine.trim());
	        }
	      br.close();
	    } catch (FileNotFoundException ex) {
	      return (null);
	    } catch (IOException ex) {
	      return (listRecords);
	    }
	    return listRecords;
	}
}