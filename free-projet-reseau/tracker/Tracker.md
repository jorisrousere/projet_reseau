# GENERAL

- TCP
- Messages sour forme de texte 
- Caratere blanc permet de separer
- Pieces echangées de taille égale

# TYPES DE MESSAGES

- Annonce de presence au tracker (tracker <- peer)

(tracker <- peer):
- Je suis un peer et j'ai
- Je suis un peer et je veux un fichiera avec tels criteres
- Mise a jour d'état de fichier 

(peer <- tracker):
- Reponse a l'annonce de presence ?
- Paquet avec la liste de fichiers
- Je veux ce fichier



(peer <-> peer) : 
- Initier une connexion en indiquant l'interet pour un fichier
- Reponse avec une buffer map
- Information de telechargement a ses peers





- Demande de liste des fichier presents ((tracker <- peer))

- Demande de telechargement d'un fichier 
- Initiation de la connexion avec les pairs concernés
- Liste des gens possedant le fichier



# TRACKER 


On doit pouvoir spécifier le port d'écoute au lancement 


- Garde une liste de ce que possède chaque client = structure client
- Elle peut être mise a jour 
- Etre capable d'envoyer des infos sur les gens possedant les fichiers, et de quoi s'y connecter
- Algo de recherche opti ? hashtable ?
- Thread pool


- Logs

# PEER
Developpement d'un peer dans le contexte d'une application d'echange peer to peer centralisés le peer doit être capable en JAVA:

Les communications entre entitées se fait en TCP, avec des messages sous forme de texte.

On aura differentes communications avec le tracker : 
- Le peer doit pouvoir annoncer au tracker sa présence avec les differents fichiers qu'il possède 
- Un pair peut à tout moment demander au Tracker la liste des fichiers présents dans le réseau vérifiant un certain nombre de critères eux-mêmes transmis en paramètre.
- Si le pair est interessé par un fichier il demande la liste des peers le possedant
- Après avoir obtenu la liste des pairs auxquels se connecter, le pair initiera une connexion avec chacun de ces pairs leur indiquant son intérêt pour le fichier désiré. Les pairs devront alors répondre avec des informations (BufferMap) concernant les parties du fichier qu’ils ont en leur possession. 
-Périodiquement, le pair informe ses voisins de l’état d’avancement du téléchargement en envoyant son nouveau buffermap. Ses voisins répondent par leur dernier buffermap. Ainsi un pair peut découvrir la disponibilité de nouvelles pièces dans son voisinage.
- Le pair informe périodiquement le Tracker des différents fichiers disponibles ou en cours de téléchargement.


Je veux définition de l’architecture générale du projet, découpage du projet en tâches, planification des tâches, affectation des tâches à des responsables, indicateurs de suivi de réalisation des tâches


Lorsqu'il est seeder de contribuer a la totalité du fichier et lorsqu'il est leecher par les parties qu'il possède

- Mise a jour pendant le telechagement de ce qui est disponible