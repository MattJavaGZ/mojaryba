package matt.pass.mojaryba.web.util;


public class RedirectUtils {

    public static String safeReturn(String referer) {
        if (referer == null) {
            return "/";
        }
        return referer;
    }
}
