DELETE FROM VISUAL.PATTERNS;
INSERT INTO VISUAL.PATTERNS VALUES(1,'Reference Provider','The user is trying to address a particular functionality and it is looking for references to services that can address the problems. ','Provide a reference to an external service or utility that can provide the needed functionality.','2014-02-26 17:49:47.616',1,1,'2014-02-26 17:49:47.616','');
INSERT INTO VISUAL.PATTERNS VALUES(2,'Trusted Advisor','The answer seeker is looking for advice regarding the recommended or appropriate way to implement a task or complete a set of instructions.  The answer seeker might have an answer or a set of answers and is looking for confirmation.','To answer these types of questions, the answerer should start by providing the recommended answer to address the problem or situation.  Then, the answered should provide some level of reasoning behind the benefits of the selected answer.','2014-02-26 17:50:10.389',1,1,'2014-02-26 17:50:10.389','');
INSERT INTO VISUAL.PATTERNS VALUES(3,'Educator','The answer seeker is looking for an explanation of a topic, error message or code.','The answerer should start by providing a brief description of the error followed by either a reference to where additional information can be found or by a longer explanation of the error.','2014-02-26 17:50:41.63',1,1,'2014-02-26 17:50:41.63','');
INSERT INTO VISUAL.PATTERNS VALUES(4,'Comparator','The answer seeker is trying to determine the difference between two or more topics.','The answerer should provide a comparison between the different options submitted  by the answer seeker.','2014-04-16 06:15:26.724',1,1,'2014-04-16 06:15:26.724','');

DELETE FROM VISUAL.PATTERN_2_PATTERN;
INSERT INTO VISUAL.PATTERN_2_PATTERN VALUES ('3', '4');

DELETE FROM VISUAL.PATTERN_2_POST;
INSERT INTO VISUAL.PATTERN_2_POST VALUES('1', 0, 27129);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('1', 0, 27065);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('1', 0, 123);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('2', 0, 65035);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('2', 0, 23106);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('3', 0, 47605);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('3 ', 0, 46898);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('3 ', 0, 50532);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('3 ', 0, 43344);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('4', 0, 564);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('4', 0, 31693);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('4', 0, 36347);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('4', 0, 40471);
INSERT INTO VISUAL.PATTERN_2_POST VALUES('4', 0, 11561);
