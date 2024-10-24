# progettoFinale

Gestione delle risorse umane, ovvero quello di una piattaforma in cui i dipendenti possono registrare le proprie presenze, richiedere ferie e visualizzare le busta paga, mentre gli amministratori possono gestire i dipendenti, approvare le richieste di ferie e generare un report su di loro(un feedback se il dipendente è stato produttivo o meno).
Frontend: React e Bootstrap per creare un portale dipendenti e amministratori.
Backend: Spring Boot per gestire il flusso delle ferie, presenze, buste paga e gestione dei dipendenti.
Database: PostgreSQL per la gestione dei dati dei dipendenti, ferie, presenze e salari.

BACKEND

1. Autenticazione e Autorizzazione
   • Autenticazione: Implementazione un sistema di login per i dipendenti e gli amministratori
   • Ruoli e permessi: Differenziare i permessi tra dipendenti e amministratori. Ad esempio, i dipendenti possono registrare le presenze, richiedere ferie e visualizzare le buste paga, mentre gli amministratori possono approvare richieste di ferie, gestire i dipendenti e generare report.
2. Gestione dei Dipendenti
   • Crud Dipendenti: Creare funzionalità per aggiungere, modificare e rimuovere i dipendenti.
   • Profili personali: Ogni dipendente dovrebbe poter gestire il proprio profilo.
   • Gestione ruoli: Gli amministratori devono poter gestire i ruoli dei dipendenti
3. Gestione Presenze
   • Registrazione Presenze: Permettere ai dipendenti di registrare le proprie presenze giornaliere (ad esempio, l'ora di entrata e di uscita).
   • Calcolo delle ore lavorate: Implementa logiche per il calcolo delle ore giornaliere o settimanali lavorate.
   • Assenze e permessi: Includi una gestione per le assenze non pianificate (es. malattie).
4. Gestione Ferie
   Richiesta ferie: Implementare un sistema in cui i dipendenti possono richiedere giorni di ferie specifici.
   • Approvazione ferie: Gli amministratori devono poter approvare o rifiutare le richieste di ferie.
   • Saldo ferie: Ogni dipendente ha un saldo ferie, che deve essere aggiornato dopo ogni richiesta approvata o ferie utilizzate.
5. Gestione Buste Paga
   • Visualizzazione busta paga: I dipendenti devono poter visualizzare le proprie buste paga mensili o settimanali.
   • Generazione automatica: Gli amministratori o una funzione devono generare le buste paga basate su ore lavorate, eventuali straordinari, ferie, e detrazioni.
6. Gestione Report Produttività
   • Report di produttività: Gli amministratori devono poter generare report mensili per verificare la produttività dei dipendenti (es. giorni lavorati, assenze, ferie richieste).
   • Indicatori di performance: Puoi implementare metriche di performance basate su vari criteri (presenze, ferie, produttività).

FRONTEND
TROVATE IL LINK DEL FRONTEND SU: https://github.com/Edyan90/Frontend_Risorse_umane.

7. Login
   • Schermata di Login: Dal login si deve capire se l’utente è un manager oppure un dipendente. 8. Dashboard e Visualizzazione Dati
   • Dashboard per dipendenti: Mostrare informazioni rilevanti come ore lavorate, ferie rimanenti, prossime ferie programmate, e l'ultima busta paga disponibile.
   • Dashboard per amministratori: Una vista completa per monitorare lo stato generale dei dipendenti, richieste di ferie in attesa, e un riepilogo delle presenze. 9. Storico Presenze
   • Storico delle presenze e ferie: Permettere agli amministratori di accedere a uno storico delle presenze e delle ferie di ciascun dipendente.
   • Servizi per la registrazione di eventi nel sistema

EXTRA
• Notifiche email: Interrare un sistema di notifiche che avvisa i dipendenti quando le loro richieste di ferie vengono approvate o quando viene pubblicata una nuova busta paga.
• Notifiche push: Implementare notifiche push via web o mobile sul telefono.
• Tracciamento delle operazioni: Implementa una funzionalità che traccia le modifiche apportate da amministratori (ad es. approvazione delle ferie o modifiche a buste paga).
• Integrazione con calendari: Sincronizzare le ferie con i calendari personali dei dipendenti (es. Google Calendar).
