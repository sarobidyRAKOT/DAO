package mg.ITU.DAO.principale;

public class Condition <T> extends ConditionMere <T> {
    
    private T value;
    private String action;
    private String key;


    public Condition (T value, String action, String key) {
        this.key = key;
        this.action = action;
        this.value = value;
    }

    @Override
    public String toString() {
        return super.getFormatInQuery(value)+" "+this.action+" "+this.key;
    }



    // public String getAction() {
    //     return action;
    // }
    // public String getKey() {
    //     return key;
    // }
    // public T getValue() {
    //     return value;
    // }
}
