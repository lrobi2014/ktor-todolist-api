-- Create Statements
CREATE TABLE lists(
    id   INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    user_id VARCHAR NOT NULL,
    date_created BIGINT NOT NULL
);

CREATE TYPE ItemStatus AS ENUM ('ACTIVE', 'DONE');
CREATE TABLE list_items(
    id   INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    list_id INTEGER NOT NULL,
    status ItemStatus NOT NULL DEFAULT 'ACTIVE',
    date_created BIGINT NOT NULL,
    CONSTRAINT fk_list
        FOREIGN KEY (list_id)
            REFERENCES lists(id) ON DELETE CASCADE
);

CREATE TABLE tags(
    id   INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    date_created BIGINT NOT NULL
);

CREATE TABLE tag_list(
    tag_id INTEGER NOT NULL,
    list_id INTEGER NOT NULL,
    date_created BIGINT NOT NULL
);

 Seed Statements
INSERT INTO lists (name, archived, user_id, date_created)
VALUES ('Grocery List 2022-02', false, 'laurenrobinson47', 1643976161),
       ('TODO: Monday, Feb 1 2022', false, 'laurenrobinson47', 1643976161),
       ('Packing List: Greece Trip', false, 'laurenrobinson47', 1643976161);

INSERT INTO list_items (name, list_id, status, date_created)
VALUES ('Toothpaste', 1, 'ACTIVE', 1643976161),
       ('Rice', 1, 'ACTIVE', 1643976161),
       ('Laundry Detergent', 1, 'ACTIVE', 1643976161),
       ('Spaghetti No. 3', 1, 'ACTIVE', 1643976161),
       ('Maple Syrup', 1, 'ACTIVE', 1643976161),
       ('Schedule apt. @ Dr Ross', 2, 'ACTIVE', 1643976161),
       ('Call Mom', 2, 'ACTIVE', 1643976161),
       ('Dinner w/ Karen', 2, 'ACTIVE', 1643976161),
       ('Swimsuit', 3, 'ACTIVE', 1643976161),
       ('Flip Flops', 3, 'ACTIVE', 1643976161),
       ('Sunblock', 3, 'ACTIVE', 1643976161),
       ('GoPro', 3, 'ACTIVE', 1643976161),
       ('Sunglasses', 3, 'ACTIVE', 1643976161),
       ('Beach Blankets', 3, 'ACTIVE', 1643976161);