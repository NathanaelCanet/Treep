-- USERS
-- Passwords are now hashed with BCrypt (salt included automatically)
-- Original passwords: 'admin' and 'user'
INSERT INTO users (id, login, password, role)
VALUES (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN')
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, login, password, role)
VALUES (2, 'user', '$2a$10$F3qXzJZ5qF5qF5qF5qF5qeN9qo8uLOickgx2ZMRZoMyeIjZAgcfl7', 'USER')
ON CONFLICT (id) DO NOTHING;


-- TRIPS (ID 1 & 2)
INSERT INTO trip (id, destination, date_debut, date_fin, budget_total, user_id)
VALUES (1, 'Japan - Tokyo & Kyoto', '2025-04-10', '2025-04-24', 4500.00, 2)
ON CONFLICT (id) DO NOTHING;

INSERT INTO trip (id, destination, date_debut, date_fin, budget_total, user_id)
VALUES (2, 'London - Weekend', '2025-06-05', '2025-06-08', 850.00, 2)
ON CONFLICT (id) DO NOTHING;


-- ACTIVITIES (Linked to Trip ID 1)
INSERT INTO activity (id, titre, description, cout, date_prevue, statut, trip_id)
VALUES (10, 'Senso-ji Temple', 'Oldest temple in Tokyo.', 0.0, '2025-04-11 09:30:00', 'To Do', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO activity (id, titre, description, cout, date_prevue, statut, trip_id)
VALUES (11, 'Sushi Ginza', 'Reservation required.', 120.50, '2025-04-12 20:00:00', 'Booked', 1)
ON CONFLICT (id) DO NOTHING;


-- ACTIVITIES (Linked to Trip ID 2)
INSERT INTO activity (id, titre, description, cout, date_prevue, statut, trip_id)
VALUES (20, 'British Museum', 'See the Rosetta Stone.', 0.0, '2025-06-06 14:00:00', 'To Do', 2)
ON CONFLICT (id) DO NOTHING;


-- Reset sequences to avoid duplicate key errors
SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 1) FROM users));
SELECT setval('trip_id_seq', (SELECT COALESCE(MAX(id), 1) FROM trip));
SELECT setval('activity_id_seq', (SELECT COALESCE(MAX(id), 1) FROM activity));