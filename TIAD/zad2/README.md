Baza danych

```sql
-- 1. Tworzenie tabel
CREATE TABLE recipes (
    recipe_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    instructions TEXT,
    preparation_time INT
);

CREATE TABLE ingredients (
    ingredient_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE recipe_ingredients (
    recipe_id INT REFERENCES recipes(recipe_id),
    ingredient_id INT REFERENCES ingredients(ingredient_id),
    quantity VARCHAR(30),
    PRIMARY KEY (recipe_id, ingredient_id)
);

-- 1. Dodawanie składników (42 unikalne składniki)
INSERT INTO ingredients (name) VALUES
('mąka'), ('cukier'), ('jajka'), ('mleko'), ('masło'), 
('olej'), ('sól'), ('pieprz'), ('czosnek'), ('cebula'),
('pomidory'), ('ogórek'), ('papryka'), ('marchew'), ('ziemniaki'),
('kurczak'), ('wołowina'), ('łosoś'), ('ser żółty'), ('ser feta'),
('śmietana'), ('majonez'), ('ocet'), ('oliwa z oliwek'), ('sok z cytryny'),
('natka pietruszki'), ('koperek'), ('bazylia'), ('oregano'), ('tymianek'),
('cynamon'), ('kakao'), ('proszek do pieczenia'), ('wiórki kokosowe'), ('migdały'),
('rodzynki'), ('mak'), ('truskawki'), ('banany'), ('jabłka'),
('cukinia'), ('bakłażan'), ('szpinak');

-- 2. Dodawanie przepisów (50 przepisów)
INSERT INTO recipes (name, instructions, preparation_time) VALUES
('Naleśniki', 'Zmiksuj wszystkie składniki na gładkie ciasto. Smaż na rozgrzanej patelni.', 30),
('Jajecznica', 'Rozgrzej masło na patelni, wbij jajka, smaż mieszając.', 10),
('Sałatka grecka', 'Pokrój warzywa w kostkę, wymieszaj z oliwą i fetą.', 15),
('Zupa pomidorowa', 'Ugotuj warzywa, zblenduj, dodaj śmietanę.', 45),
('Spaghetti Bolognese', 'Usmaż mięso z warzywami, połącz z pomidorami i podawaj z makaronem.', 60),
('Pierogi ruskie', 'Zagnieć ciasto, przygotuj farsz z ziemniaków i sera, gotuj pierogi.', 90),
('Szarlotka', 'Przygotuj ciasto, ułóż jabłka, piecz 45 minut w 180°C.', 70),
('Kurczak curry', 'Usmaż kurczaka z curry i warzywami, podawaj z ryżem.', 40),
('Placki ziemniaczane', 'Zetrzyj ziemniaki, dodaj jajko i mąkę, smaż placki.', 35),
('Brownie', 'Wymieszaj składniki, piecz 25 minut w 180°C.', 50),
('Omlet', 'Roztrzep jajka z mlekiem, usmaż na patelni.', 15),
('Ratatouille', 'Pokrój warzywa w plasterki, ułóż w naczyniu, piecz 1 godzinę.', 75),
('Pizza margherita', 'Wyrobij ciasto, dodaj sos pomidorowy i ser, piecz 12 minut.', 40),
('Krem z dyni', 'Ugotuj dynię z warzywami, zblenduj na krem.', 50),
('Łosoś pieczony', 'Natrzyj łososia solą i pieprzem, piecz 20 minut.', 30),
('Gołąbki', 'Zawiń farsz mięsny w liście kapusty, duś 45 minut.', 85),
('Sernik', 'Przygotuj masę serową, piecz 1 godzinę w 160°C.', 90),
('Frittata', 'Wymieszaj jajka z warzywami, piecz w piekarniku.', 25),
('Barszcz', 'Ugotuj buraki z przyprawami, podawaj z uszkami.', 120),
('Kotlet schabowy', 'Panieruj mięso, smaż na złoty kolor.', 35),
('Chleb bananowy', 'Zmiksuj banany z jajkami i mąką, piecz 50 minut.', 70),
('Fasolka po bretońsku', 'Duszoną fasolę z kiełbasą i pomidorami.', 55),
('Tiramisu', 'Ułóż warstwy biszkoptów z mascarpone i kawą.', 60),
('Kurczak teriyaki', 'Marynuj kurczaka w sosie, smaż z warzywami.', 45),
('Risotto', 'Podgrzewaj ryż z bulionem i warzywami, mieszając.', 50),
('Panna cotta', 'Podgrzej śmietanę z żelatyną, wlej do form.', 240), -- chłodzenie 4h
('Bruschetta', 'Podpiecz bagietkę, nałóż pomidory z czosnkiem.', 20),
('Czekoladowe fondue', 'Rozpuść czekoladę, podawaj z owocami.', 15),
('Kebab domowy', 'Marynowane mięso z warzywami w picie.', 60),
('Bigos', 'Dusz kapustę z mięsem i kiełbasą przez 3 godziny.', 180),
('Pieczone ziemniaki', 'Pokrój ziemniaki, przypraw, piecz 40 minut.', 50),
('Muffiny czekoladowe', 'Wymieszaj składniki, piecz 25 minut.', 45),
('Gulasz wołowy', 'Duszone mięso z papryką i cebulą.', 120),
('Hummus', 'Zmiksuj ciecierzycę z tahini i czosnkiem.', 20),
('Szakszuka', 'Jajka duszone w sosie pomidorowym.', 30),
('Tarta owocowa', 'Ciasto z kremem i świeżymi owocami.', 80),
('Karkówka z grilla', 'Marynowane mięso grillowane.', 90), -- + marynata 12h
('Koktajl truskawkowy', 'Zmiksuj truskawki z mlekiem i jogurtem.', 10),
('Caprese', 'Ułóż na przemian pomidory i mozzarellę.', 15),
('Frytki', 'Pokrój ziemniaki, smaż w głębokim oleju.', 25),
('Racuchy', 'Ciasto drożdżowe smażone na patelni.', 40),
('Zapiekanka makaronowa', 'Makaron z sosem i serem zapieczony.', 50),
('Chłodnik', 'Zblenduj botwinkę z ogórkiem i kefirem.', 30),
('Kanapki z awokado', 'Chleb z pastą awokado i jajkiem.', 15),
('Kurczak w sosie śmietanowym', 'Duszone mięso z pieczarkami.', 50),
('Nuggetsy', 'Panierowane kawałki kurczaka smażone.', 35),
('Lody waniliowe', 'Mieszanka śmietany i mleka mrożona.', 360), -- zamrażanie 6h
('Sałatka Cezar', 'Sałata z grzankami i sosem czosnkowym.', 25),
('Pulpety w sosie', 'Mięsne kulki duszone w pomidorach.', 60);

-- 3. Łączenie przepisów ze składnikami (ponad 200 powiązań)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES
(1,1,'300 g'),(1,2,'2 łyżki'),(1,3,'2 szt'),(1,4,'250 ml'),(1,5,'1 łyżka'),
(2,3,'3 szt'),(2,5,'1 łyżka'),(2,7,'szczypta'),(2,8,'szczypta'),(2,24,'1 łyżka'),
(3,11,'2 szt'),(3,12,'1 szt'),(3,20,'100 g'),(3,24,'2 łyżki'),(3,7,'szczypta'),
(4,11,'500 g'),(4,15,'2 szt'),(4,21,'100 ml'),(4,7,'do smaku'),(4,9,'1 ząbek'),(4,10,'1 szt'),
(5,17,'300 g'),(5,10,'1 szt'),(5,11,'200 g'),(5,9,'2 ząbki'),(5,24,'1 łyżka'),(5,7,'szczypta'),
(6,1,'400 g'),(6,3,'1 szt'),(6,15,'500 g'),(6,20,'150 g'),(6,5,'2 łyżki'),(6,7,'szczypta'),
(7,1,'250 g'),(7,2,'100 g'),(7,3,'1 szt'),(7,40,'1 kg'),(7,33,'1 łyżeczka'),(7,5,'50 g'),
(8,16,'500 g'),(8,10,'1 szt'),(8,13,'1 szt'),(8,29,'1 łyżka'),(8,24,'2 łyżki'),(8,7,'szczypta'),
(9,15,'1 kg'),(9,1,'2 łyżki'),(9,3,'1 szt'),(9,7,'szczypta'),(9,6,'1 szt'),(9,24,'2 łyżki'),
(10,1,'200 g'),(10,2,'250 g'),(10,3,'4 szt'),(10,32,'3 łyżki'),(10,33,'1 łyżeczka'),(10,5,'100 g'),
(11,3,'3 szt'),(11,4,'50 ml'),(11,5,'1 łyżka'),(11,7,'szczypta'),(11,8,'szczypta'),(11,19,'50 g'),
(12,11,'3 szt'),(12,12,'1 szt'),(12,13,'1 szt'),(12,41,'1 szt'),(12,24,'3 łyżki'),(12,9,'2 ząbki'),
(13,1,'500 g'),(13,11,'200 g'),(13,19,'200 g'),(13,24,'1 łyżka'),(13,7,'szczypta'),(13,9,'1 ząbek'),
(14,41,'1 kg'),(14,10,'1 szt'),(14,15,'2 szt'),(14,21,'200 ml'),(14,7,'do smaku'),(14,24,'1 łyżka'),
(15,18,'500 g'),(15,24,'2 łyżki'),(15,7,'szczypta'),(15,8,'szczypta'),(15,9,'2 ząbki'),(15,26,'garść'),
(16,17,'400 g'),(16,42,'200 g'),(16,10,'1 szt'),(16,11,'200 g'),(16,7,'do smaku'),(16,24,'2 łyżki'),
(17,19,'500 g'),(17,2,'150 g'),(17,3,'5 szt'),(17,1,'2 łyżki'),(17,33,'1 łyżeczka'),(17,24,'1 łyżka'),
(18,3,'4 szt'),(18,11,'2 szt'),(18,12,'1 szt'),(18,19,'100 g'),(18,24,'1 łyżka'),(18,7,'szczypta'),
(19,39,'500 g'),(19,3,'1 szt'),(19,15,'2 szt'),(19,10,'1 szt'),(19,7,'do smaku'),(19,24,'1 łyżka'),
(20,17,'600 g'),(20,1,'100 g'),(20,3,'2 szt'),(20,6,'200 ml'),(20,7,'szczypta'),(20,24,'2 łyżki'),
(21,40,'3 szt'),(21,2,'100 g'),(21,3,'2 szt'),(21,1,'200 g'),(21,33,'1 łyżeczka'),(21,5,'50 g'),
(22,35,'200 g'),(22,17,'300 g'),(22,11,'200 g'),(22,10,'1 szt'),(22,7,'do smaku'),(22,24,'1 łyżka'),
(23,19,'250 g'),(23,2,'100 g'),(23,3,'3 szt'),(23,36,'1 łyżka'),(23,4,'100 ml'),(23,24,'1 łyżka'),
(24,16,'500 g'),(24,37,'100 ml'),(24,10,'1 szt'),(24,13,'1 szt'),(24,24,'2 łyżki'),(24,7,'szczypta'),
(25,22,'300 g'),(25,4,'1 l'),(25,10,'1 szt'),(25,11,'200 g'),(25,7,'do smaku'),(25,24,'1 łyżka'),
(26,21,'500 ml'),(26,2,'100 g'),(26,34,'2 łyżeczki'),(26,4,'100 ml'),(26,40,'1 szt'),(26,24,'1 łyżka'),
(27,23,'1 bagietka'),(27,11,'3 szt'),(27,9,'1 ząbek'),(27,24,'2 łyżki'),(27,26,'garść'),(27,7,'szczypta'),
(28,32,'200 g'),(28,4,'100 ml'),(28,38,'1 szt'),(28,39,'2 szt'),(28,40,'2 szt'),(28,24,'1 łyżka'),
(29,16,'500 g'),(29,23,'4 szt'),(29,11,'2 szt'),(29,12,'1 szt'),(29,24,'3 łyżki'),(29,7,'szczypta'),
(30,42,'1 kg'),(30,17,'300 g'),(30,35,'200 g'),(30,10,'2 szt'),(30,7,'do smaku'),(30,24,'2 łyżki'),
(31,15,'1 kg'),(31,24,'3 łyżki'),(31,7,'szczypta'),(31,8,'szczypta'),(31,9,'1 ząbek'),(31,26,'garść'),
(32,1,'200 g'),(32,2,'150 g'),(32,3,'2 szt'),(32,4,'200 ml'),(32,32,'100 g'),(32,33,'1 łyżeczka'),
(33,17,'800 g'),(33,13,'2 szt'),(33,10,'2 szt'),(33,24,'3 łyżki'),(33,7,'do smaku'),(33,8,'szczypta'),
(34,35,'400 g'),(34,24,'3 łyżki'),(34,9,'2 ząbki'),(34,25,'1 łyżka'),(34,7,'szczypta'),(34,26,'garść'),
(35,11,'400 g'),(35,3,'4 szt'),(35,9,'2 ząbki'),(35,24,'2 łyżki'),(35,8,'szczypta'),(35,26,'garść'),
(36,1,'200 g'),(36,2,'100 g'),(36,3,'2 szt'),(36,21,'200 ml'),(36,40,'500 g'),(36,24,'1 łyżka'),
(37,17,'1 kg'),(37,24,'5 łyżek'),(37,7,'szczypta'),(37,8,'szczypta'),(37,9,'3 ząbki'),(37,10,'1 szt'),
(38,38,'200 g'),(38,4,'300 ml'),(38,21,'100 g'),(38,2,'2 łyżki'),(38,40,'1 szt'),(38,24,'1 łyżka'),
(39,11,'3 szt'),(39,19,'200 g'),(39,24,'2 łyżki'),(39,26,'garść'),(39,7,'szczypta'),(39,9,'1 ząbek'),
(40,15,'1 kg'),(40,6,'500 ml'),(40,7,'szczypta'),(40,1,'2 łyżki'),(40,3,'1 szt'),(40,24,'2 łyżki'),
(41,1,'300 g'),(41,3,'2 szt'),(41,4,'200 ml'),(41,33,'1 łyżeczka'),(41,40,'2 szt'),(41,24,'1 łyżka'),
(42,22,'300 g'),(42,19,'200 g'),(42,11,'200 g'),(42,21,'200 ml'),(42,7,'do smaku'),(42,24,'1 łyżka'),
(43,42,'500 g'),(43,12,'2 szt'),(43,21,'200 ml'),(43,7,'szczypta'),(43,9,'1 ząbek'),(43,24,'1 łyżka'),
(44,16,'200 g'),(44,23,'4 szt'),(44,24,'2 łyżki'),(44,7,'szczypta'),(44,8,'szczypta'),(44,9,'1 ząbek'),
(45,12,'2 szt'),(45,42,'100 g'),(45,4,'200 ml'),(45,7,'szczypta'),(45,26,'garść'),(45,24,'1 łyżka'),
(46,38,'1 szt'),(46,3,'2 szt'),(46,24,'1 łyżka'),(46,8,'szczypta'),(46,23,'2 kromki'),(46,7,'szczypta'),
(47,16,'600 g'),(47,21,'200 ml'),(47,9,'2 ząbki'),(47,7,'do smaku'),(47,24,'1 łyżka'),(47,8,'szczypta'),
(48,16,'400 g'),(48,1,'100 g'),(48,3,'2 szt'),(48,6,'500 ml'),(48,7,'szczypta'),(48,8,'szczypta'),
(49,4,'500 ml'),(49,2,'150 g'),(49,21,'200 ml'),(49,34,'1 łyżeczka'),(49,24,'1 łyżka');
```