package pe.upc.mckchain.tools;

import javax.servlet.http.HttpServletRequest;

public class UtilityMckchain {

    public static String GenerarUrl(HttpServletRequest request) {

        String url = request.getRequestURL().toString();

        return url.replace(request.getServletPath(), "");
    }
}
