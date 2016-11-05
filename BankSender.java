import App.*;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import org.omg.CosNaming.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;

public class BankSender extends Thread {
  private Transaction transaction;
  private InterBank interBank;
  private boolean confirmation;
  public BankSender(Transaction t, InterBank iB, boolean confirmation){
    this.transaction = t;
    this.interBank = iB;
    this.confirmation = confirmation;
  }
  public void run(){
    if(!confirmation)
    {
      this.interBank.envoyerTransaction(this.transaction);
    }
    else
    {
      this.interBank.confirmerTransaction(this.transaction);
    }
  }
}
