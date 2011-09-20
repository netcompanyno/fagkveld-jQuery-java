package no.mesan.js;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Starter Jetty med eVurdering indre sone, for bruk utenfor server-støtten i IntelliJ IDEA og MyEclipse.
 *
 * <p>For å bruke denne klassen trenger du en mappe i prosjektet hvor property filene kan ligge.
 * F.eks <code>evurdering/run</code>. I run mappen kopierer du filene som normalt kopieres
 * til Jetty mappen. Lag et nytt Eclipse prosjekt som peker til run mappen.
 * Propertiene som normalt settes opp rett på config til serveren i MyEclipse
 * settes isteden opp som vm arguments i denne klassens run configuration. Run mappen må
 * settes som working directory på samme fane.
 *
 * @author tmo
 */
public final class JettyStarter {

    private static Server SERVER;

    private JettyStarter() {

    }

    @SuppressWarnings({ "unchecked" })
    public static void main(final String[] args) throws Exception {

        SERVER = new Server(8080);

        final WebAppContext context = new WebAppContext();
        context.setServer(SERVER);
        context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        context.getInitParams().put("useFileMappedBuffer", "false");
        context.setContextPath("/");
        context.setWar("src/main/webapp");

        SERVER.setHandler(context);
        SERVER.start();
        SERVER.join();
    }
}
