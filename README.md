Sample Java EE Application
==========================

This is a test bed application where I put in as many Java EE technologies as possible primarily to set up the "plumbing".  I will try to limit the external runtime dependencies short of JSF/HTML/CSS add-ons.  

The focus is practicality rather than "artistic elegance" for this project.  So there will likely be some hacky stuff in order to make things work for the WebSphere Application Server Classic.

### Supported Java EE containers

* WebSphere Classic 9.0 (for Production) 
* WebSphere Liberty (for Eclipse debugging or production).

### What we have so far

* CDI
    - `@Produces`
* EJB
    - Stateless Session Bean
    - Singleton with startup
    - `@Scheduled`
* JASPIC
    - Used for `X-Forwarded-User` HTTP header authentication.  The concept can be applied to reverse proxy authentication systems such as [Entrust TruePass](https://github.com/trajano/entrust-truepass) or SiteMinder.
* JPA
    - With automatic integration tests with Eclipse Link
    - Redundant check with Hibernate (which has some issues with it's JPQL implementation)
    - Automatic schema generation
    - `@MappedSuperClass`
* JDBC
    - Raw JDBC is used for really large LOBs using `InputStream`s to improve performance.
    - MariaDB and H2 are supported. 
* Bean Validation
    - Canadian Postal Code validation
    - Canadian SIN validation
* JAX-WS (SOAP)
    - WS-Addressing 
    - MTOM
    - Code first (Java to WSDL)
    - Contract first (WSDL + Schema -> Java)
* RESTful API
* JavaScript project integration
    - Yarn
    - ReactJS
    - Webpack
* JSF
    - [PrimeFaces](https://www.primefaces.org/) for UI
    - [OmniFaces](http://omnifaces.org/) for some common JSF utilities
    - Bean validation integration
    - Simple CRUD application integrating with JPA.

### Additional technologies experimented on

* [Kotlin](https://kotlinlang.org/) support (only for unit tests, does not work with deployed code in Eclipse [KT-14838](https://youtrack.jetbrains.com/issue/KT-14838))
* Natural language processing with [CoreNLP](https://stanfordnlp.github.io/CoreNLP/).
* Machine learning with [Deeplearning4j](https://deeplearning4j.org/).

* JDK 8 build on an isolated EJB.


### TODO

The following technologies are still missing at this point (not necessarily a complete list but they are the ones that are next on queue)

* Batch
* JMS
* MDB
* Java Mail
* Web Sockets
* Web Service Ref
* JPA Relationships
* JPA converters (after WebSphere 9.0.0.4 and Liberty 17.0.0.2 is released to fix the `@Converter` bug)
* Complex JPA Inheritance
* JCache (when Java EE 8 comes out)

### Other Java EE containers

In regards to supporting other containers.

* JBoss/WildFly has issues with their CDI implementation [JBAS08818](https://issues.jboss.org/browse/JBAS-8818) [WFLY-8731](https://issues.jboss.org/browse/WFLY-8731).
* Glassfish will not be supported since it is not being supported by Oracle anyway and it is quite buggy.
* Payara inherits the Glassfish bugs.

## Number of modules

This project **intentionally** tries to reduce the number of modules and only introduces them when needed.  Some reasons are:

* API and implementation separation otherwise there may be duplicate implementations loaded by class loaders.
* Technology differences.  For example use of Kotlin and JDK8 is in a different module.  Different web contexts for different types of API (JAX-RS, JAX-WS, JSF).

* Packaging differences (EAR, WAR, JAR)