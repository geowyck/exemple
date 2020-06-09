# exemple
Exemple de tests avec Selenium

Pour configurer un fichier de properties particulier, il faut valoriser la variable d'environement 

**selenium.properties.filename**

Par défaut, il est valoriser à *pf3.selenium.properties*, fichier à mettre sous test/ressources pour un projet avec un archetype Maven.


### Rapport ZAP

Ils sont sous **target**. C'est le rapport issu de l'API (n'intégrant pas de filtre par projet)

### Exception lors d'une session Selenium débutée

Si le driver est bien instancié, en cas d'exception lié aux actions du test, une capture d'écran est prise associé à la page source de
la page et se trouve sous **target**


