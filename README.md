# Progetto Settimana 15 — Social Network

API REST di un social network con Spring Boot, Spring Security e JWT.
Dopo il login il client riceve un token JWT da mandare in ogni richiesta con l'header
`Authorization: Bearer <token>`. I ruoli sono due: **MEMBER** e **MODERATOR**.
Il primo utente registrato diventa automaticamente MODERATOR (altrimenti non esisterebbe
mai il primo moderatore), tutti gli altri partono da MEMBER.

## Regole di autorizzazione scelte

### Registrazione e Login (pubblici)
`POST /api/utenti` e `POST /api/auth/login` → **permitAll()**
La registrazione deve essere libera e il login serve proprio ad ottenere il token,
quindi non possono richiedere autenticazione. Tutto il resto è protetto.

### Profilo — `GET /api/utenti/me`
**Basta essere autenticati.** L'utente restituito è quello del token, non un id nella URL,
quindi non c'è modo di leggere i dati di qualcun altro.

### Promozione — `PATCH /api/utenti/{id}/promuovi`
**hasRole('MODERATOR')** — regola basata sul ruolo: promuovere un utente è un potere
amministrativo, quindi solo un moderatore può farlo. Un MEMBER riceve 403.

### Creazione post — `POST /api/posts`
**Basta essere autenticati.** L'autore non è nel payload ma viene preso dal token,
così è impossibile pubblicare a nome di un altro.

### Lettura post — `GET /api/posts`, `/api/posts/{id}`, `/api/posts/autore/{autoreId}`
**Basta essere autenticati.** I post sono visibili a tutta la community,
ma solo agli iscritti.

### Modifica post — `PUT /api/posts/{id}`
**hasRole('MODERATOR') or isAutore** — regola basata sulla proprietà: un post lo modifica
solo chi l'ha scritto. Il moderatore è l'eccezione perché modificare i contenuti degli
altri è proprio il suo compito (es. contenuti inappropriati).

### Like — `POST /api/posts/{id}/like` e `DELETE /api/posts/{id}/like`
**Basta essere autenticati.** L'utente del like è sempre quello del token.
La DELETE non riceve l'id del like ma solo quello del post: cancella il like
dell'utente loggato su quel post, quindi non si può togliere il like di un altro.
Il vincolo "un solo like per post" è gestito nel service, non da Spring Security.

## In sintesi

- **Solo autenticazione** per le azioni base (leggere, postare, like), con l'identità
  sempre presa dal token e mai dal body
- **Ruolo** per i poteri amministrativi (promozione)
- **Proprietà** per la modifica dei post, col moderatore come eccezione
