
CREATE table users(
	id int primary key auto_increment,
    userName varchar(20),
    userPassword varchar(30),
    eMail varchar(30),
    `name` varchar(20),
    surname varchar(40),
    dateOfBirth Date,
    address varchar(50),
    phoneNumber varchar(20),
    registrationDateAndTime datetime,
    userType varchar(15),
    userBlocked varchar(6)
);

create table genres(
	id int primary key auto_increment,
    `name` varchar(40),
    description varchar(300),
    deleted varchar(6)
);

create table books(
	id bigint primary key auto_increment,
    `name` varchar(30),
    publicher varchar(30),
    authors varchar(100),
    releaseDate Date,
    `description` varchar(200),
    imagePath varchar(30),
    price float,
    numberOfPages int,
    bookType varchar(20),
    letter varchar(20),
    bookLanguage varchar(30),
    avgRating float,
    numberOfCopies int
);

create table bookGenre(
	bookId bigint,
    genreId int,
    primary key(bookId, genreId),
    foreign key(bookId) references books(id)
		on delete cascade,
	foreign key(genreId) references genres(id)
		on delete cascade
);

create table shoppingCarts(
	id int primary key auto_increment,
    numberOfCopies int
);

create table shoppingCartUserBook(
	cartId int,
	bookId bigint,
    userId int,
    primary key(cartId, bookId, userId),
    foreign key(cartId) references shoppingCarts(id)
		on delete cascade,
	foreign key(bookId) references books(id)
		on delete cascade,
	foreign key(userId) references users(id)
		on delete cascade
);
create table shop(
	id int primary key auto_increment,
    price float,
    shoppingDate Date,
    bookSum int
);

create table shopUserCart(
	shopId int,
	shoppingCartsId int,
    primary key(shopId, shoppingCartsId),
    foreign key(shopId) references shop(id)
		on delete cascade,
	foreign key(shoppingCartsId) references shoppingCarts(id)
		on delete cascade
);

insert into users(id,userName,userPassword,eMail,`name`,surname,dateOfBirth,address,phoneNumber, registrationDateAndTime,userType,userBlocked) 
values(1,'brvj','sifra','jankovicb0230@gmail.com','Boris','Jankovic',current_date(),'Jovana Jovanovica Zmaja 3','0696354635','2020-01-12 10:10:10','Administrator','false');

insert into genres(id,`name`,description,deleted) values (1,'Horror','A horror film is one that seeks to elicit fear in its audience for entertainment purposes.','false');
insert into genres(id,`name`,description,deleted) values(2,'Comedy','Comedy is a literary genre and a type of dramatic work that is amusing and satirical in its tone, mostly having a cheerful ending.','false');

insert into books(id ,`name` , publicher , authors , releaseDate , description , imagePath , price , numberOfPages , bookType , letter , bookLanguage , avgRating , numberOfCopies ) 
values(1000000000001,'Dracula','Public domain','Bram Stoker','1897-05-26','The novel tells the story of Draculas attempt to move from Transylvania to England so that he may find new blood and spread the undead curse','dracula-cover.jpg',680,418,'Tvrdi','Latinica','Srpski',4.0,100);
insert into books(id ,`name` , publicher , authors , releaseDate , description , imagePath , price , numberOfPages , bookType , letter , bookLanguage , avgRating , numberOfCopies ) 
values(1000000000002,'Good Omens','Gollancz','Terry Pratchett,Neil Gaiman','1990-05-10','The book is a comedy about the birth of the son of Satan and the coming of the End Times.','220px-Goodomenscover.jpg',1000,288,'Tvrdi','Latinica','Srpski',4.24,50);

insert into bookGenre(bookId,genreId) values(1000000000001,1);
insert into bookGenre(bookId,genreId) values(1000000000002,1);
insert into bookGenre(bookId,genreId) values(1000000000002,2);

insert into shoppingCarts(id, numberOfCopies) values (1,2);
insert into shoppingCartUserBook(cartId,bookId,userId) values(1,1000000000001,3)












