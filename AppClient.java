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
    System.out.println("Creating Account "+args[1]+" in Bank "+args[0]);
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
      objRef = ncRef.resolve_str("app.bankCustomer"+args[0]);
      BankCustomer bank1 = BankCustomerHelper.narrow(objRef);
      boolean response = bank1.create(account_num);
      int input = 9;
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      response = false;
      int balance = 0;
      int money = 0;
      int dest_bank = 0;
      int dest_account = 0;
      while (input != 0)
      {

        System.out.println("Operation:");
        System.out.println("1 - Deposit money");
        System.out.println("2 - Withdraw money");
        System.out.println("3 - Transfer money");
        System.out.println("4 - Display sold");
        System.out.println("0 - Quit");
        //br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (isParsable(line)){
          input = Integer.parseInt(line);

          switch (input)
          {
            case 1:
              System.out.println("Deposit - How much?: ");
              //br = new BufferedReader(new InputStreamReader(System.in));
              line = br.readLine();
              while (!isParsable(line)){
                System.out.println("Error ");
                System.out.println("Deposit - How much?: ");
                line = br.readLine();
              }
                money = Integer.parseInt(line);
                response = bank1.deposit(money, account_num);
                if(response){
                  System.out.println("Operation Successful");
                }else{
                  System.out.println("Operation Unsuccessful");
                }


              break;
            case 2:
              System.out.println("Withdraw - How much?: ");
              //br = new BufferedReader(new InputStreamReader(System.in));
              line = br.readLine();
              while (!isParsable(line)){
                System.out.println("Error ");
                System.out.println("Withdraw - How much?: ");
                line = br.readLine();
              }
              money = Integer.parseInt(line);
              response = bank1.withdraw(money, account_num);
              if(response){
                System.out.println("Operation Successful");
              }else{
                System.out.println("Operation Unsuccessful");
              }
              break;
            case 3:
              System.out.println("Transfer - Bank Number: ");
              //br = new BufferedReader(new InputStreamReader(System.in));
              line = br.readLine();
              while (!isParsable(line)){
                System.out.println("Error ");
                System.out.println("Transfer - Bank Number: ");
                line = br.readLine();
              }
              dest_bank = Integer.parseInt(line);

              System.out.println("Transfer - Account Number: ");
              br = new BufferedReader(new InputStreamReader(System.in));
              line = br.readLine();
              while (!isParsable(line)){
                System.out.println("Error ");
                System.out.println("Transfer - Account Number: ");
                line = br.readLine();
              }
              dest_account = Integer.parseInt(line);

              System.out.println("Transfer - How much?: ");
              //br = new BufferedReader(new InputStreamReader(System.in));
              line = br.readLine();
              while (!isParsable(line)){
                System.out.println("Error ");
                System.out.println("Transfer - How much?: ");
                line = br.readLine();
              }
              money = Integer.parseInt(line);
              response = bank1.transfert(account_num, money, dest_bank, dest_account);
              if(response){
                System.out.println("Operation Successful");
              }else{
                System.out.println("Operation Unsuccessful");
              }
              break;
            case 4:

              //br = new BufferedReader(new InputStreamReader(System.in));
              balance = bank1.balance(account_num);
              if(balance == -1){
                System.out.println("Operation Unsuccessful");
              }else{
                System.out.println("Sold - Here is how much money you have: ");
                System.out.println(balance);
              }

              break;
            case 0:
              break;

            default:
              input = 0;
              break;
          }
          // if (!bank1.isOpened()){
          //   System.out.println("Bank is closed. Try later !");
          //   imput = 0;
          // }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("BankClient Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static boolean isParsable(String input){
    boolean parsable = true;
    try{
        Integer.parseInt(input);
    }catch(NumberFormatException e){
        parsable = false;
    }
    return parsable;
}
}
