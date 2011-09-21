package no.mesan.js;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * kjør som en vanlig java-klasse, i debug modus.
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
