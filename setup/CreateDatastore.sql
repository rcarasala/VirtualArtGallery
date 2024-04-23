CREATE DATABASE if not exists virtualartgallery;

\c virtualartgallery

CREATE TABLE if not exists arts 
  ( 
     barcode   VARCHAR(100) PRIMARY KEY, 
     name      TEXT NOT NULL, 
     artist    VARCHAR(100) NOT NULL, 
     price     INT, 
     quantity  REAL
  ); 
  
  CREATE TABLE if not exists users
  ( 
     username  VARCHAR(100) PRIMARY KEY, 
     password  VARCHAR(100) NOT NULL, 
     firstname VARCHAR(100) NOT NULL, 
     lastname  VARCHAR(100) NOT NULL, 
     address   TEXT NOT NULL, 
     phone     VARCHAR(100) NOT NULL, 
     mailid    VARCHAR(100) NOT NULL,
     usertype  INT
  ); 