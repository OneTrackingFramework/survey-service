package one.tracking.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import one.tracking.framework.config.RequestLoggingProperties;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = RequestLoggingProperties.class)
public class SurveyApplication {

  public static void main(final String[] args) {
    SpringApplication.run(SurveyApplication.class, args);
  }

}
