package can.manager.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Article {

    private final IntegerProperty number;
    private final IntegerProperty subNumber;
    private final StringProperty text;

    public Article() {
        this(0, 0, null);
    }

    public Article(int number, int subNumber, String text) {
        this.number = new SimpleIntegerProperty(number);
        this.subNumber = new SimpleIntegerProperty(subNumber);
        this.text = new SimpleStringProperty(text);
    }

    public int getNumber(){
        return number.get();
    }
    
    public int getSubNumber(){
        return subNumber.get();
    }

    public String getText(){
        return text.get();
    }

    public void setNumber(int number){
        this.number.set(number);
    }
    
    public void setSubNumber(int subNumber){
        this.number.set(subNumber);
    }
    
    public void setText(String text){
        this.text.set(text);
    }

    public IntegerProperty number(){
        return number;
    }

    public IntegerProperty subNumber(){
        return subNumber;
    }

    public StringProperty text(){
        return text;
    }
}