package org.xtext.example.mydsl.tests;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.generator.MMLCompiler;

import org.xtext.example.mydsl.mml.MMLModel;


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
		MMLModel result = parseHelper.parse("datainput \"/home/id1019/Documents/iris.csv\"\n" + 
				"mlframework R\n" + 
				"algorithm DT 20\n" + 
				"mlframework R\n" + 
				"algorithm RF\n" + 
				"mlframework R\n" + 
				"algorithm LogisticRegression\n" + 
				"mlframework R\n" + 
				"algorithm XGboost\n" + 
				"mlframework R\n" + 
				"algorithm SVM gamma= 8 C=2 kernel= radial classification nu-classification\n" + 
				"mlframework scikit-learn\n" + 
				"algorithm DT 20\n" + 
				"mlframework scikit-learn\n" + 
				"algorithm RF\n" + 
				"mlframework scikit-learn\n" + 
				"algorithm LogisticRegression\n" + 
				"mlframework scikit-learn\n" + 
				"algorithm XGboost\n" + 
				"mlframework scikit-learn\n" + 
				"algorithm SVM gamma= 8 C=2 kernel= radial classification nu-classification\n" + 
				"TrainingTest { percentageTraining 70 }\n" + 
				"balanced_accuracy recall precision accuracy F1 macro_recall macro_precision macro_accuracy macro_F1"
				);
		
		
		
		MMLCompiler mmlcompiler = new MMLCompiler(result);
		mmlcompiler.compute();
		
		
	}

	@Test
	public void compileRTrainTest() throws Exception {
		//un exemple particulier conforme  à la grammaire Xtext-MMLModel -> il faudrait le généraliser
		MMLModel result = parseHelper.parse("datainput \"/home/id1019/Documents/iris.csv\"\n" + 
				"mlframework R\n" + 
				"algorithm DT 20\n" + 
				"mlframework R\n" + 
				"algorithm RF\n" + 
				"mlframework R\n" + 
				"algorithm LogisticRegression\n" + 
				"mlframework R\n" + 
				"algorithm XGboost\n" + 
				"mlframework R\n" + 
				"algorithm SVM gamma= 8 C=2 kernel= radial classification nu-classification\n" + 
				"mlframework R\n" + 
				"algorithm SVM gamma= 8 classification c-classification\n" + 
				"mlframework scikit-learn\n" + 
				"TrainingTest { percentageTraining 70 }\n" + 
				"balanced_accuracy recall precision accuracy F1 macro_recall macro_precision macro_accuracy macro_F1"
				);
		
		
		
		MMLCompiler mmlcompiler = new MMLCompiler(result);
		mmlcompiler.compute();
		
		
	}
		


	
	
	

}