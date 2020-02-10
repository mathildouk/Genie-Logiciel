import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.xtext.example.mydsl.MmlStandaloneSetup;
import org.xtext.example.mydsl.mml.MMLModel;


public class MMLLoader {


	public MMLModel loadModel(String content) {		
		
		try {
		    // first write the content into an MML file
		    File temp = File.createTempFile("mmlfile" + UUID.randomUUID(), ".mml"); 	    
		    URI uri = URI.createFileURI(temp.getAbsolutePath());		
		    BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
		    bw.write(content);
		    bw.close();
		    // second load the resource	
		    MmlStandaloneSetup.doSetup();	
		    Resource res = new ResourceSetImpl().getResource(uri, true);	
	        MMLModel mml = (MMLModel) res.getContents().get(0); 
		    return mml;
		}
		
		catch (IOException e) {
		     System.err.println("Error loading MML model");
		     return null ; 
		}	   
		
		
	}


}

