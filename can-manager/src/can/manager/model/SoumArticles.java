package can.manager.model;

import javafx.beans.binding.Bindings;
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

    public SoumArticles(String article, String desc, String quantite, String um, String prixSoum){
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
        this.desc.concat(this.desc + "\n" + desc);
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

    
    public String getUm() {
        return um.get();
    }
    public void setUm(StringProperty um) {
        this.um = um;
    }
    public StringProperty umProperty() {
        return um;
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
    
    
    public float getTotalSoum() {
        return totalSoum.get();
    }
    public void setTotalSoum(FloatProperty totalSoum) {
        this.totalSoum = totalSoum;
    }
    public FloatProperty totalSoumProperty() {
        return totalSoum;
    }    


}
