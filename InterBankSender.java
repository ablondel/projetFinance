import App.*;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import org.omg.CosNaming.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;

public class InterBankSender extends Thread {
  private Transaction transaction;
  private BankTransaction bank;
  private boolean confirmation;
  public InterBankSender(Transaction t, BankTransaction b, boolean confirmation){
    this.transaction = t;
    this.bank = b;
    this.confirmation = confirmation;
  }
  public void run(){
    if(!confirmation)
    {
      this.bank.recevoirTransaction(this.transaction);
    }
    else
    {
      this.bank.confirmerTransaction(this.transaction);
    }
  }
}
