
import org.omg.CORBA.*;

public class ServerThread extends Thread {
  private ORB orb;
  public ServerThread(ORB orb){
    this.orb = orb;
  }
  public void run(){
    orb.run();
  }
}
