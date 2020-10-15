DROP TABLE IF EXISTS animals;

CREATE TABLE animals
(
    id      INT AUTO_INCREMENT,
    name    VARCHAR(20) NOT NULL,
    age     INT NOT NULL,
    weight  INT NOT NULL,
    PRIMARY KEY (id)
);