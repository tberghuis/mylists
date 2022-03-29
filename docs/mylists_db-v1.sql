# android studio - app inspection - export as file

PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE android_metadata (locale TEXT);
INSERT INTO android_metadata VALUES('en_AU');
CREATE TABLE `mylist` (`mylist_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mylist_text` TEXT NOT NULL, `myitem_draft_text` TEXT NOT NULL);
INSERT INTO mylist VALUES(1,'jsjjfj','');
INSERT INTO mylist VALUES(2,'idjjjf','');
INSERT INTO mylist VALUES(3,'bbvc','');
INSERT INTO mylist VALUES(4,'bbvg','');
CREATE TABLE `myitem` (`myitem_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mylist_id` INTEGER NOT NULL, `myitem_text` TEXT NOT NULL, FOREIGN KEY(`mylist_id`) REFERENCES `mylist`(`mylist_id`) ON UPDATE CASCADE ON DELETE CASCADE );
INSERT INTO myitem VALUES(1,1,'jxjj');
INSERT INTO myitem VALUES(3,1,'gffff');
CREATE TABLE room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT);
INSERT INTO room_master_table VALUES(42,'e4964b71166b6f964375fe94ce2ece72');
DELETE FROM sqlite_sequence;
INSERT INTO sqlite_sequence VALUES('mylist',4);
INSERT INTO sqlite_sequence VALUES('myitem',3);
CREATE INDEX `index_myitem_mylist_id` ON `myitem` (`mylist_id`);
COMMIT;