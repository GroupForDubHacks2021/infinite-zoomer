# infinite-zoomer
![B2707821-572A-4721-A6B8-11894E5E6AA3](https://user-images.githubusercontent.com/46334387/138604706-bcccccc2-bbd2-473e-8884-59b2e4e344a3.png)

A DubHacks 2021 submission. See [its DevPost](https://devpost.com/software/infinite-zoomer?ref_content=my-projects-tab&ref_feature=my_projects) site!

## Building & Running

This project is built in JavaScript and Java. It uses gradle and the main folder is an IntelliJ IDEA project.

To download and run, first make sure you have [Gradle](https://gradle.org/) and JDK 16!

From a terminal:
```
$ git clone git@github.com:GroupForDubHacks2021/infinite-zoomer.git
$ cd infinite-zoomer
$ gradle build
$ gradle run
```

This will start a server on port `8000` of your computer. You can view it on that computer's browser by navigating to `http://localhost:8000/`.

If connecting from a remote machine,
 * make sure you're on the same WIFI network as the host machine
 * In a web-browser, visit `http://host-machine-ip-address-here:8000` replacing `host-machine-ip-address-here` with the IP address of the computer with the server.
   * On Linux, you can get this address by running `hostname -I`. It should look something like `192.168.1.16`.
   * Some devices might have firewalls that block the server! Make sure your firewall is configured to allow remote communication over port `8000`!

## Demo a serverless version

Although Infinite Zoomer works best if connected to a server (better performance), you can demo it (serverless!) here: https://groupfordubhacks2021.github.io/infinite-zoomer/app/src/main/resources/html/index.html
