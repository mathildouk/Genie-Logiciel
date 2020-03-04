import pandas as pd
import numpy as np
from sklearn.model_selection import cross_validate
from sklearn.model_selection import RepeatedKFold
from sklearn import tree 
from sklearn.ensemble import RandomForestClassifier 

#Using pandas to import the dataset 
data = pd.read_csv('/home/id1019/Documents/iris.csv', sep=',')
columns_names = data.columns

#Spliting dataset between features (X) and label (y)
if len(set(Y))>2:  average = 'micro'; average_cv='micro'
else: average='binary'; average_cv='' 


#Cross Validation
numRepetitionCross = 10
rkf = RepeatedKFold( n_repeats=numRepetitionCross)
results=[['Algorithm', 'Parameters', 'FrameWork', 'balanced_accuracy', 'f1', 'recall', 'macro_f1']]


try:
	algo = tree.DecisionTreeClassifier(max_depth = None)
	cv_results = cross_validate(algo, X, Y, cv= rkf, scoring=['balanced_accuracy', 'f1_'+average_cv, 'recall_'+average_cv, 'f1_macro'])
	framework_algo = ['DecisionTree', 'max_depth = None', 'scikit-learn']
	framework_algo.extend([cv_results[k].mean() for k in cv_results.keys() if k.startswith("test_")])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')

try:
	algo = RandomForestClassifier()
	cv_results = cross_validate(algo, X, Y, cv= rkf, scoring=['balanced_accuracy', 'f1_'+average_cv, 'recall_'+average_cv, 'f1_macro'])
	framework_algo = ['RandomForest', '', 'scikit-learn']
	framework_algo.extend([cv_results[k].mean() for k in cv_results.keys() if k.startswith("test_")])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')


pd.DataFrame(results).to_csv("scripts_upload/results/results_python.csv",  sep=";", header=None, index=None)

