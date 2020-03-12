package org.xtext.example.mydsl.tests.groupmathieu;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.SVM;
import org.xtext.example.mydsl.mml.SVMKernel;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

import com.google.inject.Inject;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)

public class MmlParsingFakeGroupeMathieuTest {
	
	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	
	/*
	 * same framework, different algos!
	 */
	@Test
	public void loadModel() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"iris.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm RF\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm LogisticRegression\n"
				+ "CrossValidation { numRepetitionCross 5 }\n"
				+ "accuracy\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors " + errors);			
		Assertions.assertEquals("iris.csv", result.getInput().getFilelocation());			
		
	}		
	
	/*
	 * multi-framework, multi algos!
	 */
	@Test
	public void loadMultiFramework() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"iris.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "mlframework R\n"
				+ "algorithm RF\n"
				+ "mlframework Weka\n"
				+ "algorithm LogisticRegression\n"
				+ "CrossValidation { numRepetitionCross 5 }\n"
				+ "accuracy recall\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors " + errors);			
		Assertions.assertEquals("iris.csv", result.getInput().getFilelocation());			
		
	}	
	
	/*
	 * SVM kernel parameter is optional
	 */
	@Test
	public void loadSVMOptionalParameter() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"iris.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm SVM kernel=radial\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm SVM\n"
				+ "CrossValidation { numRepetitionCross 5 }\n"
				+ "accuracy balanced_accuracy\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors " + errors);			
		Assertions.assertEquals("iris.csv", result.getInput().getFilelocation());	
		
		
		MLChoiceAlgorithm ml1 = result.getAlgorithms().get(0);
		MLAlgorithm alg1 = ml1.getAlgorithm();
		Assertions.assertTrue(alg1 instanceof SVM);
		Assertions.assertTrue(((SVM) alg1).isKernelSpecified());
		Assertions.assertEquals(((SVM) alg1).getKernel(), SVMKernel.RADIAL);
		
		MLChoiceAlgorithm ml2 = result.getAlgorithms().get(1);
		MLAlgorithm alg2 = ml2.getAlgorithm();
		Assertions.assertTrue(alg2 instanceof SVM);
		Assertions.assertFalse(((SVM) alg2).isKernelSpecified());
		Assertions.assertEquals(((SVM) alg2).getKernel(), SVMKernel.LINEAR);
		
	}		
	



}
