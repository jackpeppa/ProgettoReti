# ProgettoReti

Comandi per avviare il progetto(compilazione--->avvio server--->avvio client)

WINDOWS:
javac -classpath ./json-simple-1.1.1.jar ./common/*.java ./client/view/*.java  ./client/controller/*.java ./server/model/*.java ./server/controller/*.java

java -cp ".;./json-simple-1.1.1.jar" server.controller.TuringServer

java -cp ".;./json-simple-1.1.1.jar" client.controller.TuringClient

LINUX:
javac -classpath ./json-simple-1.1.1.jar ./common/*.java ./client/view/*.java  ./client/controller/*.java ./server/model/*.java ./server/controller/*.java

java -cp ".:./json-simple-1.1.1.jar" server.controller.TuringServer

java -cp ".:./json-simple-1.1.1.jar" client.controller.TuringClient


NB: la cartella documenti "client_turing_docs" è condivisa tra tutti i client che condividono lo stesso codice e girano sulla stessa macchina. 
    Quindi, se si sta editando una sezione con un client, e si esegue il comando "show" della medesima, il file verrà sovrascritto con la versione 
    precedente presente sul server.

NB: quando si vuole eseguire il comando "show" su tutto il documento è sufficiente lasciare il campo "Section" vuoto.
