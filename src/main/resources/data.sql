DROP TABLE IF EXISTS books;

CREATE TABLE books
(
    id INT NOT NULL,
    title VARCHAR(50) NOT NULL,
    author VARCHAR(50) NOT NULL
);

INSERT INTO books (id, title, author) VALUES (1, 'Bridget Jones', 'Helen Fielding');
INSERT INTO books (id, title, author) VALUES (2, 'Maybe you should talk to someone', 'Lori Gottlieb');
INSERT INTO books (id, title, author) VALUES (3, '', '');