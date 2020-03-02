import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split 
from sklearn.metrics import balanced_accuracy_score
from sklearn.metrics import f1_score
from sklearn import tree 
from sklearn.ensemble import RandomForestClassifier 
import sklearn.svm as svm
import sklearn.svm as svm
import sklearn.svm as svm

#Using pandas to import the dataset 
data = pd.read_csv('/home/id1019/Documents/iris.csv', sep=',')
columns_names = data.columns

#Spliting dataset between features (X) and label (y)
Y_name = columns_names[-1]
Y = data[Y_name] 
X = data.drop(columns=Y_name) 
if len(set(Y))>2:  average = 'macro'
else: average=None 



# Spliting dataset into training set and test set
train_size = 0.7
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, train_size=train_size)
results=[['Algorithm', 'Parameters', 'FrameWork', 'balanced_accuracy', 'f1']]


try:
	algo = tree.DecisionTreeClassifier(max_depth = None)
	algo.fit(X_train, Y_train)
	results.append(['DecisionTree', 'max_depth = None', 'scikit-learn', balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average=average) 
	])
except: print('error')

try:
	algo = RandomForestClassifier()
	algo.fit(X_train, Y_train)
	results.append(['RandomForest', '', 'scikit-learn', balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average=average) 
	])
except: print('error')

try:
	algo = svm.SVC(C=1, kernel = 'poly', gamma ='scale')
	algo.fit(X_train, Y_train)
	results.append(['SVM - C-classification', 'C=1, kernel = poly, gamma = scale (default)', 'scikit-learn', balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average=average) 
	])
except: print('error')

try:
	algo = svm.NuSVC( kernel = 'linear', gamma ='scale')
	algo.fit(X_train, Y_train)
	results.append(['SVM - Nu-classification', 'kernel = linear, gamma = scale (default)', 'scikit-learn', balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average=average) 
	])
except: print('error')

try:
	algo = svm.OneClassSVM( kernel = 'linear', gamma ='scale')
	algo.fit(X_train, Y_train)
	results.append(['SVM - One-classification', 'kernel = linear, gamma = scale (default)', 'scikit-learn', balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, f1_score(Y_test, algo.predict(X_test),average=average) 
	])
except: print('error')


pd.DataFrame(results).to_csv("scripts_upload/results/results_python.csv",  sep=";", header=None, index=None)

