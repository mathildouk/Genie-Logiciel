# MML-web application  

## Software engineering Project 

### Céline DUVAL - Renan BERNARD - Mathilde LETOUQ - Nadège CHOJNACKI - Elena MOROIANU 


## The context of this project:  

This project is the common theme of several classes aimed at introducing us to the world of web applications with back and front ends. The idea was to learn the skills required to create a web application through several practical sessions in order to later apply them to this project. Moreover, to connect this project to our other courses, we were asked to create a web application that would apply different classification algorithms to a data-set specified by the user. The user will choose algorithms he wants to perform. And the application will return codes in selected languages if the user wants to perform by him-self these algorithms. Or the user can compare the algorithms on several criterion as the accuracy of the model. 
 

## Man-Machine Language (MML) 

Man-machine language is a specification language. That means that it's a language used to create new languages. And this new language is often a standardised version of different languages.   

During our project, we created a MML language about machine learning. Indeed, many languages have their own machine learning algorithm. But each language has its own specification, implementation and vocabulary.  
 
Or goal is to create a new machine learning language. This new language will be easier to understand and easier to learn. Indeed, it is used only for machine learning purpose so the syntax is conceived exclusively for this.  

First, we tried to create our MML language. But, for the practical sessions and project, we used a language created by our teacher. We chose to implement two languages (R and the framework scikit-learn from Python) and five differents algorithms:  

* Decision Tree 

* Random Forest 

* SVM 

* Logistic Regression 

* XGBoost 


## The website architecture:  
 
### Configuration pages:  

On the homepage, you can choose between two ways to choose the parameters of our application: the editor or the form. 

For information, the **cross-validation in R is not implemented in our application**. 

 
#### Editor  

In this editor, you have to write your programme in our mml language. 

For the syntax, you can read the **mml_language.md** files. 

#### Form 

Unlike in the editor, here everything is easy. Indeed, you don't have to know our mml language you just to click on the different buttons! 

First, you can choose the files in which the algorithm will perform. The interest variable is the last column of the csv files. 

After you can choose the algorithm, the language and the parameters you want. When the form is complete and good for you, you can submit it. 

### Results pages 

After submitting the form or the mml code, you arrive on an intermediate page. We will be able to choose between the visualisation of the code or the comparison of the algorithms.  

#### The obtained code 

In these pages, you can visualise the code created by our application with the parameters you chose in the first part. The code is highlight with the tool pygment. 

If you only chose R or Python algorithm, one of the sides will be empty with error but it's normal. 

#### The comparison of the different algorithm 

These pages contain a table with the performance of the different algorithms. So, you can compare algorithms about four criterions: accuracy, f, recall and macro f. 


## **We hope you will appreciate our web application!**
