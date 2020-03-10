import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import org.xtext.example.mydsl.generator.MMLCompiler;
import org.xtext.example.mydsl.mml.MMLModel;

import io.javalin.Javalin;
import io.javalin.core.util.FileUtil;

public class Main {

  
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(8080);

        app.post("/generate", ctx -> {
            ctx.html("Your MML " + processMML(ctx.formParam("mml")) + " will be processed");
   
        });

        app.post("/upload-example", ctx -> {
            ctx.uploadedFiles("files").forEach(file -> {
                FileUtil.streamToFile(file.getContent(), "scripts_upload/upload/" + file.getFilename());
            });
            ctx.html("Upload successful");
        });

    }

	private static String processMML(String mmlContent) throws IOException {
		
		MMLLoader mmlLoader = new MMLLoader();	    
		MMLModel mml = mmlLoader.loadModel(mmlContent);
		
		MMLCompiler mmlcompiler = new MMLCompiler(mml);
		String computeMML = mmlcompiler.compute();
		
	
		//concat results in one file
		Process p = Runtime.getRuntime().exec("/home/id1019/anaconda3/bin/python scripts_upload/concatCSV.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		
		return computeMML;
	}
	
	

}




