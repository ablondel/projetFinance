import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;


class BankImplCustomer extends BankCustomerPOA {
  private Bank bankRef;

  public BankImplCustomer(Bank b)
  {
    this.bankRef = b;
  }

  public int get_num()
  {
    return this.bankRef.get_num();
  }
  public boolean create(int num)
  {
    this.bankRef.create(num);

    return true;
  }

  public boolean destroy(int num)
  {
    this.bankRef.rmAccount(num);
    return true;
  }


  public boolean deposit(int amount, int num)
  {
    return this.bankRef.depositAccount(amount,num);
  }

  public boolean withdraw(int amount, int num)
  {
    return this.bankRef.withdrawAccount(amount,num);
  }

  public boolean transfert(int accountNum, int amount, int bankNum, int dest_accountNum)
  {
    boolean toReturn = this.bankRef.transfert(accountNum, amount, bankNum, dest_accountNum);
    return toReturn;
  }
  public int balance(int num)
  {
    return this.bankRef.balance(num);
  }



}
