import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.python.google.common.io.Files;
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
        	Files.write(ctx.formParam("mml").getBytes(), new File("scripts_upload/code.mml")); 
        	processMML(ctx.formParam("mml"));
        	
        	//generate the html results visualisation
			TableGenerator table_generator = new TableGenerator();
			Files.write(table_generator.getHtml().getBytes(), new File("target/classes/public/visuResults.html")); 
			
        	
        	ctx.redirect("/results-choose.html");
        });

        app.post("/mml-form-generate", ctx -> {
        	ctx.uploadedFiles("myFile").forEach(file -> {
                FileUtil.streamToFile(file.getContent(), "scripts_upload/upload/" + file.getFilename());
            });
        	
        	FormParser form_parser = new FormParser(ctx);
        	Files.write(form_parser.mml.getBytes(), new File("scripts_upload/code.mml")); 
        	processMML(form_parser.mml);
        	
    		
			//generate the html results visualisation
			TableGenerator table_generator = new TableGenerator();
			Files.write(table_generator.getHtml().getBytes(), new File("target/classes/public/visuResults.html")); 
			
				
        	ctx.redirect("/results-choose.html");
        });
        
        
       
    }

	private static void processMML(String mmlContent) throws IOException {
		
		MMLLoader mmlLoader = new MMLLoader();	    
		MMLModel mml = mmlLoader.loadModel(mmlContent);
		
		MMLCompiler mmlcompiler = new MMLCompiler(mml);
		mmlcompiler.compute();
		
	}
	
	

}




