---

# Projektas "Biblioteka"

## Turinys
1.  [Paskirtis](#1-paskirtis)
2.  [Technologijos](#2-technologijos)
3.  [Funkcionalumas](#4-funkcionalumas)
4.  [Pagrindinės klasės ir struktūra](#5-pagrindinės-klasės-ir-struktūra)
5.  [Panaudoti projektavimo šablonai](#7-panaudoti-projektavimo-šablonai)
6.  [Plėtimo galimybės](#8-plėtimo-galimybės)
7. [Klasių diagramos paveikslėlis](#11-klasių-diagramos-paveikslėlis)

---

## 1. Paskirtis

Projektas "Biblioteka" yra programinė įranga, skirta mažos bibliotekos leidinių (knygų, elektroninių knygų, žurnalų) ir vartotojų administravimui. Sistema leidžia kataloguoti leidinius, valdyti jų skolinimo ir grąžinimo procesus.

---

## 2. Technologijos

-   **Programavimo kalba:** Java (versija 8 ar naujesnė, dėl `java.time` ir JavaFX naudojimo)
-   **Grafinė vartotojo sąsaja (GUI):** JavaFX
-   **Duomenų saugojimas:** Java serializacija (objektai saugomi dvejetainiu formatu `.dat` faile)
-   **Kolekcijos:** Java Collections Framework (`ArrayList`, `List`), JavaFX Observable Collections (`ObservableList`)
-   **Dokumentacija:** Javadoc

---

## 3. Funkcionalumas

**Vartotojų valdymas:**
*   Prisijungimas ir registracija.
*   Dvi rolės: Administratorius ir Skaitytojas.

**Administratoriaus funkcijos:**
*   Pridėti naujus leidinius (šiuo metu GUI palaiko tik `Knyga` pridėjimą).
*   Pašalinti knygas iš bibliotekos.
*   Peržiūrėti visus bibliotekoje esančius (laisvus) leidinius.
*   Peržiūrėti visus pasiskolintus leidinius.
*   Grąžinti bet kurį pasiskolintą leidinį.

**Skaitytojo funkcijos:**
*   Peržiūrėti visus bibliotekoje esančius (laisvus) leidinius.
*   Pasiskolinti laisvas fizines knygas
*   Peržiūrėti savo pasiskolintų leidinių sąrašą.
*   Grąžinti savo pasiskolintus leidinius.
*   Peržiūrėti visų pasiskolintų leidinių sąrašą (tik peržiūros režimu).

**Leidinių tipai:**
*   **Knyga (`Knyga`):** Fizinė knyga su ISBN, autoriumi, žanru ir kt. Gali būti skolinama. Yra `Cloneable` ir `Serializable`.
*   **ElektronineKnyga (`ElektronineKnyga`):** Skaitmeninė knyga, paveldinti iš `Knyga`. Turi failo formatą, dydį MB. Negali būti skolinama.
*   **Zurnalas (`Zurnalas`):** Fizinis žurnalas su ISSN, numeriu, tema ir kt.

**Duomenų validacija:**
*   ISBN ir ISSN numerių tikrinimas.
*   Bazinė kitų laukų validacija (pvz., metai, puslapių skaičius).

---

## 4. Pagrindinės klasės ir struktūra

**Paketai:**
*   **`biblioteka.core`**: Pagrindinės duomenų modelio klasės, sąsajos ir išimtys.
    *   Klasės: `Leidinys` (abstrakti), `Knyga`, `ElektronineKnyga`, `Zurnalas`, `User`.
    *   Sąsajos: `Borrowable`, `Postponable`, `Digital`, `Downloadable`.
    *   Išimtys: `BookException`, `InvalidISBNException`.
*   **`biblioteka.factory`**: Klasės, įgyvendinančios "Factory Method" šabloną leidinių kūrimui.
    *   `LeidinysFactory` (abstrakti), `KnygaCreator`, `ZurnalasCreator`.
*   **`biblioteka.gui`**: Klasės, susijusios su grafine vartotojo sąsaja.
    *   Pagrindinė aplikacijos klasė: `BookApplication`.
    *   Duomenų saugykla ir prieigos sluoksnis: `LibraryRepository`.
    *   Duomenų išsaugojimo/įkėlimo logika: `PersistenceManager`.
*   **`biblioteka.gui.controller`**: Valdiklių (Controller) klasės, skirtos FXML failams.
    *   `LoginController`, `MainViewController`, `BookController`, `AddBooksController`, `BorrowedController`, `MyBorrowedBooksController`.

---

## 5. Panaudoti projektavimo šablonai

*   **Factory Method:** Įgyvendinta per `biblioteka.factory` paketą (`LeidinysFactory`, `KnygaCreator`, `ZurnalasCreator`) leidinių objektų kūrimui.
*   **MVC (Model-View-Controller) / MVVM (Model-View-ViewModel):** Bendra GUI architektūra.
    *   **Modelis:** `biblioteka.core` klasės ir `LibraryRepository`.
    *   **Vaizdas:** FXML failai.
    *   **Valdiklis/ViewModel:** `biblioteka.gui.controller` klasės.

---

## 6. Plėtimo galimybės

*   Pilnai integruoti `Zurnalas` ir `ElektronineKnyga` pridėjimą ir valdymą per GUI.
*   Įdiegti paieškos ir filtravimo funkcionalumą leidiniams.
*   Saugesnis slaptažodžių saugojimas (pvz., naudojant bcrypt ar Argon2).
*   Pridėti galimybę redaguoti esamų leidinių informaciją per GUI.
*   Ataskaitų generavimas (pvz., populiariausios knygos, vėluojantys grąžinti).


---


## 7. Klasių diagrama

![Klasių diagrama](https://i.imgur.com/JykTNey.png)

---