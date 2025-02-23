package mg.ITU.DAO.generique;

public class Pagination {
    
    public int OFFSET;
    public int LIMIT;

    public Pagination (int offset, int limit) throws Exception {
        setLIMIT(limit);
        setOFFSET(offset);
    }


    private void setLIMIT(int limit) throws Exception {
        if (limit < 0) throw new Exception("LA LIMITE DU PAGINATION DOIS ETRE POSITIVE");
        else this.LIMIT = limit;
    }
    private void setOFFSET(int offset) throws Exception {
        // System.out.println("OFFSET");
        if (offset < 0) throw new Exception("LE OFFSET DOIS TOUJOURS ETRE SUPP A 0");
        else this.OFFSET = offset;
    }
}
