package pe.upc.mckchain.mail;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${spring.sendgrid.api-key}")
    private String apiKey;

    public SendGrid getSendgrid() {
        return new SendGrid(apiKey);
    }
}
