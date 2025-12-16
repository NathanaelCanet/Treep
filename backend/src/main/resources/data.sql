-- TRIPS (ID 1 & 2)
INSERT INTO trip (id, destination, date_debut, date_fin, budget_total)
VALUES (1, 'Japan - Tokyo & Kyoto', '2025-04-10', '2025-04-24', 4500.00)
ON CONFLICT (id) DO NOTHING;

INSERT INTO trip (id, destination, date_debut, date_fin, budget_total)
VALUES (2, 'London - Weekend', '2025-06-05', '2025-06-08', 850.00)
ON CONFLICT (id) DO NOTHING;


-- ACTIVITIES (Linked to Trip ID 1)
-- Note: On a retiré date_prevue des colonnes et des valeurs
INSERT INTO activity (id, titre, description, cout, statut, trip_id)
VALUES (10, 'Senso-ji Temple', 'Oldest temple in Tokyo.', 0.0, 'To Do', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO activity (id, titre, description, cout, statut, trip_id)
VALUES (11, 'Sushi Ginza', 'Reservation required.', 120.50, 'Booked', 1)
ON CONFLICT (id) DO NOTHING;


-- ACTIVITIES (Linked to Trip ID 2)
INSERT INTO activity (id, titre, description, cout, statut, trip_id)
VALUES (20, 'British Museum', 'See the Rosetta Stone.', 0.0, 'To Do', 2)
ON CONFLICT (id) DO NOTHING;

-- Reset sequences
ALTER SEQUENCE trip_id_seq RESTART WITH 3;
ALTER SEQUENCE activity_id_seq RESTART WITH 21;