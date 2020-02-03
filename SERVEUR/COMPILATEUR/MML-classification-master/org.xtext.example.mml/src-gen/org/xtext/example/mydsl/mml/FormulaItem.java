/**
 * generated by Xtext 2.20.0
 */
package org.xtext.example.mydsl.mml;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Formula Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.mydsl.mml.FormulaItem#getColumn <em>Column</em>}</li>
 *   <li>{@link org.xtext.example.mydsl.mml.FormulaItem#getColName <em>Col Name</em>}</li>
 * </ul>
 *
 * @see org.xtext.example.mydsl.mml.MmlPackage#getFormulaItem()
 * @model
 * @generated
 */
public interface FormulaItem extends EObject
{
  /**
   * Returns the value of the '<em><b>Column</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Column</em>' attribute.
   * @see #setColumn(int)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getFormulaItem_Column()
   * @model
   * @generated
   */
  int getColumn();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.FormulaItem#getColumn <em>Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Column</em>' attribute.
   * @see #getColumn()
   * @generated
   */
  void setColumn(int value);

  /**
   * Returns the value of the '<em><b>Col Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Col Name</em>' attribute.
   * @see #setColName(String)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getFormulaItem_ColName()
   * @model
   * @generated
   */
  String getColName();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.FormulaItem#getColName <em>Col Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Col Name</em>' attribute.
   * @see #getColName()
   * @generated
   */
  void setColName(String value);

} // FormulaItem
