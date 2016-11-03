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
  private ArrayList<int> list_back_num = new ArrayList<int>();
  private ArrayList<Transaction> list_transaction = new ArrayList<Transaction>();
  public boolean envoyerTransaction(Transaction t)
  {
    return true;
  }
  public boolean confirmerTransaction(Transaction t)
  {
    return true;
  }
  public boolean join(int num)
  {
      list_back_num.add(num);
      return true;
  }
}
