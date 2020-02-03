/**
 * generated by Xtext 2.20.0
 */
package org.xtext.example.mydsl.mml;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.xtext.example.mydsl.mml.MmlPackage
 * @generated
 */
public interface MmlFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MmlFactory eINSTANCE = org.xtext.example.mydsl.mml.impl.MmlFactoryImpl.init();

  /**
   * Returns a new object of class '<em>MML Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MML Model</em>'.
   * @generated
   */
  MMLModel createMMLModel();

  /**
   * Returns a new object of class '<em>Data Input</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Data Input</em>'.
   * @generated
   */
  DataInput createDataInput();

  /**
   * Returns a new object of class '<em>CSV Parsing Configuration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>CSV Parsing Configuration</em>'.
   * @generated
   */
  CSVParsingConfiguration createCSVParsingConfiguration();

  /**
   * Returns a new object of class '<em>ML Choice Algorithm</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>ML Choice Algorithm</em>'.
   * @generated
   */
  MLChoiceAlgorithm createMLChoiceAlgorithm();

  /**
   * Returns a new object of class '<em>ML Algorithm</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>ML Algorithm</em>'.
   * @generated
   */
  MLAlgorithm createMLAlgorithm();

  /**
   * Returns a new object of class '<em>SVM</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>SVM</em>'.
   * @generated
   */
  SVM createSVM();

  /**
   * Returns a new object of class '<em>DT</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>DT</em>'.
   * @generated
   */
  DT createDT();

  /**
   * Returns a new object of class '<em>Random Forest</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Random Forest</em>'.
   * @generated
   */
  RandomForest createRandomForest();

  /**
   * Returns a new object of class '<em>Logistic Regression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Logistic Regression</em>'.
   * @generated
   */
  LogisticRegression createLogisticRegression();

  /**
   * Returns a new object of class '<em>XGboost</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>XGboost</em>'.
   * @generated
   */
  XGboost createXGboost();

  /**
   * Returns a new object of class '<em>RFormula</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>RFormula</em>'.
   * @generated
   */
  RFormula createRFormula();

  /**
   * Returns a new object of class '<em>XFormula</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>XFormula</em>'.
   * @generated
   */
  XFormula createXFormula();

  /**
   * Returns a new object of class '<em>All Variables</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>All Variables</em>'.
   * @generated
   */
  AllVariables createAllVariables();

  /**
   * Returns a new object of class '<em>Predictor Variables</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Predictor Variables</em>'.
   * @generated
   */
  PredictorVariables createPredictorVariables();

  /**
   * Returns a new object of class '<em>Formula Item</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Formula Item</em>'.
   * @generated
   */
  FormulaItem createFormulaItem();

  /**
   * Returns a new object of class '<em>Validation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Validation</em>'.
   * @generated
   */
  Validation createValidation();

  /**
   * Returns a new object of class '<em>Stratification Method</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Stratification Method</em>'.
   * @generated
   */
  StratificationMethod createStratificationMethod();

  /**
   * Returns a new object of class '<em>Cross Validation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Cross Validation</em>'.
   * @generated
   */
  CrossValidation createCrossValidation();

  /**
   * Returns a new object of class '<em>Training Test</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Training Test</em>'.
   * @generated
   */
  TrainingTest createTrainingTest();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  MmlPackage getMmlPackage();

} //MmlFactory
