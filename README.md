#### manGOFusion GmbH
# Shop Assist

## Cerințele proiectului

Proiectul propus – „Shop Assist – Aplicație mobilă pentru facilitarea oferirii de ajutor în cumpărături” – reprezintă o soluție software, de tip aplicație mobilă, destinată intermedierii între persoanele care nu au posibilitatea să se deplaseze la cumpărături, din varii motive, și persoanele care doresc să îndeplinească această sarcină pentru cei întâi menționați.
Cerințele inițiale ale proiectului sunt următoarele:
*	Utilizatorii pot lua atât rolul de persoană care solicită servicii din partea altora de a li se livra cumpărături, cât și rolul de persoană care să presteze serviciile în favoarea primului rol;
*	Aplicația oferă permite comunicarea a listei de cumpărături, inclusiv a eventualelor observații sau cerințe suplimentare;
*	Bonul cuprinzând articolele achiziționate va fi transmis prin intermediul aplicației;
*	O solicitare pentru cumpărături va fi trimisă mai multor utilizatori, fiind îndeplinită de primul utilizator care răspunde la aceasta.


## Specificațiile proiectului

Specificațiile aplicației sunt cele care urmează:
*	Aplicația mobilă oferă posibilitatea atât ca utilizatorul să solicite achiziționarea și livrarea produselor de către o altă persoană (rol denumit în documentație ca „Solicitant”), cât și să ofere ajutor Solicitanților (rol denumit în documentație ca „Prestator”);
*	Fiecare utilizator trebuie să aibă un cont de utilizator, creat prin introducerea câtorva date, printre care numele, prenumele, adresa de e-mail și numărul de telefon. Pentru a trimite o solicitare, contul trebuie să conțină și adresa completă a utilizatorului. Prin configurarea ulterioară a stării contului, utilizatorul va putea alege dacă și când poate fi solicitat ca Prestator.
*	Pagina de pornire a aplicației va afișa eventualele cereri primite sau stări ale solicitărilor trimise;
*	Pentru ca Solicitantul să trimită o cerere, acesta va accesa fila „Solicită cumpărături”. În respectiva filă, Solicitantul completează lista de cumpărături, eventualele observații generale și statutul Solicitantului, dintre variantele existente, printre care „persoană vârstnică”, „persoană izolată la domiciliu”, „persoană cu dizabilități locomotorii”, „altul”. Dacă lista nu este mai lungă de cinci produse diferite, acesta are posibilitatea să seteze prioritate ridicată pentru solicitare. În plus, Solicitantul poate preciza Prestatorului o sumă modică, pentru serviciile de achiziție și livrare oferite. Când Solicitantul a terminat de completat lista de cumpărături, acesta va apăsa butonul „Trimite cererea”. Utilizatorii nu pot solicita mai mult de două cereri simultane;
*	La momentul trimiterii unei cereri, aceasta va fi difuzată utilizatorilor disponibili de pe o rază stabilită de către administratorul aplicației. Primul utilizator care acceptă cererea devine Prestatorul solicitării. În cazul în care niciun utilizator nu răspunde unei solicitări, aceasta va expira în 12 ore, trebuind să fie retrimisă dacă Solicitantul dorește acest lucru.
*	Prestatorul va citi lista de cumpărături și eventualele observații și, pe măsură ce găsește articolele cerute, le bifează. După achitarea cumpărăturilor, Prestatorul va completa prețul plătit și va trimite o poză cu bonul fiscal, date pe care le primește și Solicitantul, alături de eventualele produse care nu au fost bifate. Prestatorul va livra personal cumpărăturile Solicitantului, acesta din urmă trebuind să achite toate produsele solicitate și cumpărate, alături de eventuala suma modică precizată la trimiterea cererii. Ambele persoane vor marca ulterior cererea ca încheiată;
*	Dacă Solicitantului i-au rămas produse neachiziționate, va putea începe din nou o listă care să includă respectivele articole.

Specificațiile tehnice ale proiectului sunt următoarele:
* Pentru implementarea cerințelor se vor folosi următoarele limbaje și tehnologii:
  * Mediul de dezvoltare Android Studio;
  * Limbajul de programare orientat pe obiecte Kotlin.
