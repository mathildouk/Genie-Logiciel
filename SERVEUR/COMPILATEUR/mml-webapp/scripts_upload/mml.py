import pandas as pd
import numpy as np
from sklearn.model_selection import cross_validate
from sklearn.model_selection import RepeatedKFold
from sklearn.ensemble import RandomForestClassifier 

#Using pandas to import the dataset 
data = pd.read_csv('scripts_upload/upload/iris.csv', sep=',')
columns_names = data.columns

#Spliting dataset between features (X) and label (y)
Y_name = columns_names[-1]
Y = data[Y_name] 
X = data.drop(columns=Y_name) 


#Cross Validation
numRepetitionCross = 1
rkf = RepeatedKFold( n_repeats=numRepetitionCross)
results=[['Algorithm', 'Parameters', 'FrameWork', 'balanced_accuracy', 'macro_f1']]


try:
	algo = RandomForestClassifier()
	cv_results = cross_validate(algo, X, Y, cv= rkf, scoring=['balanced_accuracy', 'f1_macro'])
	framework_algo = ['RandomForest', '', 'scikit-learn']
	framework_algo.extend([cv_results[k].mean() for k in cv_results.keys() if k.startswith("test_")])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')


pd.DataFrame(results).to_csv("scripts_upload/results/results_python.csv",  sep=";", header=None, index=None)

