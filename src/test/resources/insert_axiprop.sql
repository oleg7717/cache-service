CREATE OR REPLACE TABLE axiprop (
	axipropid int8 NOT NULL,
	propname varchar(100) NOT NULL,
	description varchar(192) NOT NULL,
	axitype varchar(8) NOT NULL,
	globalonly numeric NOT NULL,
	instanceonly numeric NOT NULL,
	axiomadefault varchar(512) NULL,
	liverefresh numeric NOT NULL,
	encrypted numeric NOT NULL,
	domainid varchar(22) NULL,
	nullsallowed numeric NOT NULL,
	securelevel varchar(20) NOT NULL,
	userdefined numeric NOT NULL,
	onlinechanges numeric NOT NULL,
	changeby varchar(60) NOT NULL,
	changedate timestamp NOT NULL,
	masked numeric NOT NULL,
	accesstype int4 NULL,
	valuerules varchar(50) NULL,
	rowstamp varchar(40) NOT NULL
);

CREATE OR REPLACE TABLE axipropvalue (
	axipropvalueid int8 NOT NULL,
	propname varchar(100) NOT NULL,
	servername varchar(100) NOT NULL,
	serverhost varchar(100) NULL,
	propvalue varchar(512) NULL,
	encryptedvalue bytea NULL,
	changeby varchar(60) NOT NULL,
	changedate timestamp NOT NULL,
	accesstype int4 NULL,
	rowstamp varchar(40) NOT NULL
	--,CONSTRAINT axipropvalue_pkey PRIMARY KEY (axipropvalueid)
);

INSERT INTO axiprop (axipropid,propname,description,axitype,globalonly,instanceonly,axiomadefault,liverefresh,encrypted,domainid,nullsallowed,securelevel,userdefined,onlinechanges,changeby,changedate,masked,accesstype,valuerules,rowstamp) VALUES
	 (1,'axe.cache.scripts.startupLoad','Start load scripts into cache on system startup','YORN',0,0,'0',0,0,NULL,0,'SECURE',0,0,'AXIOMA','2024-03-04 12:00:00',0,2,NULL,'932313020'),
	 (2,'axe.cache.scripts.enabled','USe redis for script cache','YORN',0,0,'0',0,0,NULL,0,'SECURE',0,0,'AXIOMA','2024-03-04 12:00:00',0,2,NULL,'932313022'),
	 (3,'axe.cache.scripts.blocking','Should load script process be blocking or not','YORN',0,0,'1',0,0,NULL,0,'SECURE',0,0,'AXIOMA','2024-03-04 12:00:00',0,2,NULL,'932313024'),
	 (4,'axe.cache.redis','Redis URL for cache','ALN',0,0,'redis://127.0.0.1:6379',0,0,NULL,0,'SECURE',0,0,'AXIOMA','2024-03-04 12:00:00',0,2,NULL,'932313026'),
	 (5,'axe.cache.redisUser','Пользователь для подключения к Redis','ALN',0,0,'axioma',0,0,NULL,0,'SECURE',1,0,'AXADMIN','2024-03-21 12:59:25.085',0,2,NULL,'946237869'),
	 (7,'axe.cache.redisCluster','Кластерный Redis?','YORN',0,0,'0',0,0,NULL,0,'SECURE',1,0,'AXADMIN','2024-03-21 13:18:24.112',0,2,NULL,'946249962');
INSERT INTO axipropvalue (axipropvalueid,propname,servername,serverhost,propvalue,encryptedvalue,changeby,changedate,accesstype,rowstamp) VALUES
	 (1,'axe.cache.scripts.startupLoad','COMMON',NULL,'0',NULL,'AXIOMA','2024-03-04 12:00:00',0,'932313021'),
	 (2,'axe.cache.scripts.enabled','COMMON',NULL,'0',NULL,'AXIOMA','2024-03-04 12:00:00',0,'932313023'),
	 (3,'axe.cache.scripts.blocking','COMMON',NULL,'0',NULL,'AXIOMA','2024-03-04 12:00:00',0,'932313025'),
	 (4,'axe.cache.redis','COMMON',NULL,'0',NULL,'AXIOMA','2024-03-04 12:00:00',0,'932313027'),
	 (5,'axe.cache.redisUser','COMMON',NULL,'axioma',NULL,'AXADMIN','2024-03-21 12:59:25.127',2,'946237870'),
	 (7,'axe.cache.redisCluster','COMMON',NULL,'0',NULL,'AXADMIN','2024-03-21 13:02:04.058',2,'946239629');
