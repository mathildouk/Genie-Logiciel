import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split 
from sklearn.metrics import balanced_accuracy_score
from sklearn.metrics import recall_score
from sklearn.metrics import precision_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score
from sklearn.metrics import recall_score
from sklearn.metrics import precision_score
from sklearn.metrics import f1_score
from sklearn import tree 
from sklearn.ensemble import RandomForestClassifier 
from sklearn.linear_model import LogisticRegression 
import xgboost as xgb 
import sklearn.svm as svm

#Using pandas to import the dataset 
data = pd.read_csv('/home/id1019/Documents/iris.csv', sep=',')
columns_names = data.columns

#Spliting dataset between features (X) and label (y)
Y_name = columns_names[-1]
Y = data[Y_name] 
X = data.drop(columns=Y_name) 



# Spliting dataset into training set and test set
train_size = 0.7
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, train_size=train_size)
results=[['Algorithm', 'Parameters', 'FrameWork', 'balanced_accuracy', 'recall', 'precision', 'accuracy', 'f1', 'macro_recall', 'macro_precision', 'macro_f1']]


try:
	algo = tree.DecisionTreeClassifier(max_depth = 20)
	algo.fit(X_train, Y_train)
	framework_algo = ['DecisionTree', 'max_depth = 20', 'scikit-learn']
	framework_algo.extend([balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, recall_score(Y_test, algo.predict(X_test),average= 'micro') 
	, precision_score(Y_test, algo.predict(X_test),average= 'micro') 
	, accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average= 'micro') 
	, recall_score(Y_test, algo.predict(X_test),average='macro') 
	, precision_score(Y_test, algo.predict(X_test),average='macro') 
	, f1_score(Y_test, algo.predict(X_test),average='macro') 
	])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')

try:
	algo = RandomForestClassifier()
	algo.fit(X_train, Y_train)
	framework_algo = ['RandomForest', '', 'scikit-learn']
	framework_algo.extend([balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, recall_score(Y_test, algo.predict(X_test),average= 'micro') 
	, precision_score(Y_test, algo.predict(X_test),average= 'micro') 
	, accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average= 'micro') 
	, recall_score(Y_test, algo.predict(X_test),average='macro') 
	, precision_score(Y_test, algo.predict(X_test),average='macro') 
	, f1_score(Y_test, algo.predict(X_test),average='macro') 
	])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')

try:
	algo = LogisticRegression()
	algo.fit(X_train, Y_train)
	framework_algo = ['LogisticRegression', '', 'scikit-learn']
	framework_algo.extend([balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, recall_score(Y_test, algo.predict(X_test),average= 'micro') 
	, precision_score(Y_test, algo.predict(X_test),average= 'micro') 
	, accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average= 'micro') 
	, recall_score(Y_test, algo.predict(X_test),average='macro') 
	, precision_score(Y_test, algo.predict(X_test),average='macro') 
	, f1_score(Y_test, algo.predict(X_test),average='macro') 
	])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')

try:
	algo = xgb.XGBClassifier()
	algo.fit(X_train, Y_train)
	framework_algo = ['XGboost', '', 'scikit-learn']
	framework_algo.extend([balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, recall_score(Y_test, algo.predict(X_test),average= 'micro') 
	, precision_score(Y_test, algo.predict(X_test),average= 'micro') 
	, accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average= 'micro') 
	, recall_score(Y_test, algo.predict(X_test),average='macro') 
	, precision_score(Y_test, algo.predict(X_test),average='macro') 
	, f1_score(Y_test, algo.predict(X_test),average='macro') 
	])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')

try:
	algo = svm.NuSVC( kernel = 'rbf', gamma =8)
	algo.fit(X_train, Y_train)
	framework_algo = ['SVM - Nu-classification', 'kernel = rbf, gamma = 8', 'scikit-learn']
	framework_algo.extend([balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, recall_score(Y_test, algo.predict(X_test),average= 'micro') 
	, precision_score(Y_test, algo.predict(X_test),average= 'micro') 
	, accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average= 'micro') 
	, recall_score(Y_test, algo.predict(X_test),average='macro') 
	, precision_score(Y_test, algo.predict(X_test),average='macro') 
	, f1_score(Y_test, algo.predict(X_test),average='macro') 
	])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')


pd.DataFrame(results).to_csv("scripts_upload/results/results_python.csv",  sep=";", header=None, index=None)

