# Project Assignment POO  - J. POO Morgan - Phase Two

![](https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExbnFyYzNlMTRwdGIxYnkxajNodDJkNm5weHhlc3kxa3FwdzIwc2VtZSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/gyFAgjhkWu4zJXcAQH/giphy.gif)

## Descriere proiect

Fata de etapa anterioara, aceasta etapa introduce functionalitati avansate precum planuri de servicii personalizate (standard, student, silver, gold), optiuni de cashback adaptate tipului de tranzactie si planului utilizatorului si retrageri conditionate din conturile de economii.

## Design patterns

Am folosit urmatoarele design pattern-uri:


### 1. **Command Pattern**
Command Pattern este utilizat pentru a encapsula fiecare operatiune intr-o clasa separata, oferind un mod structurat de a defini si executa comenzile. Fiecare comanda, cum ar fi `AddFundsCommand`, `SendMoneyCommand` sau `DeleteAccountCommand`, implementeaza interfata Command si contine logica specifica operatiunii. Aceasta abordare permite adaugarea usoara de noi functionalitati fara a modifica codul existent.

### 2. **Strategy Pattern**
Strategy Pattern este folosit pentru a gestiona strategiile de cashback ale comerciantilor. Fiecare comerciant are o strategie de cashback specifica, cum ar fi `nrOfTransactions` sau `spendingThreshold`, care este implementata intr-o clasa separata. Aceasta separare permite selectarea si aplicarea dinamica a strategiei de cashback in functie de tipul comerciantului, asigurand flexibilitate si reutilizare a codului.

### 3. **Factory Pattern**
Factory Pattern este utilizat pentru crearea obiectelor complexe, precum conturi si carduri. Clasele `AccountFactory` si `CardFactory` se ocupa de instantierea diferitelor tipuri de conturi (classic, savings) si carduri (normal, one-time), simplificand procesul de creare si centralizand logica de instantiere intr-un singur loc. Acest pattern imbunatateste claritatea codului si faciliteaza extinderea cu noi tipuri de conturi sau carduri in viitor.

### 4. **Chain of Responsibility**
Chain of Responsibility este implementat pentru procesarea upgrade-urilor de planuri ale utilizatorilor. Cererile de upgrade sunt trecute printr-un lant de verificari, fiecare nivel (de exemplu, trecerea de la standard la silver sau de la silver la gold) fiind responsabil pentru aplicarea conditiilor specifice. Acest design permite gestionarea modulara si extensibila a logicii complexe de upgrade, fara a duce la un cod supraincarcat.

## Diferite functionalitati implementate

Flow-ul procesului pentru comanda `acceptSplitPayment` incepe prin identificarea tranzactiei active a utilizatorului pe baza email-ului. Daca tranzactia exista si utilizatorul face parte din aceasta, raspunsul sau (`accept`) este inregistrat in statusul tranzactiei. Sistemul verifica apoi daca toti utilizatorii implicati au acceptat tranzactia (`allAccepted`). Daca da, tranzactia este procesata prin executarea comenzii `SplitPaymentCommand`, iar tranzactia este eliminata din coada fiecarui utilizator.

In cazul in care nu toti utilizatorii au raspuns, sistemul logheaza email-urile celor care inca nu au acceptat. Daca utilizatorul nu are o tranzactie activa, se arunca o exceptie care indica acest lucru.

Acest proces asigura ca tranzactia este procesata doar atunci cand toti utilizatorii si-au dat acordul, gestionand totodata utilizatorii care nu au raspuns.

Dupa ce o tranzactie, cum ar fi una `SendMoney`, este procesata si suma este dedusa din contul expeditorului, verificam daca valoarea tranzactiei, convertita in RON, depaseste pragul de 300 RON. Daca acest prag este atins, se incrementeaza un contor specific utilizatorului care monitorizeaza tranzactiile calificate pentru upgrade.






