package org.cardanofoundation.rosetta.consumer.exception;

public class HashScriptException extends RuntimeException{
  public HashScriptException(Exception e){super(e);}

  public HashScriptException(String msg){super(msg);}
}
