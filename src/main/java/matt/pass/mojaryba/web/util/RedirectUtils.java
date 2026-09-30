package matt.pass.mojaryba.web.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class RedirectUtils {

    private final URI applicationUri;

    public RedirectUtils(@Value("${app.url}") String applicationUrl) {
        this.applicationUri = URI.create(applicationUrl);
    }


    public String safeReturn(String referer) {
        if (referer == null || referer.isBlank()) {
            return "/";
        }

        try {
            URI uri = URI.create(referer);

            if (!applicationUri.getHost().equals(uri.getHost())) {
                return "/";
            }

            String path = uri.getRawPath();

            if (path == null || !path.startsWith("/") || path.startsWith("//")) {
                return "/";
            }

            String query = uri.getRawQuery();

            return query == null ? path : path + "?" + query;

        } catch (IllegalArgumentException e) {
            return "/";
        }
    }
}
