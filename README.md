# virtualstore
E-commerce store

# Implementare

    Clasa Store:

    - utilizeaza un pattern de tip Singleton
    - reprezinta clasa de baza ce modeleaza magazinul online
    - inafara metodelor date in diagrama UML am mai adaugat si:
        - metoda getProductDepartment(productID) - returneaza departamentul de care apartine un produs
        - metodele assistCustomerPurchase si assistCutomerWish - pentru o mai buna abstractizare a procesului de cumparare, primesc un Customer si ID-ul produsului dorit si apoi intermediaza toata interactiunea cu magazinul astfel incat in final produsul ajunge in cosul de cumparaturi al clientului, nefiind nevoie sa se apeleze de fiecare data manual mecanismele de notificare, etc
        - metooda getDepartmentById - returneaza Department-ul respectiv, extras din departamentele magazinului
        - metoda getCustomerByName - returneaza un obiect de tip Customer, cautat in clientii activi ai magazinului
        - un camp de tip DecimalFormat cu ajutorul caruia mai tarziu se vor putea formata ca string preturile de tip double pentru a respecta un format cu 2 zecimale

    Clasa abstracta Department:

    - reprezinta sablonul pe care sunt construite toate departamentele din magazin
    - implementeaza sabloanele Notification (pentru mecanismul de wishlist) si Visitor (pentru a putea aplica reduceri unui ShoppingCart bazat pe tipul departamentului)
    - inafara metodelor date in diagrama UML am mai adaugat si:

        - metodele handleCustomerPurchase si handleCustomerWish - vor fi apelate de catre metodele assist* din clasa Store, si executa efectiv operatiile de adaugare la ShoppingCart/Wishlist
        (Store va determina departamentul care trebuie sa se ocupe de cererea clientului (assist), iar mai departe va fi apelata metoda handle* a departamentului respectiv)

    Clasa Item:

    - reprezinta efectiv un produs din magazin (dintr-un departament)
    - am mai adaugat un camp de tipul Department, astfel se poate afla la orice moment de timp de ce departament apartine un anumit produs, prin apelarea metodei getParentDepartment()
    - am adaugat de asemenea si un constructor de copiere, deoarece in fiecare ShoppingCart/Wishlist se adauga de fapt o copie a produsului din inventarul unui departament, din motive evidente (aplicarea de reduceri individuale unui singur client, etc.)

    Clasa ItemList:

    - reprezinta o lista liniara dublu inlantuita ce implementeaza interfata ListIterator
    - constructorul unui ItemList va primi un Comparator care decide regulile de sortare pentru lista
    (ShoppingCart are ordonare dupa pret, WishList dupa nume, etc)

    Clasa ShoppingCart:

    - am adaugat o metoda pentru a calcula bugetul ramas disponibil pentu un anumit cos

    Clasa WishList:

    - am implementat un pattern de tip Strategy, pentru a sugera un produs clientului bazat pe un algoritm preferat de acesta

 Interfata Grafica:

    Clasa WindowManager:

        - punctul de pornire al aplicatiei este reprezentat de clasa WindowManager
        - aceasta reprezinta un fel de mediator intre celelalte ferestre, primind cereri pentru schimbarea JPanelului curent
        - odata cu rularea metodei Main se va deschide o fereastra pentru initializarea magazinului utilizand fiserele de initializare
        - dupa initializare se va trece la fereastra Login

    Fereastra/Clasa Login:

        - contine un text field in care se vor introduce fie numele clientului care doreste sa se conecteze (se va deschide fereastra Client) sau "admin" pentru afisarea panoului de administrare

    Fereastra/Clasa Admin:

        - contine un tab cu statistici despre magazin
        - alte 2 tab-uri ce se ocupa cu gestionarea produselor/clientilor din magazin

    Fereastra/Clasa Client:

        - are de asemenea un layout tabular: unul dintre acestea este pagina cu toate produsele disponibile, iar in celelalte 2 se pot gestiona produsele din shoppingcart/wishlist

    Clasele ItemTableModel/PriceCellRenderer:

        - in implementare am utilizat un model de tabel custom in asa fel incat sa fie posibila sortarea dupa fiecare coloana atat ascendent cat si descendent
        - price cell renderer se asigura ca in acest model celulele ce reprezinta preturi vor fi afisate cu 2 zecimale, formatate conform specificatiilor DecimalFormat-ului din clasa Store
