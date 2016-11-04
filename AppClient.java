import App.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import org.omg.CosNaming.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AppClient
{
  public static void main(String args[])
  {
    int account_num = Integer.parseInt(args[1]);
    try
    {
      //Client of Bank
      org.omg.CORBA.Object objRef;
      String test[] = new String[2];
      test[0] = "-ORBInitRef";
      test[1] = "NameService=corbaloc::127.0.0.1:2810/NameService";
      ORB orb = ORB.init(test, null);
      objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
      objRef = ncRef.resolve_str("app.bank"+args[0]);
      Bank bank1 = BankHelper.narrow(objRef);
      boolean response = bank1.create(Integer.parseInt(args[1]));
      //System.out.println(response);
      //response = bank1.deposit(1000, 1);
      //System.out.println(response);
      //int balance = bank1.balance(1);
      //System.out.println(balance);
      int input = -1;
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      response = false;
      int money = 0;
      int dest_bank = 0;
      int dest_account = 0;
      while (input != 0)
      {
        System.out.println("Operation:");
        System.out.println("1 - Deposit money");
        System.out.println("2 - Withdraw money");
        System.out.println("3 - Transfer money");
        System.out.println("0 - Quit");
        br = new BufferedReader(new InputStreamReader(System.in));
        input = Integer.parseInt(br.readLine());
        switch (input)
        {
          case 1:
            System.out.println("Deposit - How much?: ");
            br = new BufferedReader(new InputStreamReader(System.in));
            money = Integer.parseInt(br.readLine());
            response = bank1.deposit(account_num, money);
            System.out.println(response);
            break;
          case 2:
            System.out.println("Withdraw - How much?: ");
            br = new BufferedReader(new InputStreamReader(System.in));
            money = Integer.parseInt(br.readLine());
            response = bank1.withdraw(account_num, money);
            System.out.println(response);
            break;
          case 3:
            System.out.println("Transfer - Bank Number: ");
            br = new BufferedReader(new InputStreamReader(System.in));
            dest_bank = Integer.parseInt(br.readLine());
            System.out.println("Transfer - Account Number: ");
            br = new BufferedReader(new InputStreamReader(System.in));
            dest_account = Integer.parseInt(br.readLine());
            System.out.println("Transfer - How much?: ");
            br = new BufferedReader(new InputStreamReader(System.in));
            money = Integer.parseInt(br.readLine());
            response = bank1.transfert(account_num, money, dest_bank, dest_account);
            System.out.println(response);
            break;
          default:
            input = 0;
            break;
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("BankClient Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
