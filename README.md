# spring-security-jwt
 

# swagger & openAPI 3.0
# springdoc-openapi v1.6.4
https://springdoc.org/#modules
# swagger to pdf website 
https://www.swdoc.org/


# How to Build a Java RESTful API with Spring Boot
# standard document 
https://rapidapi.com/blog/how-to-build-an-api-with-java/
## swagger to pdf
can modify your REST project, so as to produce the needed static documents (html, pdf etc) upon building the project.

If you have a Java Maven project you can use the pom snippet below. It uses a series of plugins to generate a pdf and an html documentation (of the project's REST resources).

    rest-api -> swagger.json : swagger-maven-plugin
    swagger.json -> Asciidoc : swagger2markup-maven-plugin
    Asciidoc -> PDF : asciidoctor-maven-plugin

Please be aware that the order of execution matters, since the output of one plugin, becomes the input to the next:
```
<plugin>
    <groupId>com.github.kongchen</groupId>
    <artifactId>swagger-maven-plugin</artifactId>
    <version>3.1.3</version>
    <configuration>
        <apiSources>
            <apiSource>
                <springmvc>false</springmvc>
                <locations>some.package</locations>
                <basePath>/api</basePath>
                <info>
                    <title>Put your REST service's name here</title>
                    <description>Add some description</description>
                    <version>v1</version>
                </info>
                <swaggerDirectory>${project.build.directory}/api</swaggerDirectory>
                <attachSwaggerArtifact>true</attachSwaggerArtifact>
            </apiSource>
        </apiSources>
    </configuration>
    <executions>
        <execution>
            <phase>${phase.generate-documentation}</phase>
            <!-- fx process-classes phase -->
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
<plugin>
    <groupId>io.github.robwin</groupId>
    <artifactId>swagger2markup-maven-plugin</artifactId>
    <version>0.9.3</version>
    <configuration>
        <inputDirectory>${project.build.directory}/api</inputDirectory>
        <outputDirectory>${generated.asciidoc.directory}</outputDirectory>
        <!-- specify location to place asciidoc files -->
        <markupLanguage>asciidoc</markupLanguage>
    </configuration>
    <executions>
        <execution>
            <phase>${phase.generate-documentation}</phase>
            <goals>
                <goal>process-swagger</goal>
            </goals>
        </execution>
    </executions>
</plugin>
<plugin>
    <groupId>org.asciidoctor</groupId>
    <artifactId>asciidoctor-maven-plugin</artifactId>
    <version>1.5.3</version>
    <dependencies>
        <dependency>
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctorj-pdf</artifactId>
            <version>1.5.0-alpha.11</version>
        </dependency>
        <dependency>
            <groupId>org.jruby</groupId>
            <artifactId>jruby-complete</artifactId>
            <version>1.7.21</version>
        </dependency>
    </dependencies>
    <configuration>
        <sourceDirectory>${asciidoctor.input.directory}</sourceDirectory>
        <!-- You will need to create an .adoc file. This is the input to this plugin -->
        <sourceDocumentName>swagger.adoc</sourceDocumentName>
        <attributes>
            <doctype>book</doctype>
            <toc>left</toc>
            <toclevels>2</toclevels>
            <generated>${generated.asciidoc.directory}</generated>
            <!-- this path is referenced in swagger.adoc file. The given file will simply 
                point to the previously create adoc files/assemble them. -->
        </attributes>
    </configuration>
    <executions>
        <execution>
            <id>asciidoc-to-html</id>
            <phase>${phase.generate-documentation}</phase>
            <goals>
                <goal>process-asciidoc</goal>
            </goals>
            <configuration>
                <backend>html5</backend>
                <outputDirectory>${generated.html.directory}</outputDirectory>
                <!-- specify location to place html file -->
            </configuration>
        </execution>
        <execution>
            <id>asciidoc-to-pdf</id>
            <phase>${phase.generate-documentation}</phase>
            <goals>
                <goal>process-asciidoc</goal>
            </goals>
            <configuration>
                <backend>pdf</backend>
                <outputDirectory>${generated.pdf.directory}</outputDirectory>
                <!-- specify location to place pdf file -->
            </configuration>
        </execution>
    </executions>
</plugin>
```

The asciidoctor plugin assumes the existence of an .adoc file to work on. You can create one that simply collects the ones that were created by the swagger2markup plugin:

include::{generated}/overview.adoc[]
include::{generated}/paths.adoc[]
include::{generated}/definitions.adoc[]

If you want your generated html document to become part of your war file you have to make sure that it is present on the top level - static files in the WEB-INF folder will not be served. You can do this in the maven-war-plugin:


```
<plugin>
    <artifactId>maven-war-plugin</artifactId>
    <configuration>
        <warSourceDirectory>WebContent</warSourceDirectory>
        <failOnMissingWebXml>false</failOnMissingWebXml>
        <webResources>
            <resource>
                <directory>${generated.html.directory}</directory>
            <!-- Add swagger.pdf to WAR file, so as to make it available as static content. -->
            </resource>
            <resource>
                <directory>${generated.pdf.directory}</directory>
            <!-- Add swagger.html to WAR file, so as to make it available as static content. -->
            </resource>
        </webResources>
    </configuration>
</plugin>
```

The war plugin works on the generated documentation - as such, you must make sure that those plugins have been executed in an earlier phase.


----------------------------------------------------------------------------------
# Handling io.jsonwebtoken.ExpiredJwtException
Your GlobalExceptionHandler isn't truly global, it will only catch exceptions that occur in your controller (hence ControllerAdvice), the exceptions you are running into are occurring in servlet filters, which is where Spring Security does pretty much all of its work. This little chart may help explain what I am talking about:

PreFilters <- Executed before entering your controller, decryption of JWT is happening here

Controller <- ControllerAdvice will catch all exceptions thrown here

PostFilters <- Executed after exiting your controller

Luckily Spring Security already has mechanisms in place for handling exceptions that occur when doing things like decrypting a JWT in a filter. You will want to update your SpringSecurityConfig like so. Note its important that the ExceptionTranslationFilter is after your StatelessAuthenticationFilter (or whatever you named the filter where the JWT decryption is occurring).