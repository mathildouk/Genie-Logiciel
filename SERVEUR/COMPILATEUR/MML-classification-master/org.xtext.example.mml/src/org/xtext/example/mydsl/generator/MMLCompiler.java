package org.xtext.example.mydsl.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;
import org.xtext.example.mydsl.mml.SVMClassification;
import org.xtext.example.mydsl.mml.SVMKernel;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.mml.XGboost;

import com.google.common.io.Files;

public class MMLCompiler {
	private MMLModel mml;
	
	
	public MMLCompiler(MMLModel mml) {
		this.mml = mml;
	}



	public String compute() throws IOException {
		String codepython ="";
		/************les frameworks choisis **************/
		List<MLChoiceAlgorithm> algos = this.mml.getAlgorithms();
		List<FrameworkLang> frameworks = new ArrayList<FrameworkLang>();
		for (MLChoiceAlgorithm algo : algos) {
			if (frameworks.contains(algo.getFramework()) == false ){
				frameworks.add(algo.getFramework());
			}
		}
		
		for (FrameworkLang framework : frameworks) {
			switch(framework) {
			
			case SCIKIT:
				System.out.println("PYTHON");
				codepython= mmlToPython(this.mml);
			break;
			case R:
				System.out.println("R");
			break;
			case JAVA_WEKA:
				System.out.println("JAVA");
			break;
			}
			
		}
		return codepython;
	}
	
	
	
	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}

	
	
	private String mmlToPython(MMLModel result) throws IOException {
		/************les packages à importer **************/
		String pythonImport = "import pandas as pd\n"
				+ "import numpy as np\n"; //les packages de base
		
		/************Importation des données**************/
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation(); //donne l'endroit où est le fichier de data
		
		
		String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		String csv_separator = DEFAULT_COLUMN_SEPARATOR;
		CSVParsingConfiguration parsingInstruction = dataInput.getParsingInstruction();
		
		if (parsingInstruction != null) {			
			System.err.println("parsing instruction..." + parsingInstruction);
			csv_separator = parsingInstruction.getSep().toString();
		}
		String csvReading = "\n#Using pandas to import the dataset \n"
				+ "data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" + mkValueInSingleQuote(csv_separator) + ")\n"
				+ "columns_names = data.columns\n";
		
		
		
		/*****************Définir X et Y******************/
		String formula_x_y = "\n#Spliting dataset between features (X) and label (y)\n";
		RFormula formula = result.getFormula();
		
		if (formula != null) {
			//le decoupage du data set est spécifié
			
		}else {
			// le découpage n'est pas spécifier -> Y dernière colonne
			formula_x_y += "Y_name = columns_names[-1]\n"
						+ "Y = data[Y_name] \n"
						+ "X = data.drop(columns=Y_name) \n";
		}
		
		formula_x_y += "if len(set(Y))>2:  average = 'macro'\n" 
					+ "else: average=None \n";
		/******************* Validation ******************/
		String metrics_code = "";
		
		String stratificationMethod_text = "\n\n";
		Validation validation = result.getValidation();
		List<ValidationMetric> validationMetrics = validation.getMetric();
		StratificationMethod stratificationMethod = validation.getStratification();
		
		
		List<String> columns_names_result = new ArrayList<String>();
		columns_names_result.add(mkValueInSingleQuote("Algorithm"));
		columns_names_result.add(mkValueInSingleQuote("FrameWork"));
		
		List<String> metrics_values = new ArrayList<String>();
		
		if ( stratificationMethod instanceof TrainingTest) {
			Number number = stratificationMethod.getNumber();
			float pct_train = (float) stratificationMethod.getNumber()/100;
			
			///Lever une exception si number en dehors de 0-100 avec try catch
			
			
			/**** Découpage en training et test****/
			pythonImport += "from sklearn.model_selection import train_test_split \n";
			stratificationMethod_text += "\n# Spliting dataset into training set and test set\n"
					+ "train_size = " + pct_train +"\n"
					+ "X_train, X_test, Y_train, Y_test = train_test_split(X, Y, train_size=train_size)\n";	
			
			for (ValidationMetric validationMetric : validationMetrics) {
				
				columns_names_result.add(mkValueInSingleQuote(validationMetric.toString().toLowerCase())) ;
				switch(validationMetric) {
					case BALANCED_ACCURACY:
						pythonImport += "from sklearn.metrics import balanced_accuracy_score\n";
						metrics_values.add("balanced_accuracy_score(Y_test, algo.predict(X_test)) \n");
						break;
					case ACCURACY :
					case RECALL:
					case PRECISION:
					case F1:
					case MACRO_RECALL:
					case MACRO_F1:
					case MACRO_PRECISION:
						String choix = validationMetric.toString().toLowerCase();
						pythonImport += "from sklearn.metrics import " + choix +"_score\n";
						metrics_values.add(choix +"_score(Y_test, algo.predict(X_test),average=average) \n");			
						break;
					
					case MACRO_ACCURACY:
						//j'ai pas trouvé la fonction python	
					break;
					
					
				}
				
			}
			
			
		}else if(stratificationMethod instanceof CrossValidation) {
			Number number = stratificationMethod.getNumber() ;
			/**** Découpage cross-validation ****/
			stratificationMethod_text = stratificationMethod_text +"#Cross Validation\n";
			stratificationMethod_text = stratificationMethod_text + "numRepetitionCross = " + number +"\n"; 
			//A FAIRE
			
		}
		
		
		String dataframe_creation = "results=[" + columns_names_result +"]\n";
		
		System.out.println(columns_names_result.get(0));
		
		String algorithms = "\n\n";
		
		List<MLChoiceAlgorithm> algos = result.getAlgorithms();
	
		for (MLChoiceAlgorithm algo : algos) {
			List<String> framework_algo = new ArrayList<String>();
			MLAlgorithm mlalgo = algo.getAlgorithm();
			FrameworkLang framework = algo.getFramework();
			
			framework_algo.add(0,mkValueInSingleQuote(framework.toString().toLowerCase()));
			
			String algorithm = "";
			
			if (framework == FrameworkLang.SCIKIT) {
				System.err.println("scikit learn is targeted");
				
				
				if(mlalgo instanceof DT) {
					DT dt = (DT) mlalgo;
					
					String max_depth =  "None";
					if (dt.getMax_depth() != 0) {
						max_depth = Integer.toString(dt.getMax_depth());
					}
										
					pythonImport = pythonImport + "from sklearn import tree \n";
					framework_algo.add(0,mkValueInSingleQuote("DecisionTree"));
					framework_algo.addAll(metrics_values);
					
					algorithm +="algo = tree.DecisionTreeClassifier(max_depth = "+ max_depth+")\n"
							+"algo.fit(X_train, Y_train)\n"
							+ "results.append("+ framework_algo +")\n\n\n";
					
				
				}else if(mlalgo instanceof RandomForest) {
					RandomForest rf = (RandomForest) mlalgo;
					pythonImport = pythonImport + "from sklearn.ensemble import RandomForestClassifier \n";
					framework_algo.add(0,mkValueInSingleQuote("RandomForest"));
					framework_algo.addAll(metrics_values);
					
					algorithm +="algo = RandomForestClassifier()\n"
							+"algo.fit(X_train, Y_train)\n"
							+ "results.append("+ framework_algo +")\n\n\n";
					
					
				}else if(mlalgo instanceof LogisticRegression) {
					LogisticRegression lr = (LogisticRegression) mlalgo;
					pythonImport = pythonImport + "from sklearn.linear_model import LogisticRegression \n";
					framework_algo.add(0,mkValueInSingleQuote("LogisticRegression"));
					framework_algo.addAll(metrics_values);
					
					algorithm +="algo = LogisticRegression()\n"
							+"algo.fit(X_train, Y_train)\n"
							+ "results.append("+ framework_algo +")\n\n\n";
					
					
				}else if(mlalgo instanceof XGboost) {
					XGboost xgboost = (XGboost) mlalgo;
					pythonImport = pythonImport + "import xgboost as xgb \n";
					framework_algo.add(0,mkValueInSingleQuote("XGboost"));
					framework_algo.addAll(metrics_values);
					
					algorithm +="algo = xgb.XGBClassifier()\n"
							+"algo.fit(X_train, Y_train)\n"
							+ "results.append("+ framework_algo +")\n\n\n";
					
					
				}else if(mlalgo instanceof SVM) {
					SVM svm = (SVM) mlalgo;
					SVMClassification svmclassification = svm.getSvmclassification();
					String gamma = mkValueInSingleQuote("scale");
					if (svm.getGamma()!=null) {
						gamma = svm.getGamma();
					}
					String c = "1";
					if (svm.getC()!=null) {
						c = svm.getC();
					}
					String kernel = svm.getKernel().toString();
					switch(svm.getKernel()) {
					case LINEAR :
						kernel = "linear";
						break;
					case POLY:
						kernel = "poly";
						break;
					case RADIAL:
						kernel = "rbf";
						break;
						
					}
										
					pythonImport = pythonImport + "import sklearn.svm as svm\n";
					framework_algo.add(0,mkValueInSingleQuote("SVM"));
					framework_algo.addAll(metrics_values);
					
					switch(svmclassification) {
					case CCLASS:
						algorithm +="algo = svm.SVC(C="+ c +", kernel = "+mkValueInSingleQuote(kernel) +", gamma =" + gamma+")\n"
								  + "algo.fit(X_train, Y_train)\n"
								  + "results.append("+ framework_algo +")\n\n\n";
						
						break;
						
					case NU_CLASS:
						algorithm +="algo = svm.NuSVC( kernel = "+mkValueInSingleQuote(kernel) +", gamma =" + gamma+")\n"
								  + "algo.fit(X_train, Y_train)\n"
								  + "results.append("+ framework_algo +")\n\n\n";
						
						break;
					case ONE_CLASS:
						algorithm +="algo = svm.OneClassSVM( kernel = "+mkValueInSingleQuote(kernel) +", gamma =" + gamma+")\n"
								  + "algo.fit(X_train, Y_train)\n"
								  + "results.append("+ framework_algo +")\n\n\n";
						
						break;
					}
				}
				
				
				
				
				
			}else {
				System.err.println("UNSUPPORTED");
			}
			algorithms = algorithms + algorithm;
			
			
		}
		
		String export = "\npd.DataFrame(results).to_csv(\"results_python.csv\",  sep=\";\", header=None, index=None)\nprint(results)\n"; 
		
		String pandasCode = pythonImport + csvReading + formula_x_y + stratificationMethod_text + dataframe_creation + algorithms +export;
		
		
	
		Files.write(pandasCode.getBytes(), new File("scripts_upload/mml.py")); // traduction en python dans le fichier mml.py
		
		/*
		 * Calling generated Python script (basic solution through systems call)
		 * we assume that "python" is in the path
		 */
		
		
		Process p = Runtime.getRuntime().exec("python scripts_upload/mml.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }
		try {
			p.waitFor();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println ("exit: " + p.exitValue());
        System.out.println ("exit: " + p.getErrorStream());
        p.destroy();
		
        /*
        
        ProcessBuilder processBuilder = new ProcessBuilder();
    	processBuilder.command("python", "scripts_upload/mml.py");
    	try {

    		Process process = processBuilder.start();

    		StringBuilder output = new StringBuilder();

    		BufferedReader reader = new BufferedReader(
    				new InputStreamReader(process.getInputStream()));

    		String lin;
    		while ((lin = reader.readLine()) != null) {
    			output.append(lin + "\n");
    		}

    		int exitVal = process.waitFor();
    		if (exitVal == 0) {
    			System.out.println("Success!");
    			System.out.println(output);
    		
    		} else {
    			//abnormal...
    		}

    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
        */
		return pandasCode;
		
		// end of Python generation
	}

}
