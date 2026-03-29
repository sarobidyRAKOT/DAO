# DOCUMENTATION POUR L'UTILISATION DU DAO

## 1. C'est quoi DAO

- Un peu comme un framework
- Manipulation des modeles JAVA entre la BDD POSTGRES

## 2. UTILISATION

```java
    public int save (Connection conn, Object origine) throws Exception;
    public int save (Object origine) throws Exception;
    public ArrayList<Object> get_all (Object origine, String apresWhere, Pagination paging) throws Exception;
    public ArrayList<Object> get_all(Connection conn, Object origine, String apresWhere, Pagination paging) throws Exception;
    public ArrayList<Object> get_allWith_CRITERIA (Object origine, String apresWhere, Pagination paging) throws Exception;
    public ArrayList<Object> get_allWith_CRITERIA (Connection conn, Object origine, String apresWhere, Pagination paging) throws Exception;  
    public ArrayList<Object> get_allWith_criteriaINTERV(Object origine, String apresWhere, Pagination paging, Object[][] intervals, String[] col_names) throws Exception;
    public ArrayList<Object> get_allWith_criteriaINTERV(Connection conn, Object origine, String apresWhere, Pagination paging, Object[][] intervals, String[] col_names) throws Exception;

    public int updateObjectById (Connection conn, Object origine) throws Exception;
    public String get (Connection conn, Map<String, String> colAleas, String requete) throws Exception;
```

## DAO-1: Modofication du fonction get_allWith_criteriaINTERV

```java
    public ArrayList<Object> get_allWith_criteriaINTERV(Connection con, Object origine, IntervalCondition <?> [] iConditions, String apresWhere, Pagination paging) throws Exception;
```

- On utilise la variable `IntervalCondition <?> []` au lieu de simple tableau comme precedent
- UTLISATION:

    ```java
        ReservationDTO reservationDTO = new ReservationDTO();
        // SETTER pour le autre condition
        reservationDTO.setTypeAssignementId("0");
        String apresWhere = "ORDER BY nombre_passage DESC"; // REQUETE apres where

        // CONDITION INTERVAL
        /**
         * on ne definit la lr type du tableau
         * sauf pour les IntervalCondition a l'interieur
         */
        IntervalCondition <?> [] iConditions = new IntervalCondition <?>[] {
            new IntervalCondition<LocalDateTime>(dateMin, "date_heure_arrive", dateMax, IntervalType.ALLStrict)
        };
        ArrayList<Object> list = this.gen.get_allWith_criteriaINTERV(con, reservationDTO, iConditions, apresWhere, null);

    ```
