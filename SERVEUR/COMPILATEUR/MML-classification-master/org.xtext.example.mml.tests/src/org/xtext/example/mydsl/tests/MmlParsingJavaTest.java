package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.SVM;
import org.xtext.example.mydsl.mml.SVMKernel;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.ValidationMetric;

import com.google.common.io.Files;
import com.google.inject.Inject;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class MmlParsingJavaTest {

	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	@Test
	public void loadModel() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"foo.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "mlframework R\n"
				+"algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "F1\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("foo.csv", result.getInput().getFilelocation());			
		
	}		
	
	@Test
	public void compileDataInput() throws Exception {
		//un exemple particulier conforme  à la grammaire Xtext-MMLModel -> il faudrait le généraliser
		MMLModel result = parseHelper.parse("datainput \"iris.csv\" separator ,\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "mlframework R\n"
				+"algorithm DT\n"
				+ "mlframework R\n"
				+"algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "balanced_accuracy F1\n"
				+ "");
		
		
			
		
		
		//execution du python?
		/*
		 * Calling generated Python script (basic solution through systems call)
		 * we assume that "python" is in the path
		 */
		Process p = Runtime.getRuntime().exec("python mml.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }

		
		
	}

		
		
	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}

	
	
	
	
	
	private void mmlToPython(MMLModel result) {
		/************les packages à importer **************/
		String pythonImport = "import pandas as pd\n"; //les packages de base
		
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
			/*
			RFormula:
				('formula' (predictive=FormulaItem "~")? predictors=XFormula)
			;
			
			XFormula: (AllVariables | PredictorVariables);
			
			AllVariables : all='.';
			PredictorVariables : 
				(vars+=FormulaItem ("+" vars+=FormulaItem)*)
			;
			
			// by name or integer
			FormulaItem:
				column=INT | colName=STRING
			;
			 */
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
		Number number = stratificationMethod.getNumber();
		
		
		List<String> columns_names_result = new ArrayList<String>();
		columns_names_result.add(mkValueInSingleQuote("Algorithm"));
		columns_names_result.add(mkValueInSingleQuote("FrameWork"));
		
		List<String> metrics_values = new ArrayList<String>();
		
		if ( stratificationMethod instanceof TrainingTest) {
			
			
			
			/**** Découpage en training et test****/
			pythonImport += "from sklearn.model_selection import train_test_split \n";
			stratificationMethod_text += "\n# Spliting dataset into training set and test set\n"
					+ "train_size = " + number +"\n"
					+ "X_train, X_test, Y_train, Y_test = train_test_split(X, Y, train_size=train_size)\n";	
			
			for (ValidationMetric validationMetric : validationMetrics) {
				
				columns_names_result.add(mkValueInSingleQuote(validationMetric.toString().toLowerCase())) ;
				switch(validationMetric) {
					case BALANCED_ACCURACY:
					case ACCURACY :
					case RECALL:
					case PRECISION:
					case F1:
						String choix = validationMetric.toString().toLowerCase();
						metrics_values.add(choix +"_score(Y_test, algo.predict(X_test)) \n");			
					break;
					case MACRO_RECALL:
						metrics_values.add("recall_score(Y_test, algo.predict(X_test), average = 'macro') \n");		
					break;
					case MACRO_ACCURACY:
						//metric = "macro_accuracy = f1_score(Y_test, algo.predict(X_test)) \nprint(macro_accuracy)";			
					break;
					case MACRO_F1:
						metrics_values.add(" f1_score(Y_test, algo.predict(X_test), average='macro') \n");			
					break;
					case MACRO_PRECISION:
						metrics_values.add(" precision_score(Y_test, algo.predict(X_test), average='macro') \n");
					break;
				}
				
			}
			
			
		}else if(stratificationMethod instanceof CrossValidation) {
			/**** Découpage cross-validation ****/
			stratificationMethod_text = stratificationMethod_text +"#Cross Validation\n";
			stratificationMethod_text = stratificationMethod_text + "numRepetitionCross = " + number +"\n"; 
			
		}
		
		
		
		/*****************************/
		/***/
		/**/
		
		String dataframe_creation = "results = pd.DataFrame(" + columns_names_result +")\n";
		
		
		
		
		//# Set algorithms to use
		String algorithms = "\n\n# Set algorithm to use\n";
		
		List<MLChoiceAlgorithm> algos = result.getAlgorithms();
		
		for (MLChoiceAlgorithm algo : algos) {
			
			MLAlgorithm mlalgo = algo.getAlgorithm();
			FrameworkLang framework = algo.getFramework();
			
			metrics_values.add(0,mkValueInSingleQuote(framework.toString().toLowerCase()));
			
			
			
			String algorithm = "";
			
			if (framework == FrameworkLang.SCIKIT) {
				System.err.println("scikit learn is targeted");
				
				if(mlalgo instanceof DT) {
					DT dt = (DT) mlalgo;
					pythonImport = pythonImport + "from sklearn import tree \n";
					metrics_values.add(0,mkValueInSingleQuote("decision tree"));
					
					algorithm +="algo = tree.DecisionTreeClassifier()\n"
							+"algo.fit(X_train, Y_train)\n\n"
							+ "results.append("+ metrics_values +")\n";
					
				
				
				}else if(mlalgo instanceof SVM) {
					SVM svm = (SVM) mlalgo;
					SVMKernel kernel = svm.getKernel();
					
				}
			}else {
				System.err.println("UNSUPPORTED");
			}
			algorithms = algorithms + algorithm;
			
			
		}
		
		
		
		String pandasCode = pythonImport + csvReading + formula_x_y + stratificationMethod_text + dataframe_creation + algorithms ;
		
	
		
		
		
		
		
		
		
		try {
			Files.write(pandasCode.getBytes(), new File("mml.py"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // traduction en python dans le fichier mml.py
		// end of Python generation
	}
}