packages<- function(name){
  if(eval(parse(text=paste("!require(",name,")")))){
    install.packages(name)
    eval(parse(text=paste("library(",name,")")))
  }
}
packages("yardstick")
packages("rminer")
packages('rpart') 
packages("randomForest") 
packages("nnet") 
packages('xgboost') 
packages('e1071') 

#Import the dataset 
data = read.csv('/home/id1019/Documents/iris.csv', sep=',')
columns_names = colnames(data)

#Spliting dataset between features (X) and label (y)
#y-> the last column
Y_name = rev(columns_names)[1]
X_names = rev(columns_names)[-1]
formula <- as.formula(paste(Y_name, "~", paste(X_names, collapse= "+"))) 



# Spliting dataset into training set and test set
train_size = 0.7
indice_train= sample(c(1:nrow(data)), train_size*nrow(data))
train=data[indice_train, ]
test=data[-indice_train, ]
Y_test = test[Y_name] 
X_test = test[X_names]
Y_train = train[Y_name] 
X_train = train[X_names]

results_colnames = c('Algorithm', 'Parameters', 'FrameWork', 'balanced_accuracy', 'recall', 'precision', 'accuracy', 'f1', 'macro_recall', 'macro_precision', 'macro_accuracy', 'macro_f1')
results = matrix(ncol=length(results_colnames),nrow=1)

try({
	algo = rpart(formula, data=train ,method="class", maxdepth = 20)
	Y_pred = predict(algo, X_test,  type="class")
	results = rbind(results, c('DecisionTree', 'max_depth = 20', 'R', bal_accuracy_vec(Y_test[,1], Y_pred) 
	, recall_vec(Y_test[,1], Y_pred, estimator="micro") 
	, precision_vec(Y_test[,1], Y_pred, estimator="micro") 
	, accuracy_vec(Y_test[,1], Y_pred) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='ACC') /100 #ACC= equal to micro averaged F1 score (help) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroTPR') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroPRECISION') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroACC') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroF1') /100 
	))
	
	
})

try({
	algo = randomForest(formula, data=train)
	Y_pred = predict(algo, X_test,  type="class")
	results = rbind(results, c('RandomForest', '', 'R', bal_accuracy_vec(Y_test[,1], Y_pred) 
	, recall_vec(Y_test[,1], Y_pred, estimator="micro") 
	, precision_vec(Y_test[,1], Y_pred, estimator="micro") 
	, accuracy_vec(Y_test[,1], Y_pred) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='ACC') /100 #ACC= equal to micro averaged F1 score (help) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroTPR') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroPRECISION') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroACC') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroF1') /100 
	))
	
	
})

try({
	algo = multinom(formula, data=train)
	Y_pred = predict(algo, X_test,  type="class")
	results = rbind(results, c('LogisticRegression', '', 'R', bal_accuracy_vec(Y_test[,1], Y_pred) 
	, recall_vec(Y_test[,1], Y_pred, estimator="micro") 
	, precision_vec(Y_test[,1], Y_pred, estimator="micro") 
	, accuracy_vec(Y_test[,1], Y_pred) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='ACC') /100 #ACC= equal to micro averaged F1 score (help) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroTPR') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroPRECISION') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroACC') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroF1') /100 
	))
	
	
})

try({
	X_train_matrix = as.matrix(X_train)
	X_test_matrix = as.matrix(X_test)
	label_train = as.integer(Y_train[,1])-1
	label_test = as.integer(Y_test[,1])-1
	num_class = length(levels(Y_train[,1]))
	algo <- xgboost(data = X_train_matrix, label = label_train, nrounds = 100, num_class=num_class ) #nrounds=100 like in python
	Y_pred = predict(algo, X_test_matrix)
	Y_pred = as.factor(levels(Y_train[,1])[Y_pred+1]) 
	results = rbind(results, c('XGboost', '', 'R', bal_accuracy_vec(Y_test[,1], Y_pred) 
	, recall_vec(Y_test[,1], Y_pred, estimator="micro") 
	, precision_vec(Y_test[,1], Y_pred, estimator="micro") 
	, accuracy_vec(Y_test[,1], Y_pred) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='ACC') /100 #ACC= equal to micro averaged F1 score (help) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroTPR') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroPRECISION') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroACC') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroF1') /100 
	))
	
	
})

try({
	algo = svm(formula, data=train, type = "nu-classification",kernel = 'radial', gamma=8, cost=2)
	Y_pred = predict(algo, X_test)
	results = rbind(results, c('SVM - Nu-classification', 'kernel = radial, gamma = 8', 'R', bal_accuracy_vec(Y_test[,1], Y_pred) 
	, recall_vec(Y_test[,1], Y_pred, estimator="micro") 
	, precision_vec(Y_test[,1], Y_pred, estimator="micro") 
	, accuracy_vec(Y_test[,1], Y_pred) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='ACC') /100 #ACC= equal to micro averaged F1 score (help) 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroTPR') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroPRECISION') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroACC') /100 
	, mmetric(y=Y_test[,1], x =Y_pred, metric='macroF1') /100 
	))
	
	
})

results_df = data.frame(results, row.names = NULL)
colnames(results_df) =results_colnames
results_df = results_df[-1,]
write.table(results_df,"scripts_upload/results/results_R.csv", sep = ";",dec='.',quote=FALSE, row.names = FALSE)
