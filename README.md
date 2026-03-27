# DAO (Générique DAO)
    > CRUD pour Objet
    > On utilise toujours des Objets pour le type de variable
    > Avec une foctionnalité recherecher et rechercher avec intervalle

    > UTILISATION DU DAO
        1. La classe doit etre annoté `@Table`
        2. Les attributs annoté `@Colonne`
        3. Chaque attribut annoté avec `@Colonne` doit toujours avoir un setter (de type `String` OBLIGATOIRE) et aussi un getter
        4. La classe dois avoir un constructeur par defaut
        5. La valeur de l'annotaion @Table et @Colonne ne dois pas etre vide


    UTILISATION DE L'ANNOTATION `@PrimaryKey`
        . na tsy primary key aza ilay attribut de mety fona (problem a regle)

```bash
    app.base.url=jdbc:postgresql://localhost:5432/ma_base
    app.base.utilisateur=postgres
    app.base.mot.de.passe=1234
    app.classe.for.name=org.postgresql.Driver
```

```java
    public String ExecuteRequete (Connection conn, Map<String, String> parametres, String requete) throws Exception;
```

1. REQUETE UPDATE

    - La requete dois suivre obligatoirement la forme si dessous.
    - Pour le parametre de la fonction `parametres` on met l'`aleas` pour clé et la `colonne` reciproque pour ca valeur.

    Ex:
    ```sql
        UPDATE table_name
        SET 
            ????
        WHERE condition;
    ```

    ```java
        String requete  = "UPDATE table_name\n" +
        "SET \n" +
        "    ????\n" + // ???
        "WHERE id = 1";
        
        public String ExecuteRequete (Connection conn, Map<String, String> parametres, String requete) throws Exception;
    ```

- La table ne dois pas avoir plusieur primary key (un seul)