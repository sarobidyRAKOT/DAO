package mg.ITU.DAO.principale;

import mg.ITU.DAO.enums.IntervalType;

public class IntervalCondition <T> extends ConditionMere <T> {
    

    private T firstValue;
    private String key;
    private T secondValue;
    private IntervalType intervalType;

    public IntervalCondition (T first, String key, T second, IntervalType type) throws Exception {
        if (first == null || second == null) {
            throw new Exception("LES DEUX VALEURS DE L'INTERVALLE SONT NULL, C'est n'est pas un initervalle utiiser la condition SIMPLE");
        }
        this.intervalType = type;
        this.firstValue = first;
        this.key = key;
        this.secondValue = second;
    }


    public String getQuery () {
        switch (intervalType) {
            case ALLStrict:
                return super.getFormatInQuery(firstValue)+" <= "+key+" AND "+key+" <= "+super.getFormatInQuery(secondValue);
            case ALLSimple:
                return super.getFormatInQuery(firstValue)+" < "+key+" AND "+key+" < "+super.getFormatInQuery(secondValue);
            case firstStrictANDSecondSimple:
                return super.getFormatInQuery(firstValue)+" <= "+key+" AND "+key+" < "+super.getFormatInQuery(secondValue);
            case firstSimpleANDSecondStrict:
                return super.getFormatInQuery(firstValue)+" < "+key+" AND "+key+" <= "+super.getFormatInQuery(secondValue);
            default: // ALLStrict
                return super.getFormatInQuery(firstValue)+" <= "+key+" AND "+key+" <= "+super.getFormatInQuery(secondValue);
        }
    }



}
