import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;


class BankImplTransaction extends BankTransactionPOA {
  private Bank bankRef;

  public BankImplTransaction(Bank b)
  {
    this.bankRef = b;
  }

  public int get_num()
  {
    return this.bankRef.get_num();
  }

  public boolean recevoirTransaction(Transaction t)
  {
    return this.bankRef.recevoirTransaction(t);
  }
  public boolean confirmerTransaction(Transaction t)
  {
    System.out.println("Received Confirmation: "+t.amount+"$ will be deduct from account number "+t.account_source);
    this.bankRef.withdrawAccount(t.amount, t.account_source);
    return true;
  }
}
