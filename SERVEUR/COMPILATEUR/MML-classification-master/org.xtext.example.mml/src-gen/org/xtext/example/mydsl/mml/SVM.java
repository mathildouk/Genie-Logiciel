/**
 * generated by Xtext 2.20.0
 */
package org.xtext.example.mydsl.mml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SVM</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.mydsl.mml.SVM#getGamma <em>Gamma</em>}</li>
 *   <li>{@link org.xtext.example.mydsl.mml.SVM#getC <em>C</em>}</li>
 *   <li>{@link org.xtext.example.mydsl.mml.SVM#isKernelSpecified <em>Kernel Specified</em>}</li>
 *   <li>{@link org.xtext.example.mydsl.mml.SVM#getKernel <em>Kernel</em>}</li>
 *   <li>{@link org.xtext.example.mydsl.mml.SVM#isClassificationSpecified <em>Classification Specified</em>}</li>
 *   <li>{@link org.xtext.example.mydsl.mml.SVM#getSvmclassification <em>Svmclassification</em>}</li>
 * </ul>
 *
 * @see org.xtext.example.mydsl.mml.MmlPackage#getSVM()
 * @model
 * @generated
 */
public interface SVM extends MLAlgorithm
{
  /**
   * Returns the value of the '<em><b>Gamma</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Gamma</em>' attribute.
   * @see #setGamma(String)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getSVM_Gamma()
   * @model
   * @generated
   */
  String getGamma();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.SVM#getGamma <em>Gamma</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Gamma</em>' attribute.
   * @see #getGamma()
   * @generated
   */
  void setGamma(String value);

  /**
   * Returns the value of the '<em><b>C</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>C</em>' attribute.
   * @see #setC(String)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getSVM_C()
   * @model
   * @generated
   */
  String getC();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.SVM#getC <em>C</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>C</em>' attribute.
   * @see #getC()
   * @generated
   */
  void setC(String value);

  /**
   * Returns the value of the '<em><b>Kernel Specified</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Kernel Specified</em>' attribute.
   * @see #setKernelSpecified(boolean)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getSVM_KernelSpecified()
   * @model
   * @generated
   */
  boolean isKernelSpecified();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.SVM#isKernelSpecified <em>Kernel Specified</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Kernel Specified</em>' attribute.
   * @see #isKernelSpecified()
   * @generated
   */
  void setKernelSpecified(boolean value);

  /**
   * Returns the value of the '<em><b>Kernel</b></em>' attribute.
   * The literals are from the enumeration {@link org.xtext.example.mydsl.mml.SVMKernel}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Kernel</em>' attribute.
   * @see org.xtext.example.mydsl.mml.SVMKernel
   * @see #setKernel(SVMKernel)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getSVM_Kernel()
   * @model
   * @generated
   */
  SVMKernel getKernel();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.SVM#getKernel <em>Kernel</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Kernel</em>' attribute.
   * @see org.xtext.example.mydsl.mml.SVMKernel
   * @see #getKernel()
   * @generated
   */
  void setKernel(SVMKernel value);

  /**
   * Returns the value of the '<em><b>Classification Specified</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Classification Specified</em>' attribute.
   * @see #setClassificationSpecified(boolean)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getSVM_ClassificationSpecified()
   * @model
   * @generated
   */
  boolean isClassificationSpecified();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.SVM#isClassificationSpecified <em>Classification Specified</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Classification Specified</em>' attribute.
   * @see #isClassificationSpecified()
   * @generated
   */
  void setClassificationSpecified(boolean value);

  /**
   * Returns the value of the '<em><b>Svmclassification</b></em>' attribute.
   * The literals are from the enumeration {@link org.xtext.example.mydsl.mml.SVMClassification}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Svmclassification</em>' attribute.
   * @see org.xtext.example.mydsl.mml.SVMClassification
   * @see #setSvmclassification(SVMClassification)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getSVM_Svmclassification()
   * @model
   * @generated
   */
  SVMClassification getSvmclassification();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.SVM#getSvmclassification <em>Svmclassification</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Svmclassification</em>' attribute.
   * @see org.xtext.example.mydsl.mml.SVMClassification
   * @see #getSvmclassification()
   * @generated
   */
  void setSvmclassification(SVMClassification value);

} // SVM
