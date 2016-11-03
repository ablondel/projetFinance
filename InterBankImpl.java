import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;


class InterBankImpl extends InterBankPOA
{
  private ArrayList<Bank> list_bank = new ArrayList<Bank>();
  private ArrayList<Transaction> list_transaction = new ArrayList<Transaction>();
  public boolean envoyerTransaction(Transaction t)
  {
    return true;
  }
  public boolean confirmerTransaction(Transaction t)
  {
    return true;
  }
}
