import io.javalin.core.util.FileUtil;
import io.javalin.http.Context;

public class FormParser {
	
	public String mml = "";
	
	public FormParser(Context ctx) {
		
		String data = ctx.uploadedFiles("myFile").get(0).getFilename();
		
		this.mml = "datainput \"scripts_upload/upload/" + data + "\"\n";
		
		int i = 0;
		while (true)
		{
		  String select_algo = ctx.formParam("select_algo" + Integer.toString(i));
		  
		  if (isNullOrEmpty(select_algo)) {
			  // If the parameter doesn't exist, it means there are no more algorithms.
			  break;
		  }
		  
		  String select_fw = ctx.formParam("select_fw" + Integer.toString(i));
		  this.mml += "mlframework " + select_fw + "\n";
		  this.mml += generateMML(ctx, select_algo, i);
		  
		  i += 1;
		}
		
		String validation = ctx.formParam("val_choice");
		switch(validation) {
		case "cv":
			String k = ctx.formParam("kcrossval");
			this.mml += "CrossValidation { numRepetitionCross " + k + " }\n";
			break;
		case "split":
			String div_rate = ctx.formParam("div_rate_choice");
			this.mml += "TrainingTest { percentageTraining " + div_rate + " }\n";
			break;
		}
		
		this.mml += writeMetricsMML(ctx);
	}
		
	private static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
	
	private String generateMML(Context ctx, String select_algo, int i) {
		String mml = "";
		switch(select_algo) {
		  case "Random Forest":
			  mml = writeRandomForestMML();
			  break;
		  case "Decision Tree":
			  String max_depth = ctx.formParam("dt_maxdepth" + Integer.toString(i));
			  mml = writeDecisionTreeMML(max_depth);
			  break;
		  case "Logistic Regression":
			  mml = writeLogisticRegressionMML();
			  break;
		  case "XGBoost":
			  mml = writeXGBoostMML();
			  break;
		  case "SVM":
			  String gamma = ctx.formParam("gamma" + Integer.toString(i));
			  String c = ctx.formParam("c_choice" + Integer.toString(i));
			  String kernel = ctx.formParam("select_kernel" + Integer.toString(i));
			  String classification = ctx.formParam("select_classification" + Integer.toString(i)).toLowerCase();
			  mml = writeSvmMML(gamma, c, kernel, classification);
			  break;
	  }
		return mml + "\n";
	}
	
	private String writeRandomForestMML() {
		return "algorithm RF";
	}
	
	private String writeDecisionTreeMML(String max_depth) {
		return "algorithm DT " + max_depth;
	}
	
	private String writeLogisticRegressionMML() {
		return "algorithm LogisticRegression";
	}
	
	private String writeXGBoostMML() {
		return "algorithm XGboost";
	}
	
	private String writeSvmMML(String gamma, String c, String kernel, String classification) {
		return "algorithm SVM " + "gamma= " + gamma + " C= " + c + " kernel= " + kernel + " classification " + classification;
	}
	
	private String writeMetricsMML(Context ctx) {
		String mml = "";
		String[] params = {"balanced_accuracy", "recall", "precision", "accuracy", "F1", "macro_recall", "macro_precision", "macro_accuracy", "macro_F1"};
		for (String param: params) {
			if (!(isNullOrEmpty(ctx.formParam(param)))) {
				mml += param + " ";
			}
		}
		return mml;
	}
}
