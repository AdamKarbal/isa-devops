# Guidelines générales

Ces règles visent à garantir un code lisible, maintenable et une collaboration efficace.

---

## Gestion des issues

### Vérification préalable
Avant de créer une issue, **vérifiez les issues existantes** afin d’éviter les doublons.

---

### User Stories

**Création**
- Aller sur **New Issue**
- Choisir **User Story**
- Utiliser le template fourni
- Donner un titre orienté besoin utilisateur

**Convention de titre**
```
[US] En tant que <rôle>, je veux <objectif>
```

---

### Tasks

**Convention de nommage**
```
T{NUMERO_US}.{NUMERO_TASK} {TITRE_DE_LA_TASK}
```

---

## Branches & Workflow Git

### feature/nom-de-la-feature
- Une fonctionnalité par branche
- Espacement du nom par des tirets

### fix/feature-a-fix
- Une correction de fonctionnalité par branche

### main
- Branche stable d’intégration
- Pull Request obligatoire (interdit de push sur main)

### production
- Versions stables uniquement

---

## Convention de nommage des commits

Format :
```
<type>(scope): message
```
Types autorisés :

feat : nouvelle fonctionnalité
fix : correction de bug
docs : documentation
style : formatage, indentation (sans impact logique)
refactor : pour refactoriser du code déjà présent
test : ajout/modification de tests
chore : maintenance, configuration, tooling

Pour le nommage du commit, réfléchir à :
- Ce qui a été changé
- Quels sont ses effets sur le système

---

## Pull Requests

- Nom : `type(scope): description`
- Lier l’issue concernée
- Review obligatoire
- Une PR = une fonctionnalité

---

## Règles de merge
- Pour pouvoir merge de feature vers main :
    - Tester avant de push
    - Review par un membre du groupe effectuée

- Pour pouvoir merge de fix vers main :
    - Tous les tests liés à la feature passent
    - Review par un membre du groupe effectuée

- Pour pouvoir merge de main vers production :
    - Tous les tests liés à la CI passent
    - La milestone est complétée


---
