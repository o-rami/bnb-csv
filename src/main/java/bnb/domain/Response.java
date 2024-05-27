package bnb.domain;


import java.util.ArrayList;
import java.util.List;

public class Response {

    private final List<String> messages = new ArrayList<>();

    public void addErrorMessage(String message) {
        messages.add(message);
    }

    public List<String> getErrorMessages() {
        return messages;
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }

}
