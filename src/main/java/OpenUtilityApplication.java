import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OpenUtilityApplication
{
    public static void main( String[] args )
    {
        ConfigurableApplicationContext context = SpringApplication.run(OpenUtilityApplication.class, args);

    }
}
