package mg.ITU.DAO.enums;

public enum IntervalType {
    ALLStrict, // <= AND <=
    ALLSimple, // < AND <
    firstSimpleANDSecondStrict, // < AND <=
    firstStrictANDSecondSimple // <= AND <
}
