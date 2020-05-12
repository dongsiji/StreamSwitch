package	streamswitch-core; 
 
import org.apache.samza.config.Config;

public interface OperatorControllerFactory {
    OperatorController getController(Config config);
}
