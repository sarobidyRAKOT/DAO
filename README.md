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