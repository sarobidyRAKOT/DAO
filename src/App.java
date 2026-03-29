// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.stream.Collectors;

// import mg.ITU.DAO.principale.Condition;
// import mg.ITU.DAO.principale.IntervalCondition;

public class App {

    public static void main(String[] args) throws Exception {
        
        // LocalDateTime date = LocalDateTime.of(2026, 3, 27, 13, 57, 10);

        // Condition <LocalDateTime> first = new Condition<>(date, "<=", "r.date_arrivee::DATE");
        // Condition <LocalDateTime> second = new Condition<>(date.plusMinutes(50), ">", "r.date_arrivee::DATE");

        // ArrayList<Object[]> list = new ArrayList<>();
        // list.add(new Object[] {"tonfa", "tay"});
        // list.add(new Object[] {"tonfa1", "tay1"});
        // list.add(new Object[] {"tonfa2", "tay2"});

        // String t = list.stream().map(objs -> objs[0]+" = "+objs[1]).collect(Collectors.joining("\nAND "));
        // System.out.println(t);
        // IntervalCondition <LocalDateTime> iCondition = new IntervalCondition<>(date, "r.date_arrivee::DATE", date.plusHours(1));

        // IntervalCondition <LocalDateTime> iCondition = new IntervalCondition<>(date, "r.date_arrivee::DATE", date.plusHours(1));
        // IntervalCondition <Integer> iCondition = new IntervalCondition<>(1, "r.date_arrivee::DATE", 2);

        // System.out.println(iCondition.firstStrictANDSecondSimple());

        
        // Employer employer = new Employer();
        // employer.setAge(14);
        // employer.setNom("Jean Paul");
        // employer.setDate_naiss(Date.valueOf("2009-03-27"));
        
        // Generic_DAO generic_DAO = null;
        // LocalDate.parse("");
        // Connection con = null;
        // try {
        //     generic_DAO = new Generic_DAO();
        //     con = generic_DAO.getUdb().get_connection();
            
        //     generic_DAO.save(con, employer);

        //     // ArrayList <Object> l = generic_DAO.get_all(con, employer, 0, 0);
            
        //     // ArrayList <Object> l = generic_DAO.get_allWith_CRITERIA(employer, 0, null);
        //     Object[][] interv = new Object[1][2];
        //     String[] nom_attrCibles = new String[1];
        //     nom_attrCibles[0] = "Date_naiss";
        //     interv[0][0] = Date.valueOf("2005-03-17");

        //     // ArrayList <Object> l = generic_DAO.get_allWith_criteriaINTERV(employer, interv, nom_attrCibles, 0, null);
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     System.err.println("\n");

        // } finally {
        //     try {
        //         ArrayList <Object> l = generic_DAO.get_all(con, employer, 0, 0);
                
        //         for (Object object : l) {
        //             System.out.println(object.toString());
        //         }
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //         System.err.println("\n");
        //     }
        // }

    }
}
