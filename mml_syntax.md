# MML Syntax

The structure of our language : 
```
	input=DataInput
	algorithms+=MLChoiceAlgorithm+
	formula=RFormula?
	validation=Validation
```
**/!\ Any space before egals**
## DataInput

```
DataInput: filelocation parsingInstruction;
```

* **filelocation** : string

* **parsingInstruction** : comma or semi_colon. Optional.

*Example*
```
input= "data/iris.csv" ";"
```

## MLChoiceAlgorithm
You can choose as much algorithm as you want

```
MLChoiceAlgorithm:
	'mlframework' framework = FrameworkLang
	'algorithm' algorithm = MLAlgorithm
```
* **FrameworkLang** : scikit-learn or R.

* **MLAlgorithm** : SVM or DT or RandomForest or LogisticRegression or XGboost

*Example*
```
MLChoiceAlgorithm = scikit-learn
                    RandomForest
```

Some algorithm requires hyperparameters.

### SVM
```
MLChoiceAlgorithm= scikit-learn
SVM gamma=gamma C=C kernel=SVMKernel
	classification=SVMClassification
```
  
* **gamma** : Float. Optionnal
* **C** : Float. Optionnal
* **SVMKernel** : linear or polynomial of radial. Optionnal
* **SVMClassification** : C-classification or nu-classification or one-classification. Optionnal

*Example*
```
MLChoiceAlgorithm = scikit-learn
SVM gamma=5 C=3 kernel=linear
classification=C-classification
```

### DT : Décision Tree

```
DT max_depth
```
  
* **max_depth** : Integer. Optionnal

*Example*
```
MLChoiceAlgorithm = scikit-learn
DT 30
```

### RandomForest / LogisticRegression / XGboost 

No hyperparameters

## RFormula

```
RFormula: predictive=FormulaItem ~ predictors=XFormula
```
* **FormulaItem** : column(Int) or colName(String). At least one. Separated by '+' everytime.
* **XFormula**: AllVariables('.') or PredictorVariable.

#### PredictorVariables
```
PredictorVariables : vars=FormulaItem 
```

*Example*:
```
RFormula: predictive="PetalLength" ~ predictors= .
```

## Validation

```
Validation: 	stratification=StratificationMethod
	             metric=ValidationMetric
```

* **StratificationMethod** : CrossValidation or TrainingTest. **/!\ Cross validation isn't impleted in R**
* **ValidationMetric** : balanced_accuracy or recall or precision or F1  or accuracy or macro_recall or macro_precision or macro_F1 or macro_accuracy. As many as you want.
In case of multi-class (more than 2 classes), recall, precision, f1, accuracy are computed with the "micro" average

### CrossValidation
```
CrossValidation {numRepetitionCross}
```
* **numRepetitionCross** : Int.

*Example:*
```
CrossValidation {30}
recall accuracy
```

### TrainingTest

```
TrainingTest {percentageTraining};
```
* **percentageTraining** : Int.

*Example*
```
TrainingTest {70}
balanced_accuracy recall
```

