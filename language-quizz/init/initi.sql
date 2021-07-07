create database lgquizz;
drop table if exists lgquizz.TestResponses;
drop table if exists lgquizz.TestParticipants;
drop table if exists lgquizz.TestContents;
drop table if exists lgquizz.Tests;
drop table if exists lgquizz.Responses;
drop table if exists lgquizz.Exercises;
drop table if exists lgquizz.Problems;
drop table if exists lgquizz.Lessons;
drop table if exists lgquizz.Users;
drop table if exists lgquizz.Categories;


create table lgquizz.Users(
	id_user int primary key auto_increment,
    email varchar(50) unique not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    password varchar(50) not null
);

create table lgquizz.Lessons(
	id_lesson int primary key auto_increment,
    lesson_name varchar(50) not null,
    lesson_description varchar(500)
);

create table lgquizz.Categories(
	id_category int primary key auto_increment,
    category_name varchar(50) not null,
    category_description varchar(500)
);

create table lgquizz.Problems(
	id_problem int primary key auto_increment,
    id_lesson int not null,
    id_category int not null,
    problem_text varchar(100) not null,
	foreign key (id_lesson) references lgquizz.Lessons(id_lesson),
    foreign key (id_category) references lgquizz.Categories(id_category)
);

create table lgquizz.Exercises(
	id_exercise int primary key auto_increment,
    id_problem int not null,
    content varchar(250) not null,
    mark double not null,
    constraint `EXERCISE_CHECK_FAIL` check(mark<=10 AND mark>=0),
    foreign key (id_problem) references lgquizz.Problems(id_problem)
);
create table lgquizz.Responses(
	id_response int primary key auto_increment,
    id_exercise int not null ,
    content varchar(250) not null,
    percentage double,
    foreign key (id_exercise) references Exercises(id_exercise),
    constraint `RESP_CHECK_FAIL` check(percentage<=1 AND percentage>=0),
    constraint `UNIQUE_ANS` unique(id_exercise,content)
);

create table lgquizz.Tests(
	id_test int primary key auto_increment,
    test_name varchar(100) unique,
    restricted bool not null,
    expiration_date timestamp
);

create table lgquizz.TestContents(
	id_test int,
    id_problem int,
    number_of_exercises int,
    primary key(id_test,id_problem),
    foreign key (id_test) references lgquizz.Tests(id_test) on delete cascade,
    foreign key (id_problem) references lgquizz.Problems(id_problem) on delete cascade
);

create table lgquizz.TestParticipants(
	id_test int,
    id_user int,
    primary key (id_test,id_user),
    foreign key (id_test) references lgquizz.Tests(id_test) on delete cascade,
    foreign key (id_user) references lgquizz.Users(id_user) on delete cascade
);

create table lgquizz.TestResponses(
	id_test int,
    id_response int,
	id_user int,
    primary key(id_test,id_response,id_user),
    foreign key (id_test) references lgquizz.Tests(id_test) on delete cascade,
    foreign key (id_user) references lgquizz.Users(id_user) on delete cascade,
    foreign key (id_response) references lgquizz.Responses(id_response) on delete cascade
);


