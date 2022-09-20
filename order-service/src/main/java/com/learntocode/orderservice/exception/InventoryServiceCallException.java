package com.learntocode.orderservice.exception;

public class InventoryServiceCallException extends Exception{
    public InventoryServiceCallException() {
        super();
    }

    public InventoryServiceCallException(String message) {
        super(message);
    }
}
