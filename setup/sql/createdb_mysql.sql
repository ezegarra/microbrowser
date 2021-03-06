ALTER TABLE VISUAL_THREADS DROP PRIMARY KEY;

DROP VIEW VISUAL_CLASSIFIED_THREAD;

DROP TABLE VISUAL_THREADS;

DROP TABLE VISUAL_SIMILARITIES;

DROP TABLE VISUAL_SVD_THREADS;

DROP TABLE VISUAL_CLASSIFICATION;

DROP TABLE VISUAL_TERMS;

DROP TABLE VISUAL_PATTERNS;

DROP TABLE VISUAL_PATTERN_2_PATTERN;

DROP TABLE VISUAL_PATTERN_2_POST;

DROP TABLE VISUAL_USERS;

--  DROP SCHEMA VISUAL RESTRICT;

--  CREATE SCHEMA VISUAL;

CREATE TABLE VISUAL_THREADS (
		ID INTEGER NOT NULL,
		POSTTYPEID INTEGER,
		ACCEPTEDANSWERID INTEGER,
		PARENTID INTEGER,
		CREATIONDATE VARCHAR(100),
		SCORE INTEGER,
		VIEWCOUNT INTEGER,
--		BODY CLOB(2147483647),
        BODY TEXT,
		OWNERUSERID INTEGER,
		OWNERDISPLAYNAME VARCHAR(40),
		LASTEDITORUSERID INTEGER,
		LASTEDITORDISPLAYNAME VARCHAR(40),
		LASTEDITDATE VARCHAR(100),
		LASTACTIVITYDATE VARCHAR(100),
		TITLE VARCHAR(250),
		TAGS VARCHAR(150),
		ANSWERCOUNT INTEGER,
		COMMENTCOUNT INTEGER,
		FAVORITECOUNT INTEGER,
		CLOSEDDATE VARCHAR(100),
		COMMUNITYOWNEDDATE VARCHAR(100)
	);

CREATE TABLE VISUAL_SIMILARITIES (
		THREADID INTEGER,
		THREADID2 INTEGER,
		SIMILARITY DOUBLE
	);

CREATE TABLE VISUAL_SVD_THREADS (
		THREADID INTEGER,
		X DOUBLE,
		Y DOUBLE
	);

CREATE TABLE VISUAL_CLASSIFICATION (
		THREADID INTEGER,
		CLASSIFICATION INTEGER DEFAULT 0
	);

CREATE TABLE VISUAL_TERMS (
		THREADID INTEGER,
		TERM VARCHAR(350),
		OCCURRENCES INTEGER,
		TERMSTRENGTH INTEGER
	);
	

CREATE TABLE VISUAL_PATTERNS (
    ID                  INTEGER NOT NULL,
	PATTERNNAME			VARCHAR(350) NOT NULL,
	PATTERNDESCRIPTION 	VARCHAR(350),
	PATTERNSOLUTION 	VARCHAR(350),
	CREATIONDATE		TIMESTAMP,
	OWNERUSERID			INTEGER,
	LASTEDITORUSERID	INTEGER,
	LASTACTIVITYDATE	TIMESTAMP,
	TAGS				VARCHAR(100)
);

CREATE TABLE VISUAL_PATTERN_2_PATTERN (
	PATTERNNAMESOURCE	VARCHAR(350),
	PATTERNNAMETARGET	VARCHAR(350)
);

CREATE TABLE VISUAL_PATTERN_2_POST (
	PATTERNNAME		VARCHAR(350),
	POSTID			INTEGER,	-- THE ID OF THE ANSWER OR COMMENT
	POSTPARENTID	INTEGER	-- THE ID OF THE QUESTION
);

CREATE TABLE VISUAL_USERS (
    ID          INTEGER NOT NULL,
    DISPLAYNAME VARCHAR(40),   -- THE NAME OF THE USER
    REPUTATION  INTEGER    -- THE POINTS OF THE USER
);

CREATE VIEW VISUAL_CLASSIFIED_THREAD (ID, POSTTYPEID, ACCEPTEDANSWERID, PARENTID, CREATIONDATE, SCORE, VIEWCOUNT, BODY, OWNERUSERID, OWNERDISPLAYNAME, LASTEDITORUSERID, LASTEDITORDISPLAYNAME, LASTEDITDATE, LASTACTIVITYDATE, TITLE, TAGS, ANSWERCOUNT, COMMENTCOUNT, FAVORITECOUNT, CLOSEDDATE, COMMUNITYOWNEDDATE, CLASSIFICATION) AS
(
	SELECT T.*, COALESCE(C.CLASSIFICATION, 0) CLASSIFICATION 
	FROM VISUAL_THREADS T LEFT JOIN VISUAL_CLASSIFICATION C ON T.ID = C.THREADID);

ALTER TABLE VISUAL_THREADS ADD CONSTRAINT PK_VISUAL_THREADS PRIMARY KEY (ID);