import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split 
from sklearn.metrics import balanced_accuracy_score
from sklearn.metrics import accuracy_score
from sklearn.ensemble import RandomForestClassifier 

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
results=[['Algorithm', 'Parameters', 'FrameWork', 'balanced_accuracy', 'accuracy']]


try:
	algo = RandomForestClassifier()
	algo.fit(X_train, Y_train)
	framework_algo = ['RandomForest', '', 'scikit-learn']
	framework_algo.extend([balanced_accuracy_score(Y_test, algo.predict(X_test)) 
	, accuracy_score(Y_test, algo.predict(X_test)) 
	])
	results.append(framework_algo)
except: print('At least one algorithm has received an error')


pd.DataFrame(results).to_csv("scripts_upload/results/results_python.csv",  sep=";", header=None, index=None)

