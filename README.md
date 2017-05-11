Sample Java EE Application
==========================

This is a test bed application where I put in as many Java EE technologies as possible primarily to set up the "plumbing".  I will try to limit the external runtime dependencies short of JSF/HTML/CSS add-ons.

The focus is practicality rather than "artistic elegance" for this project.  So there will likely be some hacky stuff in order to make things work for the WebSphere Application Server Classic.

### Supported Java EE containers

* WebSphere Classic 9.0 (for Production) 
* WebSphere Liberty (for Eclipse debugging or production).

### What we have so far

* CDI
* EJB
    - Stateless Session Bean
    - Singleton with startup
* JPA
    - With automatic integration tests with Eclipse Link
    - Redundant check with Hibernate (which has some issues with it's JPQL implementation)
* Bean Validation
    - Canadian Postal Code validation
    - Canadian SIN validation
* [Kotlin](https://kotlinlang.org/) support
* SOAP 
    - WS-Addressing 
    - MTOM
* RESTful API
* JSF
    - [PrimeFaces](https://www.primefaces.org/) for UI
    - [OmniFaces](http://omnifaces.org/) for some common JSF utilities
    - Bean validation integration
    - Simple CRUD application integrating with JPA.

### TODO

The following technologies are still missing at this point (not necessarily a complete list but they are the ones that are next on queue)

* Batch
* JMS
* MDB
* Java Mail
* Web Sockets
* Web Service Ref
* JPA Relationships
* JPA converters (after WebSphere 9.0.0.4 and Liberty 17.0.0.2 is released to fix the @Converter bug)
* Complex JPA Inheritance
* JCache (when Java EE 8 comes out)

### Other Java EE containers

In regards to supporting other containers.

* JBoss/WildFly has issues with their CDI implementation.
* Glassfish will not be supported since it is not being supported by Oracle anyway and it is quite buggy.
* Payara inherits the Glassfish bugs.
