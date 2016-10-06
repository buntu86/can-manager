package can.manager.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Article {

    private final IntegerProperty ID;
    private final IntegerProperty position;
    private final IntegerProperty subPosition;
    private final IntegerProperty variable;
    private final IntegerProperty line;
    private final StringProperty alt;
    private final StringProperty unit;
    private final IntegerProperty publication;
    private final IntegerProperty begin;
    private final StringProperty text;

    public Article() {
        this(0, 0, 0, 0, 0, null, null, 0, 0, null);
    }
    
    public Article(int ID, int position, int subPosition, int variable, int line, String alt, String unit, int publication, int begin, String text){
        this.ID = new SimpleIntegerProperty(ID);
        this.position = new SimpleIntegerProperty(position);
        this.subPosition = new SimpleIntegerProperty(subPosition);
        this.variable = new SimpleIntegerProperty(variable);
        this.line = new SimpleIntegerProperty(line);
        this.alt = new SimpleStringProperty(alt);
        this.unit = new SimpleStringProperty(unit);
        this.publication = new SimpleIntegerProperty(publication);
        this.begin = new SimpleIntegerProperty(begin);
        this.text = new SimpleStringProperty(text);
    }

    //GET
    public int getID(){
        return ID.get();
    }
    public int getPosition(){
        return position.get();
    }
    public int getSubPosition(){
        return subPosition.get();
    }
    public int getVariable(){
        return variable.get();
    }
    public int getLine() {
        return line.get();
    }
    public String getAlt() {
        return alt.get();
    }
    public String getUnit() {
        return unit.get();
    }
    public int getPublication() {
        return publication.get();
    }
    public int getBegin() {
        return begin.get();
    }
    public String getText() {
        return text.get();
    }
    
    //SET
    public void setID(int ID){
        this.ID.set(ID);
    }
    public void setPosition(int position){
        this.position.set(position);
    }
    public void setSubPosition(int subPosition){
        this.subPosition.set(subPosition);
    }
    public void setVariable(int variable){
        this.variable.set(variable);
    }
    public void setLine(int line){
        this.line.set(line);
    }
    public void setAlt(String alt){
        this.alt.set(alt);
    }
    public void setUnit(String unit){
        this.unit.set(unit);
    }
    public void setPublication(int publication){
        this.publication.set(publication);
    }
    public void setBegin(int begin){
        this.begin.set(begin);
    }
    public void setText(String text){
        this.text.set(text);
    }
    
    //PROPERTY
    public IntegerProperty IDProperty(){
        return ID;
    }    
    public IntegerProperty positionProperty(){
        return position;
    }
    public IntegerProperty subPositionProperty(){
        return subPosition;
    }    
    public IntegerProperty variableProperty(){
        return variable;
    } 
    public IntegerProperty lineProperty(){
        return line;
    }
    public StringProperty altProperty(){
        return alt;
    }       
    public StringProperty unitProperty(){
        return unit;
    }        
    public IntegerProperty publicationProperty(){
        return publication;
    }        
    public IntegerProperty beginProperty(){
        return begin;
    }        
    public StringProperty textProperty(){
        return text;
    }        
}