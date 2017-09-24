package pac;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Dmytro Tymoshenko on 13.04.17.
 */
@Configuration
@ComponentScan(basePackages = "pac")
@PropertySource("classpath:googleApi.properties")
public class ModuleConfig {

    @Value("${sheetsFolderPath}")
    private String sheetsFolderPath;
    @Value("${client_secret}")
    private String client_secret;

    @Bean
    public GoogleApi getGoogleApi(){
        return new GoogleApi(sheetsFolderPath, client_secret);
    }

//    @Bean
//    public ModuleFile getModuleFile() throws IOException {
//        return new ModuleFIleImpl();
//    }
}
