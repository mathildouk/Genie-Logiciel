import pandas as pd
from sklearn.model_selection import train_test_split 
from sklearn import tree 

#Using pandas to import the dataset 
data = pd.read_csv('iris.csv', sep=',')
columns_names = data.columns

#Spliting dataset between features (X) and label (y)
Y_name = columns_names[-1]
Y = data[Y_name] 
X = data.drop(columns=Y_name) 



# Spliting dataset into training set and test set
train_size = 70
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, train_size=train_size)
results = pd.DataFrame(['Algorithm', 'FrameWork', 'balanced_accuracy', 'f1'])


# Set algorithm to use
algo = tree.DecisionTreeClassifier()
algo.fit(X_train, Y_train)

results.append(['decision tree', 'scikit-learn', balanced_accuracy_score(Y_test, algo.predict(X_test)) 
, f1_score(Y_test, algo.predict(X_test)) 
])
