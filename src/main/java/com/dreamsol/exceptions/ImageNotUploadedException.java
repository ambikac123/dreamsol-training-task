package com.dreamsol.exceptions;

public class ImageNotUploadedException extends RuntimeException
{
    String errorMessage;
    public ImageNotUploadedException(String errorMessage)
    {
        super("Image not uploaded!, Reason: "+errorMessage);
        this.errorMessage=errorMessage;
    }
}
