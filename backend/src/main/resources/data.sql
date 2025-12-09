-- TRIPS (ID 1 & 2)
INSERT INTO trip (id, destination, start_date, end_date, total_budget)
VALUES (1, 'Japan - Tokyo & Kyoto', '2025-04-10', '2025-04-24', 4500.00);

INSERT INTO trip (id, destination, start_date, end_date, total_budget)
VALUES (2, 'London - Weekend', '2025-06-05', '2025-06-08', 850.00);


-- ACTIVITIES (Linked to Trip ID 1)
INSERT INTO activity (id, title, description, price, date, status, trip_id)
VALUES (10, 'Senso-ji Temple', 'Oldest temple in Tokyo.', 0.0, '2025-04-11 09:30:00', 'To Do', 1);

INSERT INTO activity (id, title, description, price, date, status, trip_id)
VALUES (11, 'Sushi Ginza', 'Reservation required.', 120.50, '2025-04-12 20:00:00', 'Booked', 1);


-- ACTIVITIES (Linked to Trip ID 2)
INSERT INTO activity (id, title, description, price, date, status, trip_id)
VALUES (20, 'British Museum', 'See the Rosetta Stone.', 0.0, '2025-06-06 14:00:00', 'To Do', 2);


-- RESET SEQUENCES
ALTER SEQUENCE trip_id_seq RESTART WITH 3;
ALTER SEQUENCE activity_id_seq RESTART WITH 21;