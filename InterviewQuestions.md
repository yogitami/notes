### Spring Boot Interview Questions :

 1. What is an embedded container in spring boot and why is it important?
    - Tomcat is a lightweight server that can run inside a Springboot application. It is lightweight because it had to be integrated with the framework and make our application to run as a standalone application. It has default configurations. It eliminates the need for developers to deploy their applications on external containers like Tomcat or Jetty, making it easier to develop & deploy applications.
    - It also supports hot reloading through some dev tools.

2. What is hot reloading.
   - Hot reloading allows developers to make changes to their code, resources or configuration files while an application is running, and see those changes immediately reflected without needing to restart the application.
   - How does it work.
     - Hot reloading is facilitated through the use of development tools and libraries that support dynamic reloading of resources, configuration and code changes. Springboot itself doesn't provide native hot reloading capabilites, it integrates seamlessly with third party tools & frameworks that enable this functionality. It works based on following steps :
       1. Monitoring for changes : continously monitors. The monitoring can be done using file system watchers, event listeners,etc.
       2. Detecting Change : identifies the nature of the modification(adding,modifying,deleting) & determines the scope of the change.
       3. Applying changes dynamically : involves reloading the modified class, recompiling changed resources, or reinitializing updated configurations.
       4. Preparing Application State : to ensure a seamless user experience, hot reloading mechanism strive to preserve the application's state during the reloading process. This includes maintaining existing session data, keeping connections alive and minimizing disruptions to ongoing transactaions or background proccess
       5. Refreshing Application Context
       6. Feedback and verification
       7. Incremental compilation and deployment

  3. How can you externalise the configuration properties.
     - By using applicaition.property file & then using @Value("${property key name}") in the code to use it.
     - By using environment variables.
     - VM options
