# INSTALL
This application was made to be used on Ubuntu (Linux). If you don't have linux, you can create a virtual machine with VMWare for example.
## Requisites
In order to use this application, you need
* **Python version 3.6 and some libraries:**
We can find here https://medium.com/@moreless/install-python-3-6-on-ubuntu-16-04-28791d5c2167 a tutorial to install python3.6. 
You have to install the following packages:
```
$ pip3.6 install pandas
$ pip3.6 install numpy
$ pip3.6 install xgboost
$ pip3.6 install scikit-learn
```
* **R version 3.6.2** with a CRAN mirror because the following packages are needed : yardstick, rminer, rpart, randomForest, nnet, xgboost, e1071
* **Eclipse IDE for Java and DSL Developers**
See : http://www.eclipse.org/downloads/eclipse-packages/
* **Pygments**
This is required for the R and Python code visualisation. It's used to generate the html page with coloring.
* **An internet connection for downloading the R packages required for your analysis**

## Importing the Git project
In the menu of Eclipse IDE, choose File > import > Git > Projects from Git, then choose Clone URI.
Complete the URI field with https://github.com/mathildouk/Genie-Logiciel
Then click on Next until Local Destination and change the directory to the path where you want to download files. Then next and finish.

## Let's start
In order to launch the application, open the file Main.java situated in "MML Webapp/src/main/java/" (in eclipse). Right click > Run as > Java application. This will start Javalin. The application is available under localhost:8080.

To see how to use our application, please read the README.md file.
