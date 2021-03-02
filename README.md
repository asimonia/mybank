### Java RESTful service without Spring

#### Dependencies and plugins used 
- org.apache.tomcat.embed - embedded tomcat
- com.fasterxml.jackson.core - serves JSON
- maven shade plugin to create manifest and package 3rd party libaries in fat JAR

#### Application notes
1. Application launcher runs the embedded tomcat, opens up a port, adds the servlet and starts to listen to HTTP requests
2. Structure app with a poor man's DI container in context package, with singletons to inject dependencies.  
3. Create a model, service and web layer
4. Servlet accepts GET and POST requests
5. No database for now - store JSON in a list 
6. Accept an amount and reference for creating new transactions