-- exemples taken from  Annie Heminway Practice Makes Perfect Complete French Grammar (2007 )
insert into lgquizz.Categories(`category_name`) values('Fill in the blanks'),('Multiple choice');
insert into lgquizz.Lessons(`lesson_name`) values('The present tense of regular -er verbs');
insert into lgquizz.Problems(`problem_text`,`id_lesson`,`id_category`) values('Mettre les verbes entre parenthèses au présent.',1,1); 
insert into lgquizz.Exercises(`content`,`mark`,`id_problem`) values('Lucie $BLANK(travailler) à Lyon.',0.15,1),
																	('Mon frère et moi, nous $BLANK(accepter) votre invitation.',0.15,1),
																	('M. et Mme Benoît $BLANK(chercher) un appartement.',0.15,1),
																	('Tu $BLANK(apporter) toujours des fl eurs.',0.15,1),
																	('Vous $BLANK(bavarder) sur la terrasse.',0.15,1),
																	('Je $BLANK(commander) une soupe de légumes.',0.15,1),
																	('Ils $BLANK(habiter) en Normandie.',0.15,1),
																	('Elle $BLANK(déjeuner) avec sa belle-sœur.',0.15,1),
																	('Vous $BLANK(dessiner) très bien.',0.15,1),
																	('Nous $BLANK(visiter) le château de Fontainebleau.',0.15,1),
                                                                    ('Nous $BLANK(commencer) à huit heures le matin.',0.15,1),
																	('Vous $BLANK(avancer) rapidement.',0.15,1),
																	('Je $BLANK(déplacer) les meubles du salon.',0.15,1),
																	('Nous $BLANK(devancer) nos concurrents.',0.15,1),
																	('Nous $BLANK(annoncer) une augmentation de salaire au début de l’année.',0.15,1),
																	('Tu $BLANK(effacer) le tableau.',0.15,1),
																	('Nous $BLANK(remplacer) toute l’équipe.',0.15,1),
																	('Ils $BLANK(exercer) une grande infl uence.',0.15,1),
																	('Nous $BLANK(financer) ce projet.',0.15,1),
																	('Elle $BLANK(menacer) de partir.',0.15,1),
                                                                    ('Vous $BLANK(mélanger) les ingrédients.',0.15,1),
																	('Je $BLANK(ranger) mes aff aires.',0.15,1),
																	('Nous $BLANK(exiger) votre présence à la réunion.',0.15,1),
																	('Ils $BLANK(déménager) demain.',0.15,1),
																	('Elle $BLANK(héberger) ses amis.',0.15,1),
																	('Vous $BLANK(corriger) les exercices.',0.15,1),
																	('Nous $BLANK(manger) sur la terrasse à midi.',0.15,1),
																	('Tu $BLANK(nager) dans la piscine.',0.15,1),
																	('Nous $BLANK(encourager) ces jeunes talents.',0.15,1),
																	('Souvent, il $BLANK(changer) d’avis.',0.15,1),
                                                                    ('Il $BLANK(renouveler) son passeport.',0.15,1),
																	('Je $BLANK(emmener) ma nièce à l’opéra.',0.15,1),
																	('Vous $BLANK(acheter) un kilo de haricots.',0.15,1),
																	('Le sorcier $BLANK(ensorceler) le public.',0.15,1),
																	('Elle $BLANK(espérer) aller à Paris en mai.',0.15,1),
																	('Vous $BLANK(exagérer).',0.15,1),
																	('Elle $BLANK(s’appeler) Juliette.',0.15,1),
																	('Ces diamants $BLANK(étinceler) de mille feux.',0.15,1),
																	('Il $BLANK(répéter) mille fois la même chose.',0.15,1),
																	('Nous $BLANK(célébrer) son anniversaire.',0.15,1),
																	('Aujourd’hui, il $BLANK(acheter) du poisson au marché.',0.15,1),
																	('Ils $BLANK(travailler) le samedi.',0.15,1),
																	('Vous $BLANK(emprunter) de l’argent à la banque.',0.15,1),
																	('Tu $BLANK(aimer) voyager en bateau.',0.15,1),
																	('Nous $BLANK(renoncer) à notre projet.',0.15,1),
																	('Je $BLANK(habiter) au dixième étage.',0.15,1),
																	('L’après-midi, elle $BLANK(préférer) aller dans le parc.',0.15,1),
																	('Comment $BLANK(s’appeler) sa sœur?',0.15,1),
																	('De temps en temps, nous $BLANK(bavarder) pendant la pause-café.',0.15,1),
																	('Il vous $BLANK(rappeler) avant midi.',0.15,1)
                                                                    ;
insert into lgquizz.Problems(`problem_text`,`id_lesson`,`id_category`) values('Traduire en français.',1,1);
insert into lgquizz.Exercises(`content`,`mark`,`id_problem`) values('We refuse the invitation.\n$BLANK',0.15,2),
																	('She cancels the trip.\n$BLANK',0.15,2),
																	('He speaks French.\n$BLANK',0.15,2),
																	('You bring some flowers. (formal)\n$BLANK',0.15,2),
																	('I cut the bread.\n$BLANK',0.15,2),
																	('They are having lunch with Julie.\n$BLANK',0.15,2),
																	('He borrows ten euros.\n$BLANK',0.15,2),
																	('I order a dessert.\n$BLANK',0.15,2),
																	('You study Russian. (informal)\n$BLANK',0.15,2),
																	('They are looking for a good restaurant.\n$BLANK',0.15,2)
                                                                    ;
                                                                    
