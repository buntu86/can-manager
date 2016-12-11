package can.manager.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SoumArticles {
    private StringProperty article, desc, quantite, um, prixSoum, totalSoum;
    private int lastVar;

    public SoumArticles(String article, String desc, String quantite, String um, String prixSoum, String lastVar){        
        this.article = new SimpleStringProperty(article);
        this.desc = new SimpleStringProperty(desc);
        this.quantite = new SimpleStringProperty(quantite);
        this.um = new SimpleStringProperty(um);
        this.prixSoum = new SimpleStringProperty(prixSoum);
        this.totalSoum = new SimpleStringProperty();
        this.lastVar = new Integer(lastVar);
    }    

    SoumArticles(String article) {
        if(article.length()<6)
        {
            String str = "";
            for(int i=article.length(); i<6; i++)
                str = str + "0";
            article += str;
        }
        
        this.article = new SimpleStringProperty(article);
        this.desc = new SimpleStringProperty("");
        this.quantite = new SimpleStringProperty("");
        this.um = new SimpleStringProperty("");
        this.prixSoum = new SimpleStringProperty("");
        this.totalSoum = new SimpleStringProperty("");
    }

    SoumArticles(int article) {
        this.article = new SimpleStringProperty(Integer.toString(article));
        this.desc = new SimpleStringProperty("");
        this.quantite = new SimpleStringProperty("");
        this.um = new SimpleStringProperty("");
        this.prixSoum = new SimpleStringProperty("");
        this.totalSoum = new SimpleStringProperty("");
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
    
    
    public String getQuantite() {
        return quantite.get();
    }
    public void setQuantite(StringProperty quantite) {
        this.quantite = quantite;
    }
    public StringProperty quantiteProperty() {
        return quantite;
    }    
    void addQuantite(String quantite) {
        if(!quantite.isEmpty())
        {
            float quantiteOld=0, quantiteNew = Float.parseFloat(quantite) / 1000;
            if(!this.quantite.get().isEmpty())
                quantiteOld=Float.parseFloat(this.quantite.get());

            this.quantite = new SimpleStringProperty(String.format("%.2f", quantiteNew + quantiteOld));
            calculTotalSoum();
            
            if(this.um.get().isEmpty())
                this.um.set("up");
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
        if(!um.equals(""))
            this.um = new SimpleStringProperty(um.trim());
    }    
    
    
    public String getPrixSoum() {
        return prixSoum.get();
    }
    public void setPrixSoum(StringProperty prixSoum) {
        this.prixSoum = prixSoum;
    }
    public StringProperty prixSoumProperty() {
        return prixSoum;
    }
    void addPrixSoum(String prixSoum) {
        if(!prixSoum.isEmpty())
        {
            float prixOld=0, prixNew = Float.parseFloat(prixSoum) / 100;
            if(!this.prixSoum.get().isEmpty())
                prixOld=Float.parseFloat(this.prixSoum.get());
           
            this.prixSoum = new SimpleStringProperty(String.format("%.2f", prixNew + prixOld));            
            calculTotalSoum();
        }
    }
    
    
    public String getTotalSoum() {
        return totalSoum.get();
    }
    public void setTotalSoum(StringProperty totalSoum) {
        this.totalSoum = totalSoum;
    }
    public StringProperty totalSoumProperty() {
        return totalSoum;
    } 
    
    public int getLastVar() {
        return lastVar;
    }
    public void addLastVar(String lastVar)
    {
        this.lastVar = new Integer(lastVar);
    }

    private void calculTotalSoum() {
        if(!this.quantite.get().equals("") && !this.prixSoum.get().equals(""))
            this.totalSoum.set(String.format("%.2f", Float.parseFloat(this.quantite.get()) * Float.parseFloat(this.prixSoum.get())));
    }
}
