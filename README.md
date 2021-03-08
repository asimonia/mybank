### Java RESTful service without Spring

#### Dependencies and plugins used 
- org.apache.tomcat.embed - embedded tomcat
- com.fasterxml.jackson.core - serves JSON
- maven shade plugin to create manifest and package 3rd party libaries in fat JAR
- spring-context - DI container for ApplicationContext
- javax.annotation-api - annotations for pre and post bean construct 


#### Application notes
1. Application launcher runs the embedded tomcat, opens up a port, adds the servlet and starts to listen to HTTP requests
2. Structure app with a poor man's DI container in context package, with singletons to inject dependencies.  
3. Create a model, service and web layer
4. Servlet accepts GET and POST requests
5. No database for now - store JSON in a list 
6. Accept an amount and reference for creating new transactions



0. The interface ApplicationContext represent the Spring IoC container.  It provides basic functionalities for managing beans.

1. Spring needs a configuration class in order to construct an ApplicationContext.

2. @Configuration marks the container and the bean definitions are @Bean
Historically, you did not write Java classes to configure Spring. Instead, you wrote XML files.

3. You need to initiate a AnnotationConfigApplicationContext inside the initialization servlet in order to use the @Configuration and @Bean and inject the dependencies.

By default, all beans are singletons. @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)

////////////////////////////////////////

So far you explicitly had to write @Bean factory methods in your @Configuration class.

Wouldn’t it be much nicer if you were to get rid of them and Spring somehow figured out what @Beans your application consists of?

This is where two annotations, @Component and @ComponentScan come in.

1. @Component marks for dependency injection
2. @ComponentScan tells Spring where to scan for these annotations.  This will scan the base package. If you want to scan outside of the base, you have to be explicit.

Takeaway: If you want to create Spring beans from 3rd party libraries that don’t offer explicit Spring support, you will have to fall-back to @Bean methods.

The answer is, that @ComponentScan, by default, only scans the package and all the sub-packages of its annotated class.

@ComponentScan(basePackageClasses = ApplicationLauncher.class)

@Service, @Repository, @Controller ARE EQUIVALENT to @Component (they just have semantic meanining)

////////////////////////////////////////////////

Again, the advantage of setter injection vs field injection, is when you are using your class outside of a Spring context, e.g. for testing. Why? Because with direct field injection, you cannot set e.g. the userService yourself!


Lifecycle annotations are call some logic after the bean is created or before it is destroyed: @PostConstruct @PreDestroy

////////////////////////////////////////////////

Use this annotation on your configuration class to tell Spring where to find the properties
@PropertySource("classpath:/application.properties")

Inject properties with this
@Value("${property.url}")

You can specify profiles for different loading environments (qa, dev, prod)
@Profile("dev")

You can activate them with:
VM Option: -Dspring.profiles.active=dev
https://www.baeldung.com/spring-profiles#2-using-springactiveprofile

////////////////////////////////////////////////
Spring MVC’s own servlet, called DispatcherServlet, comes bundled with Spring MVC.  It takes invoicing requests and forwards to the proper controller by URI

@RestController → @Controller + @ResponseBody

@GetMapping
@PutMapping
@PostMapping
@DeleteMapping

@RequestParam → http://localhost:8080/invoices?user_id=franz&amount=50
@PathVariable → http://localhost:8080/invoices/franz/50
@RequestBody *application/json* in the content-type for POST requests

@EnableWebMVC
Looks at classpath. And if it finds the JSON library, it automatically creates a Spring JSON converter. If it finds an XML library, it automatically creates an XML converter.
This means your application now has two converters (XML and JSON) and they have a certain order with different priorities.
The XML converter has a higher default priority, that is why you are now receiving XML, instead of JSON.

Content negotation. Spring understands what data format you send in and what you expect as a result, by looking at HTTP headers.

////////////////////////////////////////////////

Hibernate has a server side validation API for incoming requests.  

@Valid tells Spring to actually perform this validation

@Validated tells Spring that the controller is validated

@AssertFalse / @AssertTrue → makes sure that a boolean field is set to false / true.
@DecimalMin / @DecimalMax → makes sure that a number (BigDecimal, BigInteger, CharSequence, byte, short, int, long etc.) is >= or ⇐ a value. It’s an equivalent of @Min, @Max you used above.
@Digits
@Email → the string needs to be a well-formed e-mail address
@Future / @FutureOrPresent → a date (pre Java 8 types and Java8+ types) needs to be in the future or present
@Min / @Max → same as DecimalMin / DecimalMax
@Negative / @NegativeOrZero → self-explanatory
@NotBlank / @NotEmpty → a string must not be blank or empty
@Null / @NotNull → self-explanatory
@Past / @PastOrPresent → a date (pre Java 8 types and Java8+ types) needs to be in the future or present
@Pattern → a string needs to match a regex pattern
@Positive / @PositiveOrZero → self-explanatory
@Size → the element size must be between a boundary. valid for strings, collections, maps, arrays.


@RestControllerAdvice - returns JSON with a HTTP 400 or 500 status codes

/////////////////////////////////////////////////

To connect to a database with Java and Spring, you need two things:

- database
- JDBC driver
- DataSource config bean

DataSource is the interface that is implemented by a vendor to connect and manage those connections to the database

You can also place your sql scripts in a directory to create the schema.
spring-jdbc is one of the oldest available core Spring framework modules, and a tiny wrapper around plain JDBC

JDBCTemplate is the low level Spring way of executing queries




to enable transaction management:

@EnableTransactionManagement

@Bean
public TransactionManager platformTransactionManager() {
    return new DataSourceTransactionManager(dataSource());
}
