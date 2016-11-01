# Car Deployer

This project is to take a look at how to programatically deploy car files
into WSO2. It leans heavily on libraries used in this project
[here](https://github.com/wso2/maven-tools/tree/master/maven-car-deploy-plugin). 

I would like to get it to the point where it will be able to take a request
to deploy a car, check if the car is deployed, if it is then do nothing, if not
then deploy it.

Currently there are no unit tests around the code as I am just trying to get
something working, testing and refactoring will come later. So far the project
is able to deploy a car by running the main method on the Test class
uk.co.mruoc.wso2.Test. You will need to modify the host url being passed to
point at whichever WSO2 component you want to deploy your Car into.