import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import org.omg.CosNaming.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class AppBank
{
  public static void main(String args[])
  {
    InterBank interbank = null;
    try
    {
      /* CLIENT of InterBanK */
     System.out.println(args[0]);
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

     int numero_bank = Integer.parseInt(args[0]);
     boolean ok = interbank.join(numero_bank);


      int input = 9;
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      while (input != 0)
      {
        System.out.println("Operation:");
        System.out.println("0 - Quit");
        //br = new BufferedReader(new InputStreamReader(System.in));
        input = Integer.parseInt(br.readLine());
        switch (input)
        {
          case 0:
            interbank.desactivate_bank(numero_bank);
            break;
          default:
            input = 0;
            interbank.desactivate_bank(numero_bank);
            break;
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("HelloInterBank Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
