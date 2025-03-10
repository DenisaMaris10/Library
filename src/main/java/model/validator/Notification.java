package model.validator;

import java.util.ArrayList;
import java.util.List;

public class Notification<T> {
    private T result;

    private final List<String> errors; // vor fi pastrate toate potentialele erori din aplicatie

    public Notification(){
        this.errors = new ArrayList<>();
    }

    public void addError(String error){
        this.errors.add(error);
    }

    public boolean hasErrors(){
        return !this.errors.isEmpty();
    }

    public void setResult(T result){
        this.result = result;
    }

    public T getResult(){
        if(hasErrors()){
            throw new ResultFetchException(errors);
        }
        return result;
    }

    public String getFirstError(){
        return this.errors.getFirst();
    }

    public String getFormattedErrors(){
        return String.join("\n", this.errors);
    }
}
