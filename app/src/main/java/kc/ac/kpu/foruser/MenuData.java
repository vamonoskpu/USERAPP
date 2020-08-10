package kc.ac.kpu.foruser;

public class MenuData {

    public String usermenu;

    public MenuData(){ }
    public MenuData(String usermenu){
        this.usermenu=usermenu;
    }

    public void setUsermenu(String usermenu) {
        this.usermenu = usermenu;
    }
    public String getUsermenu(){
        return usermenu;
    }
}
