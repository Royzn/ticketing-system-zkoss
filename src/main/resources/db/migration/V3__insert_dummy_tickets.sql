-- Insert dummy tickets for testing dashboard
DO $$
DECLARE
    i INT;
    total_tickets INT := 150; -- ubah ini kalau mau 500 atau 1000 tiket
    status_names TEXT[] := ARRAY['OPEN', 'IN_PROGRESS', 'CLOSED'];
    priority_names TEXT[] := ARRAY['LOW', 'MEDIUM', 'HIGH'];
    requester_names TEXT[] := ARRAY['johndoe', 'admin', 'agent'];
BEGIN
    FOR i IN 1..total_tickets LOOP
        INSERT INTO tickets (
            title, description, status_id, priority_id, assigned_to_id, requester, createddate
        )
        VALUES (
            'Dummy Ticket #' || i,
            'This is a dummy description for ticket #' || i,
            (SELECT id FROM statuses WHERE name = status_names[(i % array_length(status_names,1)) + 1]),
            (SELECT id FROM priority WHERE name = priority_names[(i % array_length(priority_names,1)) + 1]),
            (SELECT id FROM users WHERE username = requester_names[(i % array_length(requester_names,1)) + 1]),
            requester_names[(i % array_length(requester_names,1)) + 1],
            NOW() - (i || ' hours')::interval
        );
    END LOOP;
END $$;
