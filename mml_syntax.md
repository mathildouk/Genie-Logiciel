# MML Syntax

The structure of our language : 
```
	DataInput
	MLChoiceAlgorithm
	Validation
```
**/!\ Any space before egals**

## DataInput

```
datainput filelocation parsingInstruction
```

* **filelocation** : string

* **parsingInstruction** : comma or semi_colon. Optional.

*Example*
```
datainput "data/iris.csv" ;
```

## MLChoiceAlgorithm
You can choose as much algorithm as you want

```
mlframework FrameworkLang
algorithm MLAlgorithm
```
* **FrameworkLang** : scikit-learn or R.

* **MLAlgorithm** : SVM or DT or RandomForest or LogisticRegression or XGboost

*Example*
```
mlframework scikit-learn
algorithm RandomForest
mlframework R
algorithm XGBoost
```

Some algorithm requires hyperparameters.

### SVM
```
algorithm SVM gamma=gamma C=C kernel=SVMKernel classification SVMClassification
```
  
* **gamma** : Float. Optionnal
* **C** : Float. Optionnal
* **SVMKernel** : linear or polynomial of radial. Optionnal
* **SVMClassification** : C-classification or nu-classification or one-classification. Optionnal

*Example*
```
algorithm SVM gamma=5 C=3 kernel=linear classification=C-classification
```

### DT : Décision Tree
  
```
algorithm DT max_depth
```
* **max_depth** : Integer. Optionnal

*Example*
```
algorithm DT 30
```

### RandomForest / LogisticRegression / XGboost 

No hyperparameters


## Validation

```
StratificationMethod
ValidationMetric
```

* **StratificationMethod** : CrossValidation or TrainingTest. **/!\ Cross validation isn't impleted in R**

### CrossValidation
```
CrossValidation { numRepetitionCross number}
```
* **number** : Int.

*Example:*
```
CrossValidation { 30 }
```

### TrainingTest

```
TrainingTest { percentageTraining number}
```
* **number** : Int.

*Example*
```
TrainingTest { 70 }
```

* **ValidationMetric** : balanced_accuracy or recall or precision or F1  or accuracy or macro_recall or macro_precision or macro_F1 or macro_accuracy. As many as you want.
In case of multi-class (more than 2 classes), recall, precision, f1, accuracy are computed with the "micro" average

*Example*
```
balanced_accuracy F1
```

# General Example

```
datainput "/home/id1146/Documents/iris.csv"
mlframework scikit-learn
algorithm DT 15
mlframework scikit-learn
algorithm RF
mlframework R
algorithm DT 15
mlframework R
algorithm RF
mlframework scikit-learn
algorithm SVM gamma=5.6
mlframework scikit-learn
algorithm SVM classification nu-classification
TrainingTest { percentageTraining 70 }
balanced_accuracy F1

```


