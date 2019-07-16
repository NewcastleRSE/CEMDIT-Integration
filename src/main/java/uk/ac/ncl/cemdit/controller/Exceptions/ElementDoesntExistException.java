package uk.ac.ncl.cemdit.controller.Exceptions;

public class ElementDoesntExistException extends Exception {

    public ElementDoesntExistException() {
        super();
    }

    public ElementDoesntExistException(String message) {
        super(message);

    }

}