print('ok')
import pandas as pd
print('okpandas')
import numpy as np
print('oknumpy')
from sklearn.model_selection import train_test_split 
print('sk')
from sklearn.metrics import balanced_accuracy_score
from sklearn.metrics import recall_score
from sklearn.metrics import precision_score
from sklearn.metrics import accuracy_score
from sklearn.ensemble import RandomForestClassifier 
from sklearn import tree 
from sklearn.linear_model import LogisticRegression 
import xgboost as xgb 
print('okxg')
import sklearn.svm as svm

#Using pandas to import the dataset 
data = pd.read_csv('scripts_upload/upload/iris.csv', sep=',')
columns_names = data.columns

#Spliting dataset between features (X) and label (y)
Y_name = columns_names[-1]
Y = data[Y_name] 
X = data.drop(columns=Y_name) 



# Spliting dataset into training set and test set
train_size = 0.8
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, train_size=train_size)
results=[['Algorithm', 'Parameters', 'FrameWork', 'balanced_accuracy', 'recall', 'precision', 'accuracy']]


try:
	algo = RandomForestClassifier()
	algo.fit(X_train, Y_train)
	framework_algo = ['RandomForest', '', 'scikit-learn']
	framework_algo.extend([balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, recall_score(Y_test, algo.predict(X_test),average= 'micro') 
	, precision_score(Y_test, algo.predict(X_test),average= 'micro') 
	, accuracy_score(Y_test, algo.predict(X_test)) 
	])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')

try:
	algo = tree.DecisionTreeClassifier(max_depth = 30)
	algo.fit(X_train, Y_train)
	framework_algo = ['DecisionTree', 'max_depth = 30', 'scikit-learn']
	framework_algo.extend([balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, recall_score(Y_test, algo.predict(X_test),average= 'micro') 
	, precision_score(Y_test, algo.predict(X_test),average= 'micro') 
	, accuracy_score(Y_test, algo.predict(X_test)) 
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
	])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')

try:
	algo = svm.SVC(C=1, kernel = 'linear', gamma =1)
	algo.fit(X_train, Y_train)
	framework_algo = ['SVM - C-classification', 'C=1, kernel = linear, gamma = 1', 'scikit-learn']
	framework_algo.extend([balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, recall_score(Y_test, algo.predict(X_test),average= 'micro') 
	, precision_score(Y_test, algo.predict(X_test),average= 'micro') 
	, accuracy_score(Y_test, algo.predict(X_test)) 
	])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')


print(pd.DataFrame(results))

