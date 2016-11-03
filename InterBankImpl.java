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
  public boolean create(int num)
  {
    Account new_account = new Account(num);
    this.list_account.add(new_account);
    return true;
  }
  public boolean destroy(int num)
  {
    this.list_account.remove(this.find_account(num));
    return true;
  }
  private Account find_account(int num)
  {
    Account toReturn = null;
    for (Iterator<Account> iter = this.list_account.listIterator(); iter.hasNext(); )
    {
      Account a = iter.next();
      if (a.get_account_num() == num)
      {
          toReturn = a;
      }
    }
    return toReturn;
  }
  public boolean deposit(int amount, int num)
  {
    Account a = this.find_account(num);
    a.deposit(amount);
    return true;
  }
  public boolean withdraw(int amount, int num)
  {
    Account a = this.find_account(num);
    a.withdraw(amount);
    return true;
  }
  public boolean transfert(int amount, int bankNum, int accountNum)
  {
    return true;
  }
  public int balance(int num)
  {
    Account a = this.find_account(num);
    return a.balance();
  }
  public boolean recevoirTransaction(Transaction t)
  {
    return true;
  }
  public boolean confirmerTransaction(Transaction t)
  {
    return true;
  }
}
