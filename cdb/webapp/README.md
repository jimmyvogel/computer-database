Pour lancer les tests selenium :  il vous faudra télécharger le driver de firefox geckdriver
https://github.com/mozilla/geckodriver/releases

Ensuite il faudra créer le fichier suivant : config.properties dans :

/cdb/webapp/src/test/resources/

Ensuite rajouter dans ce fichier la ligne suivante : 

driverPath = $votre_path_de_votre_geckodriver
