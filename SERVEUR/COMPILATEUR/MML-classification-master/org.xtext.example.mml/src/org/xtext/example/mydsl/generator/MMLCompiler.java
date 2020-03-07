package org.xtext.example.mydsl.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
		String codeR ="";
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
				codeR = mmlToR(this.mml);
			break;
			case JAVA_WEKA:
				System.out.println("JAVA");
			break;
			}
			
		}
		//return codepython;
		return codeR;
	}
	
	
	
	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}

	/**********************************************************************************************************************/
	/******************************************			PYTHON			***************************************************/
	/**********************************************************************************************************************/
	
	private String mmlToPython(MMLModel result) throws IOException {
		/************les packages à importer **************/
		String pythonImport = "import pandas as pd\n"
				+ "import numpy as np\n"; //les packages de base
		
		/************Importation des données**************/
		DataInput dataInput = result.getInput();
		//String fileLocation = "scripts_upload/upload/" + dataInput.getFilelocation(); //donne l'endroit où est le fichier de data
		String fileLocation =  dataInput.getFilelocation(); 
		
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
			//FormulaItem y = formula.getPredictive();
			//System.out.println(formula.getPredictive().getColName());
			//System.out.println(formula.getPredictive().getColumn());
			//System.out.println(formula.getPredictors());
			//System.out.println(y);
			//System.out.println(x);
			//y.getColumn()
			//x.getAll();
			
		}else {
			// le découpage n'est pas spécifier -> Y dernière colonne
			formula_x_y += "Y_name = columns_names[-1]\n"
						+ "Y = data[Y_name] \n"
						+ "X = data.drop(columns=Y_name) \n";
		}
		
		/******************* Validation ******************/
		String metrics_code = "";
		
		String stratificationMethod_text = "\n\n";
		Validation validation = result.getValidation();
		List<ValidationMetric> validationMetrics = validation.getMetric();
		StratificationMethod stratificationMethod = validation.getStratification();
		
		
		List<String> columns_names_result = new ArrayList<String>();
		columns_names_result.add(mkValueInSingleQuote("Algorithm"));
		columns_names_result.add(mkValueInSingleQuote("Parameters"));
		columns_names_result.add(mkValueInSingleQuote("FrameWork"));
		
		List<String> metrics_values = new ArrayList<String>();
		String metrics_values_list ="";
		String fit_or_cv_algo = "";
		
		if ( stratificationMethod instanceof TrainingTest) {
			Number number = stratificationMethod.getNumber();
			float pct_train = (float) stratificationMethod.getNumber()/100;
			
			///Lever une exception si number en dehors de 0-100 avec try catch
			
			
			/**** Découpage en training et test****/
			pythonImport += "from sklearn.model_selection import train_test_split \n";
			stratificationMethod_text += "\n# Spliting dataset into training set and test set\n"
					+ "train_size = " + pct_train +"\n"
					+ "X_train, X_test, Y_train, Y_test = train_test_split(X, Y, train_size=train_size)\n";	
			
			
			fit_or_cv_algo = "algo.fit(X_train, Y_train)\n";
			
			for (ValidationMetric validationMetric : validationMetrics) {
				
				columns_names_result.add(mkValueInSingleQuote(validationMetric.toString().toLowerCase())) ;
				switch(validationMetric) {
					case BALANCED_ACCURACY:
						pythonImport += "from sklearn.metrics import balanced_accuracy_score\n";
						metrics_values.add("balanced_accuracy_score(Y_test, algo.predict(X_test)) \n");
						break;
					case ACCURACY :	
						pythonImport += "from sklearn.metrics import accuracy_score\n";
						metrics_values.add("accuracy_score(Y_test, algo.predict(X_test)) \n");
						break;
					case RECALL:
					case PRECISION:
					case F1:
						String choix = validationMetric.toString().toLowerCase();
						pythonImport += "from sklearn.metrics import " + choix +"_score\n";
						metrics_values.add(choix +"_score(Y_test, algo.predict(X_test),average= 'micro') \n");
						break;
						
					case MACRO_RECALL:
						pythonImport += "from sklearn.metrics import recall_score\n";
						metrics_values.add("recall_score(Y_test, algo.predict(X_test),average='macro') \n");			
						break;
					case MACRO_F1:
						pythonImport += "from sklearn.metrics import f1_score\n";
						metrics_values.add("f1_score(Y_test, algo.predict(X_test),average='macro') \n");			
						break;
					case MACRO_PRECISION:
						pythonImport += "from sklearn.metrics import precision_score\n";
						metrics_values.add("precision_score(Y_test, algo.predict(X_test),average='macro') \n");			
						break;							
					case MACRO_ACCURACY:
						//j'ai pas trouvé la fonction python	
					break;				
				}
				
			}
			metrics_values_list = metrics_values.toString();
			System.out.println(metrics_values_list.getClass());
			
		}else if(stratificationMethod instanceof CrossValidation) {
			pythonImport += "from sklearn.model_selection import cross_validate\n"
						+ "from sklearn.model_selection import RepeatedKFold\n";
			
			Number number = stratificationMethod.getNumber() ;
			
			/**** Découpage cross-validation ****/
			stratificationMethod_text += "#Cross Validation\n"
										+ "numRepetitionCross = " + number +"\n"
										+ "rkf = RepeatedKFold( n_repeats=numRepetitionCross)\n";
			
			List<String> list_scoring = new ArrayList<String>();
			for (ValidationMetric validationMetric : validationMetrics) {
				columns_names_result.add(mkValueInSingleQuote(validationMetric.toString().toLowerCase())) ;
				
				switch(validationMetric) {
					case BALANCED_ACCURACY:
						list_scoring.add("'balanced_accuracy'");
						break;
					case ACCURACY :
						list_scoring.add("'accuracy'");
						break;
					case RECALL:
						list_scoring.add("'recall_micro'");
						break;
					case PRECISION:
						list_scoring.add("'precision_micro'");
						break;
					case F1:
						list_scoring.add("'f1_micro'");
						break;				
					case MACRO_RECALL:
						list_scoring.add("'recall_macro'");
						break;
					case MACRO_F1:
						list_scoring.add("'f1_macro'");
						break;
					case MACRO_PRECISION:
						list_scoring.add("'precision_macro'");
						break;						
					case MACRO_ACCURACY:
						//j'ai pas trouvé la fonction python	
					break;
				}
			}
			fit_or_cv_algo = "cv_results = cross_validate(algo, X, Y, cv= rkf, scoring="+list_scoring+")\n";
			metrics_values_list = "[cv_results[k].mean() for k in cv_results.keys() if k.startswith(\"test_\")]"; 
			
			//A FAIRE
			
		}
		
		
		String dataframe_creation = "results=[" + columns_names_result +"]\n";
		
		System.out.println(dataframe_creation);
		
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
					framework_algo.add(0,mkValueInSingleQuote("max_depth = "+ max_depth));
					framework_algo.add(0,mkValueInSingleQuote("DecisionTree"));
						
					System.err.println(framework_algo);
					algorithm +="algo = tree.DecisionTreeClassifier(max_depth = "+ max_depth+")\n"
							+ fit_or_cv_algo
							+ "framework_algo = " + framework_algo + "\n"
							+ "framework_algo.extend(" + metrics_values_list+")\n"
							+ "results.append(framework_algo)\n";
					
				
				}
				else if(mlalgo instanceof RandomForest) {
					RandomForest rf = (RandomForest) mlalgo;
					pythonImport = pythonImport + "from sklearn.ensemble import RandomForestClassifier \n";
					framework_algo.add(0,mkValueInSingleQuote(""));
					framework_algo.add(0,mkValueInSingleQuote("RandomForest"));
					algorithm +="algo = RandomForestClassifier()\n"
							+ fit_or_cv_algo
							+ "framework_algo = " + framework_algo + "\n"
							+ "framework_algo.extend(" + metrics_values_list+")\n"
							+ "results.append(framework_algo)\n";
					
					
				}else if(mlalgo instanceof LogisticRegression) {
					LogisticRegression lr = (LogisticRegression) mlalgo;
					pythonImport = pythonImport + "from sklearn.linear_model import LogisticRegression \n";
					framework_algo.add(0,mkValueInSingleQuote(""));
					framework_algo.add(0,mkValueInSingleQuote("LogisticRegression"));
										
					algorithm +="algo = LogisticRegression()\n"
							+ fit_or_cv_algo
							+ "framework_algo = " + framework_algo + "\n"
							+ "framework_algo.extend(" + metrics_values_list+")\n"
							+ "results.append(framework_algo)\n";
					
				}else if(mlalgo instanceof XGboost) {
					XGboost xgboost = (XGboost) mlalgo;
					pythonImport = pythonImport + "import xgboost as xgb \n";
					framework_algo.add(0,mkValueInSingleQuote(""));
					framework_algo.add(0,mkValueInSingleQuote("XGboost"));
										
					algorithm +="algo = xgb.XGBClassifier()\n"
							+ fit_or_cv_algo
							+ "framework_algo = " + framework_algo + "\n"
							+ "framework_algo.extend(" + metrics_values_list+")\n"
							+ "results.append(framework_algo)\n";
					
					
				}else if(mlalgo instanceof SVM) {
					SVM svm = (SVM) mlalgo;
					SVMClassification svmclassification = svm.getSvmclassification();
					String gamma = mkValueInSingleQuote("scale");
					String gamma_no_quote = "scale (default)";
					if (svm.getGamma()!=null) {
						gamma = svm.getGamma();
						gamma_no_quote = svm.getGamma();
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
					
					
					switch(svmclassification) {
					case CCLASS:
						framework_algo.add(0,mkValueInSingleQuote("C="+ c +", kernel = "+kernel +", gamma = " + gamma_no_quote));
						framework_algo.add(0,mkValueInSingleQuote("SVM - C-classification"));
						
						algorithm +="algo = svm.SVC(C="+ c +", kernel = "+mkValueInSingleQuote(kernel) +", gamma =" + gamma+")\n"
								+ fit_or_cv_algo
								+ "framework_algo = " + framework_algo + "\n"
								+ "framework_algo.extend(" + metrics_values_list+")\n"
								+ "results.append(framework_algo)\n";
						
						break;
						
					case NU_CLASS:
						framework_algo.add(0,mkValueInSingleQuote("kernel = "+kernel +", gamma = " + gamma_no_quote));
						framework_algo.add(0,mkValueInSingleQuote("SVM - Nu-classification"));
					
						algorithm +="algo = svm.NuSVC( kernel = "+mkValueInSingleQuote(kernel) +", gamma =" + gamma+")\n"
								+ fit_or_cv_algo
								+ "framework_algo = " + framework_algo + "\n"
								+ "framework_algo.extend(" + metrics_values_list+")\n"
								+ "results.append(framework_algo)\n";
						
						break;
					case ONE_CLASS:
						framework_algo.add(0,mkValueInSingleQuote("kernel = "+kernel +", gamma = " + gamma_no_quote));
						framework_algo.add(0,mkValueInSingleQuote("SVM - One-classification"));
						
						algorithm +="algo = svm.OneClassSVM( kernel = "+mkValueInSingleQuote(kernel) +", gamma =" + gamma+")\n"
								+ fit_or_cv_algo
								+ "framework_algo = " + framework_algo + "\n"
								+ "framework_algo.extend(" + metrics_values_list+")\n"
								+ "results.append(framework_algo)\n";
						
						break;
					}
				}
					
			}
			if(algorithm!=""){
				algorithms = algorithms + "try:\n" + algorithm.replaceAll("(?m)^", "\t") + "except: print('At least one algorithm has received an error')\n\n";
			}
			
		}
		
		String export = "\npd.DataFrame(results).to_csv(\"scripts_upload/results/results_python.csv\",  sep=\";\", header=None, index=None)\n\n"; 
		
		String pandasCode = pythonImport + csvReading + formula_x_y + stratificationMethod_text + dataframe_creation + algorithms +export;
		
		
	
		Files.write(pandasCode.getBytes(), new File("scripts_upload/mml.py")); // traduction en python dans le fichier mml.py
		
		/*
		 * Calling generated Python script (basic solution through systems call)
		 * we assume that "python" is in the path
		 */
		
		
		Process p = Runtime.getRuntime().exec("/home/id1019/anaconda3/bin/python scripts_upload/mml.py"); //which python on terminal
		
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		InputStream error = p.getErrorStream();
	    for (int i = 0; i < error.available(); i++) {
	       System.out.println("" + error.read());
	    }
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
        p.destroy();
		
       
		return pandasCode;
		
		// end of Python generation
	}
	
	
	
	/**********************************************************************************************************************/
	/********************************************			R			***************************************************/
	/**********************************************************************************************************************/
	
	
	private String mmlToR(MMLModel result) throws IOException {
		/************les packages à importer **************/
		String RPackages = "packages<- function(name){\n" + 
				"  if(eval(parse(text=paste(\"!require(\",name,\")\")))){\n" + 
				"    install.packages(name)\n" + 
				"    eval(parse(text=paste(\"library(\",name,\")\")))\n" + 
				"  }\n" + 
				"}\n"; 
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
		String csvReading = "\n#Import the dataset \n"
				+ "data = read.csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" + mkValueInSingleQuote(csv_separator) + ")\n"
				+ "columns_names = colnames(data)\n";
		
		
		/*****************Définir X et Y******************/
		String formula_x_y = "\n#Spliting dataset between features (X) and label (y)\n"
				+ "#y-> the last column\n";
		RFormula formula = result.getFormula();
		
		if (formula != null) {
			//formula specified
			//data = only columns choose
		}else {
			// le découpage n'est pas spécifier -> Y dernière colonne
			formula_x_y += "Y_name = rev(columns_names)[1]\n"
						+ "X_names = rev(columns_names)[-1]\n";
						
		}
		
		formula_x_y += "formula <- as.formula(paste(Y_name, \"~\", paste(X_names, collapse= \"+\"))) \n";
		
		
		/******************* Validation ******************/
		//String fit_or_cv_algo = "";
		String metrics_code = "";
		
		String stratificationMethod_text = "\n\n";
		Validation validation = result.getValidation();
		List<ValidationMetric> validationMetrics = validation.getMetric();
		StratificationMethod stratificationMethod = validation.getStratification();
		
		
		List<String> columns_names_result = new ArrayList<String>();
		columns_names_result.add(mkValueInSingleQuote("Algorithm"));
		columns_names_result.add(mkValueInSingleQuote("Parameters"));
		columns_names_result.add(mkValueInSingleQuote("FrameWork"));
		
		List<String> metrics_values = new ArrayList<String>();
		
		if ( stratificationMethod instanceof TrainingTest) {
			Number number = stratificationMethod.getNumber();
			float pct_train = (float) stratificationMethod.getNumber()/100;
			
			/**** TRAIN / TEST ****/
			
			stratificationMethod_text += "\n# Spliting dataset into training set and test set\n"
					+ "train_size = " + pct_train +"\n"
					+ "indice_train= sample(c(1:nrow(data)), train_size*nrow(data))\n"
					+ "train=data[indice_train, ]\n"
					+ "test=data[-indice_train, ]\n"
					+ "Y_test = test[Y_name] \n"
					+ "X_test = test[X_names]\n"
					+ "Y_train = train[Y_name] \n" 
					+ "X_train = train[X_names]\n\n";
			
			//fit_or_cv_algo = "Y_pred = predict(algo, X_test,  type=\"class\")\n";
			
			RPackages += "packages(\"yardstick\")\n"
					+ "packages(\"rminer\")\n";
			for (ValidationMetric validationMetric : validationMetrics) {
				
				columns_names_result.add(mkValueInSingleQuote(validationMetric.toString().toLowerCase())) ;
				switch(validationMetric) {
					case BALANCED_ACCURACY:
						metrics_values.add("bal_accuracy_vec(Y_test[,1], Y_pred) \n");
						break;
					case ACCURACY :
						metrics_values.add("accuracy_vec(Y_test[,1], Y_pred) \n");
						break;
					case RECALL:
						metrics_values.add("recall_vec(Y_test[,1], Y_pred, estimator=\"micro\") \n");
						break;
					case PRECISION:
						metrics_values.add("precision_vec(Y_test[,1], Y_pred, estimator=\"micro\") \n");
						break;
					case F1:
						metrics_values.add("mmetric(y=Y_test[,1], x =Y_pred, metric='ACC') /100 #ACC= equal to micro averaged F1 score (help) \n");
						break;
					case MACRO_RECALL:
						metrics_values.add("mmetric(y=Y_test[,1], x =Y_pred, metric='macroTPR') /100 \n");
						break;
					case MACRO_F1:
						metrics_values.add("mmetric(y=Y_test[,1], x =Y_pred, metric='macroF1') /100 \n");
						break;
					case MACRO_PRECISION:
						metrics_values.add("mmetric(y=Y_test[,1], x =Y_pred, metric='macroPRECISION') /100 \n");
						break;
					case MACRO_ACCURACY:
						metrics_values.add("mmetric(y=Y_test[,1], x =Y_pred, metric='macroACC') /100 \n");
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
		
		
		//String dataframe_creation = "results = read.csv(text=gsub(\"'|\\\\[|\\\\]\", \"\", \"" + columns_names_result +"\"))\n";
		String dataframe_creation = "results_colnames = c(" + columns_names_result.toString().substring(1, columns_names_result.toString().length()-1) +")\n"
								+ "results = matrix(ncol=length(results_colnames),nrow=1)";
		
		//System.out.println(columns_names_result.toString().replaceAll('([\[\]\'])', ""));
		
		String algorithms = "\n\n";
		
		List<MLChoiceAlgorithm> algos = result.getAlgorithms();
	
		for (MLChoiceAlgorithm algo : algos) {
			List<String> framework_algo = new ArrayList<String>();
			MLAlgorithm mlalgo = algo.getAlgorithm();
			FrameworkLang framework = algo.getFramework();
			
			framework_algo.add(0,mkValueInSingleQuote(framework.toString()));
			
			String algorithm = "";
			
			if (framework == FrameworkLang.R) {
				System.err.println("R is targeted");
				
				
				if(mlalgo instanceof DT) {
					DT dt = (DT) mlalgo;
					
					String max_depth =  ""; //30 default in R
					if (dt.getMax_depth() != 0) {
						max_depth = Integer.toString(dt.getMax_depth());
					}
									
					framework_algo.add(0,mkValueInSingleQuote("max_depth = "+ max_depth));
					framework_algo.add(0,mkValueInSingleQuote("DecisionTree"));
					
					if ( stratificationMethod instanceof TrainingTest) {
						framework_algo.addAll(metrics_values);						
						RPackages = RPackages + "packages('rpart') \n";
						
						algorithm +="algo = rpart(formula, data=train ,method=\"class\", maxdepth = " + max_depth +")\n" 
								+ "Y_pred = predict(algo, X_test,  type=\"class\")\n"
								+ "results = rbind(results, c("+ framework_algo.toString().substring(1, framework_algo.toString().length()-1) +"))\n\n\n";
					}
					
				}
				else if(mlalgo instanceof RandomForest) {
					RandomForest rf = (RandomForest) mlalgo;
					
					framework_algo.add(0,mkValueInSingleQuote(""));
					framework_algo.add(0,mkValueInSingleQuote("RandomForest"));
										
					if ( stratificationMethod instanceof TrainingTest) {
						framework_algo.addAll(metrics_values);
						RPackages = RPackages + "packages(\"randomForest\") \n";
						
						algorithm +="algo = randomForest(formula, data=train)\n" 
								+ "Y_pred = predict(algo, X_test,  type=\"class\")\n"
								+ "results = rbind(results, c("+ framework_algo.toString().substring(1, framework_algo.toString().length()-1) +"))\n\n\n";
								
					}
				}else if(mlalgo instanceof LogisticRegression) {
					LogisticRegression lr = (LogisticRegression) mlalgo;
					
					framework_algo.add(0,mkValueInSingleQuote(""));
					framework_algo.add(0,mkValueInSingleQuote("LogisticRegression"));
					
					if ( stratificationMethod instanceof TrainingTest) {
						framework_algo.addAll(metrics_values);
						RPackages = RPackages + "packages(\"nnet\") \n";
						
						algorithm +="algo = multinom(formula, data=train)\n" 
								+ "Y_pred = predict(algo, X_test,  type=\"class\")\n"
								+ "results = rbind(results, c("+ framework_algo.toString().substring(1, framework_algo.toString().length()-1) +"))\n\n\n";
						
					}
					
				}else if(mlalgo instanceof XGboost) {
					XGboost xgboost = (XGboost) mlalgo;
					
					framework_algo.add(0,mkValueInSingleQuote(""));
					framework_algo.add(0,mkValueInSingleQuote("XGboost"));
										
					if ( stratificationMethod instanceof TrainingTest) {
						framework_algo.addAll(metrics_values);
						RPackages = RPackages + "packages('xgboost') \n";
						
						algorithm +="X_train_matrix = as.matrix(X_train)\n" + 
								"X_test_matrix = as.matrix(X_test)\n" + 
								"label_train = as.integer(Y_train[,1])-1\n" + 
								"label_test = as.integer(Y_test[,1])-1\n" + 
								"num_class = length(levels(Y_train[,1]))\n" + 
								"algo <- xgboost(data = X_train_matrix, label = label_train, nrounds = 100, num_class=num_class ) #nrounds=100 like in python\n" + 
								"Y_pred = predict(algo, X_test_matrix)\n" + 
								"Y_pred = as.factor(levels(Y_train[,1])[Y_pred+1]) \n" +
								"results = rbind(results, c("+ framework_algo.toString().substring(1, framework_algo.toString().length()-1) +"))\n\n\n";
					}
					
				}else if(mlalgo instanceof SVM) {
					SVM svm = (SVM) mlalgo;
					SVMClassification svmclassification = svm.getSvmclassification();
					
					//gamma
					String gamma = "";
					if (svm.getGamma()!=null) {
						gamma = svm.getGamma();
					}
					//cost C
					String c = "";
					if (svm.getC()!=null) {
						c = svm.getC();
					}
					//kernel
					String kernel = svm.getKernel().toString().toLowerCase();					
										
					RPackages = RPackages + "packages('e1071') \n";
					
					switch(svmclassification) {
						case CCLASS:
							framework_algo.add(0,mkValueInSingleQuote("C="+ c +", kernel = "+kernel +", gamma = " + gamma));
							framework_algo.add(0,mkValueInSingleQuote("SVM - C-classification"));
							if ( stratificationMethod instanceof TrainingTest) {
								framework_algo.addAll(metrics_values);
								algorithm +="algo = svm(formula, data=train, type = \"C-classification\",kernel = "+ mkValueInSingleQuote(kernel) + ", gamma="+gamma + ", cost="+c + ")\n" 
										+ "Y_pred = predict(algo, X_test)\n"
										+ "results = rbind(results, c("+ framework_algo.toString().substring(1, framework_algo.toString().length()-1) +"))\n\n\n";									
							}
							break;
							
						case NU_CLASS:
							framework_algo.add(0,mkValueInSingleQuote("kernel = "+kernel +", gamma = " + gamma));
							framework_algo.add(0,mkValueInSingleQuote("SVM - Nu-classification"));
							if ( stratificationMethod instanceof TrainingTest) {
								framework_algo.addAll(metrics_values);							
								algorithm +="algo = svm(formula, data=train, type = \"nu-classification\",kernel = "+ mkValueInSingleQuote(kernel) + ", gamma="+gamma + ", cost="+c + ")\n" 
										+ "Y_pred = predict(algo, X_test)\n"
										+ "results = rbind(results, c("+ framework_algo.toString().substring(1, framework_algo.toString().length()-1) +"))\n\n\n";
							}		
							break;
							
						case ONE_CLASS://problem
							framework_algo.add(0,mkValueInSingleQuote("kernel = "+kernel +", gamma = " + gamma));
							framework_algo.add(0,mkValueInSingleQuote("SVM - One-classification"));
							if ( stratificationMethod instanceof TrainingTest) {
								framework_algo.addAll(metrics_values);								
								algorithm +="algo = svm(formula, data=train, type = \"one-classification\",kernel = "+ mkValueInSingleQuote(kernel) + ", gamma="+gamma + ", cost="+c + ")\n" 
										+ "Y_pred = predict(algo, X_test)\n"
										+ "results = rbind(results, c("+ framework_algo.toString().substring(1, framework_algo.toString().length()-1) +"))\n\n\n";
							}		
							break;
					}
				}
								
								
			}
			if(algorithm!=""){
				algorithms = algorithms + "try({\n" + algorithm.replaceAll("(?m)^", "\t") + "})\n\n";
			}
					
			
		}
		
		String export = "results_df = data.frame(results, row.names = NULL)\n" + 
				"colnames(results_df) =results_colnames\n" + 
				"results_df = results_df[-1,]\n"
				+ "write.table(results_df,\"scripts_upload/results/results_R.csv\", sep = \";\",dec='.',quote=FALSE, row.names = FALSE)\n"; 
		
		String RCode = RPackages + csvReading + formula_x_y + stratificationMethod_text + dataframe_creation + algorithms +export;
		
		
	
		Files.write(RCode.getBytes(), new File("scripts_upload/mml.R")); // traduction en R dans le fichier mml.py
		
		
		
		Process p = Runtime.getRuntime().exec("Rscript scripts_upload/mml.R");
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
		
		
      
		return RCode;
		
		
	}
}
