DROP TABLE public.images CASCADE;
DROP TABLE public.cameras CASCADE;
DROP TABLE public.user CASCADE;
DROP TABLE public.privileges CASCADE;

CREATE TABLE public.cameras (
    cameraID		serial NOT NULL PRIMARY KEY,
    name		varchar(256) NOT NULL,
    description 	varchar(4096) NOT NULL,
    url 		varchar(256) NOT NULL
);

CREATE TABLE public.user (
    userID		serial NOT NULL PRIMARY KEY,	
    is_admin		boolean NOT NULL,
    name		varchar(256) NOT NULL,
    password		varchar(256) NOT NULL
);

CREATE TABLE public.images (
    cameraID	int References public.cameras(cameraID),
    date	timestamp(0) WITH TIME ZONE NOT NULL
);

CREATE TABLE public.privileges (
    userID	int References public.user(userID),
    cameraID	int References public.cameras(cameraID)  
);

INSERT INTO public.user
(is_admin, name, password) values (true, 'Klaus Schröder', '123');
INSERT INTO public.user
(is_admin, name, password) values (false, 'Sven Müller', '321');
INSERT INTO public.user
(is_admin, name, password) values (false, 'Christina Brandt', '111');
INSERT INTO public.user
(is_admin, name, password) values (false, 'Olaf Peterson', '222');
INSERT INTO public.user
(is_admin, name, password) values (true, 'Astrid Groß', '333');

INSERT INTO public.cameras
(name, description, url) values ('Mannheim', 'Webcam mit Blick auf den Wasserturm', 'https://www.mvv-energie.de/webcam_maritim/MA-Wasserturm.jpg');
INSERT INTO public.cameras
(name, description, url) values ('Hamburg', 'Webcam der "Deutsche Afrika-Linien John T. Essberger"', 'http://my.dal.biz/cgi-bin/webcam/getpics.cgi?Cam=east');
INSERT INTO public.cameras
(name, description, url) values ('Hamburg', 'Webcam der MPC Münchmeyer Petersen & Co. GmbH', 'http://www.mpc-it.de/webcam/big.jpg');
INSERT INTO public.cameras
(name, description, url) values ('Ludwigshafen am Bodensee', 'Webcam mit Blick auf den Bodensee vom Zollhaus von Ludwigshafen nach Bodman', 'http://www.die-ersten-am-see.de/webcam/camluzo.jpg');
INSERT INTO public.cameras
(name, description, url) values ('Füssen', 'Webcam mit Blick in die Fussgängerzone (Reichenstrasse)', 'http://www.webcamfuessen.de/webcam/webcamfuessen.jpg');

INSERT INTO privileges
(userid, cameraid) VALUES (1, 1);
INSERT INTO privileges
(userid, cameraid) VALUES (1, 2);
INSERT INTO privileges
(userid, cameraid) VALUES (1, 3);
INSERT INTO privileges
(userid, cameraid) VALUES (1, 4);
INSERT INTO privileges
(userid, cameraid) VALUES (2, 5);
INSERT INTO privileges
(userid, cameraid) VALUES (2, 3);
INSERT INTO privileges
(userid, cameraid) VALUES (3, 1);

