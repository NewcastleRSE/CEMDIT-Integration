package uk.ac.ncl.cemdit.controller.Exceptions;

public class InvalidDocumentFormatException extends Exception {

    public InvalidDocumentFormatException(String errorMessage) {
        super(errorMessage);
    }
}
