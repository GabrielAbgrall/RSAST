package fr.gabrielabgrall.dmst;

import java.util.HashMap;
import java.util.Map;

import fr.gabrielabgrall.dmst.app.client.Client;
import fr.gabrielabgrall.dmst.app.server.Server;
import fr.gabrielabgrall.dmst.utils.Debug;

public class DSAST {

    protected static final Map<String, String> ALIASES = new HashMap<>();
    
    static {
        ALIASES.put("s", "server");
        ALIASES.put("c", "client");
        ALIASES.put("u", "userinterface");

        ALIASES.put("p", "port");
        ALIASES.put("h", "host");
        ALIASES.put("n", "name");
    }

    public static void main(String[] args) throws InvalidArgumentException {
        launchApp(buildArgs(args));
    }

    public static void launchApp(Map<String, String> args) throws InvalidArgumentException{
        if(!args.containsKey("no-debug")) Debug.setDebug(true);

        String name = args.get("name");
        String host = args.get("host");
        int port = -1;
        try {
            if(args.containsKey("port")) port = Integer.parseInt(args.get("port"));
        } catch (NumberFormatException _) {
        }

        if(args.containsKey("server")) {
            if(port == -1) throw new InvalidArgumentException("Invalid or missing port number.");
            Server server = new Server(port);
            server.start();
        }

        if(args.containsKey("client")) {
            if(port == -1) throw new InvalidArgumentException("Invalid or missing port number.");
            if(name == null) throw new InvalidArgumentException("Invalid or missing name.");
            if(host == null) throw new InvalidArgumentException("Invalid or missing host name or address.");
            Client client = new Client(name, host, port);
            client.start();
        }
    }

    public static Map<String, String> buildArgs(String[] args) {
        Map<String, String> r = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(arg.startsWith("-") && !arg.startsWith("--")) {
                String alias = arg.substring(1, arg.length());
                if(ALIASES.containsKey(alias)) arg = "--" + ALIASES.get(alias);
            }
            if(arg.startsWith("--")) {
                String k = arg.substring(2, arg.length());
                String v = null;
                if(i+1<args.length && !args[i+1].startsWith("-")) v = args[i+1];
                r.put(k, v);
            }
        }
        return r;
    }
}
