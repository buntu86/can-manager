package can.manager.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SoumArticles {
    private StringProperty article;
    private StringProperty desc;
    private FloatProperty quantite;
    private StringProperty um;
    private FloatProperty prixSoum;
    private FloatProperty totalSoum;
    private int lastVar;

    public SoumArticles(String article, String desc, String quantite, String um, String prixSoum, String lastVar){
        if(quantite.isEmpty())
            quantite="0";
        if(prixSoum.isEmpty())
            prixSoum="0";
        
        this.article = new SimpleStringProperty(article);
        this.desc = new SimpleStringProperty(desc);
        this.quantite = new SimpleFloatProperty(Float.valueOf(quantite));
        this.um = new SimpleStringProperty(um);
        this.prixSoum = new SimpleFloatProperty(Float.valueOf(prixSoum));
        this.totalSoum = new SimpleFloatProperty(Float.valueOf(prixSoum) * Float.valueOf(quantite));
        this.lastVar = new Integer(lastVar);
    }    

    SoumArticles(String article) {
        if(article.length()<6)
        {
            String str = "";
            for(int i=article.length(); i<6; i++)
                str = str + "0";
            article += str;
            System.out.println(article);
        }
        
        this.article = new SimpleStringProperty(article);
        this.desc = new SimpleStringProperty("");
        this.quantite = new SimpleFloatProperty(0);
        this.um = new SimpleStringProperty("");
        this.prixSoum = new SimpleFloatProperty(0);
        this.totalSoum = new SimpleFloatProperty(0);
    }

    SoumArticles(int article) {
        this.article = new SimpleStringProperty(Integer.toString(article));
        this.desc = new SimpleStringProperty("");
        this.quantite = new SimpleFloatProperty(0);
        this.um = new SimpleStringProperty("");
        this.prixSoum = new SimpleFloatProperty(0);
        this.totalSoum = new SimpleFloatProperty(0);
    }    
    
    
    public String getArticle() {
        return article.get();
    }
    public void setArticle(StringProperty article) {
        this.article = article;
    }
    public StringProperty articleProperty() {
        return article;
    }    

    
    public String getDesc() {
        return desc.get();
    }
    public void setDesc(StringProperty desc) {
        this.desc = desc;
    }
    public StringProperty descProperty() {
        return desc;
    }
    public void addDesc(String desc) {
        this.desc = new SimpleStringProperty(this.desc.get().concat(desc));
    }    
    
    
    public float getQuantite() {
        return quantite.get();
    }
    public void setQuantite(FloatProperty quantite) {
        this.quantite = quantite;
    }
    public FloatProperty quantiteProperty() {
        return quantite;
    }    
    void addQuantite(String quantite) {
        if(!quantite.isEmpty())
        {
            this.quantite = new SimpleFloatProperty((this.quantite.get() + Float.valueOf(quantite))/1000);
            setTotalSoum(new SimpleFloatProperty(this.getPrixSoum()*this.getQuantite()));
        }
    }    

    
    public String getUm() {
        return um.get();
    }
    public void setUm(StringProperty um) {
        this.um = um;
    }
    public StringProperty umProperty() {
        return um;
    }
    void addUm(String um) {
        this.um = new SimpleStringProperty(this.um.get().concat("\n" + um.trim()));
    }    
    
    
    public float getPrixSoum() {
        return prixSoum.get();
    }
    public void setPrixSoum(FloatProperty prixSoum) {
        this.prixSoum = prixSoum;
    }
    public FloatProperty prixSoumProperty() {
        return prixSoum;
    }
    void addPrixSoum(String prixSoum) {
        if(!prixSoum.isEmpty())
        {
            this.prixSoum = new SimpleFloatProperty((this.prixSoum.get() + Float.valueOf(prixSoum))/1000);
            setTotalSoum(new SimpleFloatProperty(this.getPrixSoum()*this.getQuantite()));
        }
    }
    
    
    public float getTotalSoum() {
        return totalSoum.get();
    }
    public void setTotalSoum(FloatProperty totalSoum) {
        this.totalSoum = totalSoum;
    }
    public FloatProperty totalSoumProperty() {
        return totalSoum;
    } 
    
    public int getLastVar() {
        return lastVar;
    }
    public void addLastVar(String lastVar)
    {
        this.lastVar = new Integer(lastVar);
    }
}
