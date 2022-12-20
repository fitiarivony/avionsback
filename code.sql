create database flotteauto;
alter database flotteauto owner to societe;


create sequence vehicule_seq;
create sequence typeassurance_seq;
create sequence typeentretien_seq;
create sequence visitetech_seq;
create sequence kilometrage_seq;
create sequence admin_seq;

create table vehicule(
    idvehicule varchar(10) default 'VEH'||nextval('vehicule_seq') primary key,
    numeromatricule varchar(20) not null,
    puissance float not null,
    prix_neuf float not null,
    datedebut date default current_timestamp
);
insert into vehicule(numeromatricule,puissance,prix_neuf,datedebut) values 
('4578TKA',7000,3000000,'29/04/2022'),
('4211TKO',2000,4000000,'20/03/2022'),
('2389TKH',30000,500000,'22/04/2021'),
('5690TGH',5000,20000000,'22/04/2020')
;

create table typeassurance(
    idtypeassurance varchar(10) default 'ASS'||nextval('typeassurance_seq')  primary key,
    nomassurance varchar(40) not null
);
insert into typeassurance (nomassurance) values
 ('assurance tier')
 ;
create table assurance_vehicule(
    idvehicule varchar(10),
    idtypeassurance varchar(10),
    datedebut date not null default current_timestamp,
    datefin date not null, 
    prix float  not null,
    FOREIGN KEY (idvehicule) references vehicule(idvehicule),
    FOREIGN KEY (idtypeassurance) references typeassurance(idtypeassurance)
);
insert into assurance_vehicule(idtypeassurance,datedebut,datefin,prix,idvehicule) values 
('ASS1','20/04/2021','20/05/2023',300000,'VEH1'),
('ASS1','20/04/2021','20/04/2023',300000,'VEH1')
;

create table typeentretien(
    idtypeentretien varchar(10) default 'ENT'||nextval('typeentretien_seq') primary key,
    nomentretetien varchar(40) not null
);
insert into typeentretien (nomentretetien) values
('vidange')
;

create table entretien_vehicule(
    idvehicule varchar(10),
    idtypeentretien varchar(10),
    dateentretien date not null,
    prix float not null,
    FOREIGN KEY (idvehicule) references vehicule(idvehicule),
     FOREIGN KEY (idtypeentretien) references typeentretien(idtypeentretien)
);
insert into entretien_vehicule(idvehicule, idtypeentretien,dateentretien,prix)values
('VEH1','ENT1','20/03/2022',400000)
;

create table visite_technique(
    idvisite_technique varchar(10) default 'VIS'||nextval('visitetech_seq') primary key,
    datevisite date not null,
    idvehicule varchar(10),
    FOREIGN KEY (idvehicule) references vehicule(idvehicule)
);
insert into visite_technique(datevisite,idvehicule) values 
('10/03/2022','VEH1')
;

create table kilometrage(
    idkilometrage varchar(10) default 'KIL'||nextval('kilometrage_seq') primary key,
    idavion varchar(10),
    datekilometrage date default current_timestamp,
    debutkm float,
    finkm float,
    FOREIGN KEY (idavion) references avion(idavion)
);


create table administrateur(
    idadmin varchar(10) default 'ADM'||nextval('admin_seq'),
    identifiant varchar(50) not null,
    mdp varchar(50) not null,
    token varchar(100) not null,
    dateExpiration timestamp default current_timestamp+'15:00'::time
);
insert into administrateur(identifiant,mdp,token) values 
('admin',md5('admin'),'dffsdf12')
;

create or replace view assurance_vehicule_detail
as
select vehicule. idvehicule,vehicule.numeromatricule,vehicule.puissance,vehicule.prix_neuf,vehicule.datedebut dateutilisation,
assurance_vehicule.datedebut,assurance_vehicule.datefin,prix,
assurance_vehicule.idtypeassurance,nomassurance
from vehicule
join assurance_vehicule on vehicule.idvehicule=assurance_vehicule.idvehicule
join typeassurance on typeassurance.idtypeassurance=assurance_vehicule.idtypeassurance
;


