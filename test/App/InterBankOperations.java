package App;


/**
* App/InterBankOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from App.idl
* mercredi 2 novembre 2016 11 h 50 CET
*/

public interface InterBankOperations 
{
  void envoyerTransaction (App.Transaction t);
  void confirmerTransaction (App.Transaction t);
} // interface InterBankOperations
