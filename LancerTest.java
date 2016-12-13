import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import org.omg.CosNaming.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class LancerTest {

  public static void main(String... args) {
    boolean estMisAssertion = false;
    assert estMisAssertion = true;

    if (!estMisAssertion) {
      System.out.println("Execution impossible sans l'option -ea");
      return;
    }
    //static private void lancer(Class c){
    //throws Exception;
    // }

    InterBank interbank = null;
    int numero_bank = 20;
    int numero_bank2 = 21;
    try
    {
      /* CLIENT of InterBanK */
      // create and initialize the ORB
      org.omg.CORBA.Object objRef;
      String test[] = new String[2];
      test[0] = "-ORBInitRef";
      test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
      ORB orb = ORB.init(test, null);
      // get the naming service
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      // resolve the object reference from the naming service
      objRef = ncRef.resolve_str("app.interbank");
      // convert the CORBA object reference into Echo reference
      interbank = InterBankHelper.narrow(objRef);

      boolean ok = interbank.join(numero_bank);
      boolean ok2 = interbank.join(numero_bank2);
    }
    catch (Exception e)
    {
      System.out.println("HelloInterBank Exception: " + e.getMessage());
      e.printStackTrace();
    }
    TestClient t = new TestClient(numero_bank,numero_bank2);
    t.lancer();
    interbank.desactivate_bank(numero_bank);
    interbank.desactivate_bank(numero_bank2);
  }
}