insert into lgquizz.Problems(`problem_text`,`id_lesson`,`id_category`) values('Reformuler les phrases en utilisant être en train de + infinitif.',1,1);
insert into lgquizz.Exercises(`content`,`mark`,`id_problem`) values ('Nous chantons une chanson.\n$BLANK(50)',0.15,3),
																	('Elle dessine un mouton.\n$BLANK(50)',0.15,3),
																	('Je travaille dans la cuisine.\n$BLANK(50)',0.15,3),
																	('Tu eff aces le tableau.\n$BLANK(50)',0.15,3),
																	('Vous étudiez l’histoire européenne.\n$BLANK(50)',0.15,3),
																	('Nous bavardons dans le jardin.\n$BLANK(50)',0.15,3),
																	('Il corrige les copies.\n$BLANK(50)',0.15,3),
																	('Tu laves la chemise.\n$BLANK(50)',0.15,3),
																	('Je range mes aff aires.\n$BLANK(50)',0.15,3),
																	('Elle mange une omelette aux champignons.\n$BLANK(50)',0.15,3)
                                                                    ;                                                                    

insert into lgquizz.Problems(`problem_text`,`id_lesson`,`id_category`) values('Répondre aux questions en utilisant le présent et depuis',1,1);
insert into lgquizz.Exercises(`content`,`mark`,`id_problem`) values ('Depuis combien de temps chante-t-elle dans cette chorale? (trois ans)\n$BLANK(50)',0.15,4),
																	('Depuis combien de temps partages-tu cet appartement? (six mois)\n$BLANK(50)',0.15,4),
																	('Depuis combien de temps nage-t-il dans cette piscine? (un mois)\n$BLANK(50)',0.15,4),
																	('Depuis quand habitez-vous à Montpellier? (2004)\n$BLANK(50)',0.15,4),
																	('Depuis combien de temps possède-t-il cette propriété? (dix ans)\n$BLANK(50)',0.15,4),
																	('Depuis combien de temps regardez-vous cette émission? (des années)\n$BLANK(50)',0.15,4),
																	('Depuis quand travaille-t-il dans cette entreprise? (2002)\n$BLANK(50)',0.15,4),
																	('Depuis combien de temps portez-vous des lunettes?\n$BLANK(50)',0.15,4),
																	('Depuis quand est-il président? (2005)\n$BLANK(50)',0.15,4),
																	('Depuis combien de temps ce magasin est-il fermé? (deux mois)\n$BLANK(50)',0.15,4)
                                                                    ;
                                                                    
insert into lgquizz.Problems(`problem_text`,`id_lesson`,`id_category`) values('Traduire les phrases suivantes en utilisant vous et l’inversion si nécessaire.',1,1);
insert into lgquizz.Exercises(`content`,`mark`,`id_problem`) values('I study French.\n$BLANK(50)',0.15,5),
																	('I spell my name.\n$BLANK(50)',0.15,5),
																	('They are moving tomorrow.\n$BLANK(50)',0.15,5),
																	('She likes to travel by boat.\n$BLANK(50)',0.15,5),
																	('How long have you been studying French?\n$BLANK(50)',0.15,5),
																	('You repeat the sentence. (informal)\n$BLANK(50)',0.15,5),
																	('We are fi nancing the project.\n$BLANK(50)',0.15,5),
																	('She cancels the meeting.\n$BLANK(50)',0.15,5),
																	('How long have you been living in this house?\n$BLANK(50)',0.15,5),
																	('I weigh the vegetables.\n$BLANK(50)',0.15,5)
                                                                    ; 

insert into lgquizz.Lessons(`lesson_name`) values('The present tense of -ir and -re verbs');
insert into lgquizz.Problems(`problem_text`,`id_lesson`,`id_category`) values('Mettre les verbes entre parenthèses au présent.',2,1);
insert into lgquizz.Exercises(`content`,`mark`,`id_problem`) values ('Nous $BLANK(10)(cueillir) des fl eurs dans le jardin.',0.15,6),
																	('Ils $BLANK(10)(finir) à dix-huit heures.',0.15,6),
																	('Je $BLANK(10)(remplir) les verres des invités.',0.15,6),
																	('Nous $BLANK(10)(investir) dans l’immobilier.',0.15,6),
																	('Ils $BLANK(10)(mentir) à la police.',0.15,6),
																	('Tu $BLANK(10)(ouvrir) les fenêtres du salon.',0.15,6),
																	('Vous $BLANK(10)(réfléchir) à leur proposition.',0.15,6),
																	('Je $BLANK(10)(sentir) les bonnes odeurs de la cuisine.',0.15,6),
																	('Ils $BLANK(10)(offrir) toujours les mêmes fleurs.',0.15,6),
																	('Il $BLANK(10)(mourir) de faim.',0.15,6)
                                                                    ;
                                                                    
insert into lgquizz.Tests(`test_name`,`restricted`) values ('Present verbs',false);
insert into lgquizz.TestContents(`id_test`,`id_problem`,`number_of_exercises`) values(1,1,4),(1,2,2),(1,3,5);
