package App;


/**
* App/BankOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from App.idl
* Friday, November 4, 2016 3:38:09 PM CET
*/

public interface BankOperations 
{
  boolean create (int num);
  boolean destroy (int num);
  boolean deposit (int amount, int num);
  boolean withdraw (int amount, int num);
  boolean transfert (int myNum, int amount, int numBank, int num);
  int balance (int num);
  boolean recevoirTransaction (App.Transaction t);
  boolean confirmerTransaction (App.Transaction t);
  int get_num ();
} // interface BankOperations
