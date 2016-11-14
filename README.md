# WSO2 Car Deployer

This library provides an abstraction layer to try and make it easy to
programatically deploy and undeploy car files into WSO2 components using
libraries published by WSO2. It also provides the ability to check that
files have been deployed and undeployed successfully.

## Deploying Car Files

It is possible to deploy a car file by specifying a server url (for the
WSO2 component you want to deploy the car into) along with a username
and password that has access to deploy car files on the server. A
simple example of deploying a single car file is shown below:

```
// create a file handle to the car file to be deployed
File file = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");

// set up a config object with details on the WSO2 server and username/password
// and pass it into a stub factory
Config config = new ConfigBuilder()
    .setUrl("https://0.0.0.0:9443/")
    .setUsername("admin")
    .setPassword("admin")
    .build();
StubFactory stubFactory = new StubFactory(config);

// finally use the stub factory to create a car deployer and deploy the car
CarDeployer deployer = new CarDeployer(stubFactory);
deployer.deploy(file);
```

## Undeploying Car Files

It is also possible to undeploy a car file by specifying the same details
as outlined above, again a simple example is shown below:

```
// create a file handle to the car file to be undeployed
File file = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");

// set up a config object with details on the WSO2 server and username/password
// and pass it into a stub factory
Config config = new ConfigBuilder()
    .setUrl("https://0.0.0.0:9443/")
    .setUsername("admin")
    .setPassword("admin")
    .build();
StubFactory stubFactory = new StubFactory(config);

// finally use the stub factory to create a car undeployer and undeploy the car
CarUndeployer undeployer = new CarUndeployer(stubFactory);
undeployer.undeploy(file);
```

## Verifying Car Deployment / Undeployment

Finally, it is possible to check whether or not a car file is already
deployed or not, this can be done by making a one of call, or by multiple
calls within a maximum timeout, this is useful when you have just deployed
or undeployed a file, as the status is not updated synchronously after a
deployment or undeployment, the RetriabledDeploymentChecker is designed
to handle this for you whilst hiding any of the complexity. Examples of
both a regular DeploymentCheck and a RetriableDeploymentCheck are shown
below:

```
// create a file handle to the car file to be undeployed
File file = new File("test/json-validator-mediator-config-local-1.0.0-SNAPSHOT.car");

// set up a config object with details on the WSO2 server and username/password
// and pass it into a stub factory
Config config = new ConfigBuilder()
    .setUrl("https://0.0.0.0:9443/")
    .setUsername("admin")
    .setPassword("admin")
    .build();
StubFactory stubFactory = new StubFactory(config);

// finally use the stub factory to create a DeploymentChecker or
// RetriableDeploymentChecker
DeploymentChecker deploymentChecker = new DeploymentChecker(stubFactory);
RetriableDeploymentChecker retriableDeploymentChecker = new RetriableDeploymentChecker(stubFactory);

// will do a single check and return true if the car file is deployed,
// and false if the car file is not deployed
deploymentChecker.isDeployed(file);

// will do multiple checks until the file is deployed and method returns
// true as soon as the file is deployed, or false if it is not deployed
// after the timeout (defaults to 20 seconds, and can be overridden by
// passing a constructor argument) expires
retriableDeploymentChecker.isDeployed(file);

// will do multiple checks until the file is undeployed and method returns
// true as soon as the file is undeployed, or false if it is deployed
// after the timeout (defaults to 20 seconds, and can be overridden by
// passing a constructor argument) expires
retriableDeploymentChecker.isUndeployed(file);
```

## Running the tests

This project is covered by both unit tests and integration tests. The
integration tests make use of docker, so you will need to have a docker
daemon running on your machine for them to work.

The integration tests also take around 2 or 3 minutes to run, this is
why they have been split out from the unit tests so each set of tests
can be run independently.

### Running the unit tests

To run just the unit tests you can run the command:

```
gradlew clean test
```

or

```
gradlew clean build -x integrationTest
```

### Running the integration tests

The integration tests make use of docker, so you will need to have a docker
daemon running on your machine for them to work.

To run just the integration tests you can run the command:

```
gradlew clean integrationTest
```

or

```
gradlew clean build -x test
```