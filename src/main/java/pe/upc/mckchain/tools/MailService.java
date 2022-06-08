package pe.upc.mckchain.tools;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    final
    SendGrid sendGrid;

    final
    TemplateEngine templateEngine;

    @Value("${image.logo.url}")
    private String img_logo;

    @Value("${email.sender}")
    private String system_mail;

    public MailService(SendGrid sendGrid, TemplateEngine templateEngine) {
        this.sendGrid = sendGrid;
        this.templateEngine = templateEngine;
    }

    private static Map<String, Object> SetParameters(String img_logo, String url, String system_mail) {

        Map<String, Object> model = new HashMap<>();

        model.put("img_logo", img_logo);
        model.put("url", url);
        model.put("system_mail", system_mail);

        return model;
    }

    public void UserRequestEmail(String email, String url) {

        String asunto = "Solicitud de Registro";

        Context context = new Context();

        context.setVariables(SetParameters(img_logo, url, system_mail));

        String html_template = templateEngine.process("userrequest-mailtemplate", context);

        MailStructure(email, system_mail, asunto, html_template);
    }

    public void CompradorVerifyEmail(String email, String url) {

        String asunto = "Verificación de Cuenta";

        Context context = new Context();

        context.setVariables(SetParameters(img_logo, url, system_mail));

        String html_template = templateEngine.process("check-mail", context);

        MailStructure(email, system_mail, asunto, html_template);
    }

    public void RestorePassword(String email, String url) {

        String asunto = "Solicitud de Restablecimiento de Contraseña";

        Context context = new Context();

        context.setVariables(SetParameters(img_logo, url, system_mail));

        String html_template = templateEngine.process("restorepassword-mailtemplate", context);

        MailStructure(email, system_mail, asunto, html_template);
    }

    public void SignupMineraRequestApproved(String email, String url) {

        String asunto = "Estado de Solicitud a Mckchain";

        Context context = new Context();

        context.setVariables(SetParameters(img_logo, url, system_mail));

        String html_template = templateEngine.process("request-approved", context);

        MailStructure(email, system_mail, asunto, html_template);
    }

    public void SignupMineraRequestRejected(String email) {

        String asunto = "Estado de Solicitud a Mckchain";

        Context context = new Context();

        context.setVariables(SetParameters(img_logo, "https://formalizacionminera.minem.gob.pe", system_mail));

        String html_template = templateEngine.process("request-rejected", context);

        MailStructure(email, system_mail, asunto, html_template);
    }

    private void MailStructure(String email, String system_mail, String asunto, String html_template) {
        Mail mail = new Mail(
                new Email(system_mail),
                asunto,
                new Email(email),
                new Content("text/html", html_template)
        );

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            this.sendGrid.api(request);
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }
}
