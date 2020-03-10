packages<- function(name){
  if(eval(parse(text=paste("!require(",name,")")))){
    install.packages(name)
    eval(parse(text=paste("library(",name,")")))
  }
}
packages("yardstick")
packages("rminer")
packages("randomForest") 

#Import the dataset 
data = read.csv('scripts_upload/upload/iris.csv', sep=',')
columns_names = colnames(data)

#Spliting dataset between features (X) and label (y)
#y-> the last column
Y_name = rev(columns_names)[1]
X_names = rev(columns_names)[-1]
formula <- as.formula(paste(Y_name, "~", paste(X_names, collapse= "+"))) 



# Spliting dataset into training set and test set
train_size = 0.8
indice_train= sample(c(1:nrow(data)), train_size*nrow(data))
train=data[indice_train, ]
test=data[-indice_train, ]
Y_test = test[Y_name] 
X_test = test[X_names]
Y_train = train[Y_name] 
X_train = train[X_names]

results_colnames = c('Algorithm', 'Parameters', 'FrameWork', 'balanced_accuracy', 'accuracy')
results = matrix(ncol=length(results_colnames),nrow=1)

try({
	algo = randomForest(formula, data=train)
	Y_pred = predict(algo, X_test,  type="class")
	results = rbind(results, c('RandomForest', '', 'R', bal_accuracy_vec(Y_test[,1], Y_pred) 
	, accuracy_vec(Y_test[,1], Y_pred) 
	))
	
	
})

results_df = data.frame(results, row.names = NULL)
colnames(results_df) =results_colnames
results_df = results_df[-1,]
write.table(results_df,"scripts_upload/results/results_R.csv", sep = ";",dec='.',quote=FALSE, row.names = FALSE)
