-- Users
INSERT INTO `user` (`email`, `password_hash`, `first_name`, `surname`) VALUES
('john.doe@example.com', 'hashedpassword123', 'John', 'Doe'),
('jane.smith@example.com', 'hashedpassword456', 'Jane', 'Smith'),
('alice.jones@example.com', 'hashedpassword789', 'Alice', 'Jones'),
('bob.brown@example.com', 'hashedpassword012', 'Bob', 'Brown');

-- Quests
INSERT INTO `quest` (`title`, `description`, `long_description`, `complete_description`) VALUES
('Burns\' Footsteps', 'Embrace the legacy of Robert Burns, in the streets that first put his words to page.', 'Follow in the footsteps of Robert Burns through Kilmarnock, where his literary journey began. This quest leads you to the places that shaped Scotland’s national poet— from the monument honoring his legacy to the very printer that first brought his words to life. Discover the town that shaped Burns and the stories behind his first published works.', 'TODO: Completed quest description for Burns\' Footsteps'),
('The Spirit of Kilmarnock', 'Explore the journey of Kilmarnock\'s most famous export.', 'Explore the origins of Johnnie Walker Whiskey in the heart of Kilmarnock. From a small grocery store to a global whisky icon, this quest takes you through some of the key sites that shaped the brand’s history. Walk the same streets where John Walker started it all, and see how a local blend became a worldwide symbol of quality, lasting to this day.', 'TODO: Completed quest description for The Spirit of Kilmarnock'),
('Victory at Loudoun Hill', 'Relive Robert the Bruce''s triumphant stand against English forces at Loudoun Hill, a defining moment in Scotland''s fight for independence.', 'Step onto the historic grounds of Loudoun Hill, where in 1307 Robert the Bruce led a daring and strategic battle that changed the course of Scottish history. Follow the path of the Scottish forces as they outmaneuvered a larger English army using the landscape to their advantage. Experience the sites where bravery and clever tactics secured a crucial victory, inspiring a nation in its quest for freedom.', 'TODO: Completed quest description for TheVictory at Loudon Hill');

-- Objectives
INSERT INTO `objective` (`latitude`, `longitude`, `name`, `title`, `description`, `image_url`) VALUES
(55.612847873, -4.489739516, 'Burns Monument', 'From monument to manuscript', 
'Our journey begins at the Burns Monument in Kay Park, a tribute to the legacy of Scotland''s beloved poet. His exploration of universal themes, such as love and friendship, transcend time and penetrate minds across the globe, even today. This serene setting marks the start of a trail through Rabbie Burns'' life in Kilmarnock. As you journey though his footsteps, reflect on the grand and ever-lasting appreciation for this man.', 
''),
(55.609740453, -4.497233736, 'New Laigh Kirk', 'Pews, Poems & Prayers',
'Standing before the New Laigh Kirk, we can consider the Burns\'s spiritual and his connections within this community. This church bore witness to much of the esteemed poet''s personal and public life.', 
''),
(55.610064199, -4.496429122, 'Burns & Wilson Monument', 'Partnership in Print', 
'We''ve now arrived at a celebration of the connection that first brought to life the mind of Rabbie Burns. TODO: write more?', 
''),
(55.610839293, -4.495649354, 'Waterloo Street Printers', 'Pressing the Past', 
'It was here, within the Waterloo Street Printers, where Burns approached John Wilson to print his first book of poems, now dubbed the "Kilmarnock Edition". Burns'' many friends had chipped in to finance this publication and found it to be greatly successful, selling all 612 printed copies within a month. This significant point in Scottish literary history brought forth treasured poems such as "To a Louse" and the "Twa Dugs".',
''); 

-- Artefacts
INSERT INTO `artefact` (`name`, `description`, `quest_id`, `image_url`) VALUES
('Kilmarnock Edition', 'The sell-out book that carried the first printed words of Rabbie Burns.', 1, ''),
('1865 Old Highland Whisky', 'The first of many blends from Kilmarnock''s prized Whiskey brand.', 2, '');

-- Artefact User
INSERT INTO `artefact_user` (`artefact_id`, `user_id`) VALUES
(1, 1), -- User 1 collects Artefact 1
(2, 2); -- User 2 collects Artefact 2

-- Quest Objectives
INSERT INTO `quest_objective` (`quest_id`, `objective_id`, `sequence`) VALUES
(1, 1, 1), -- First objective for Quest 1
(1, 2, 2), -- Second objective for Quest 1
(1, 3, 3), -- Third objective for Quest 1
(1, 4, 4); -- Fourth objective for Quest 1

