-- Insert default roles
INSERT INTO roles (name, label) VALUES
  ('ROLE_ADMIN', 'Admin'),
  ('ROLE_USER', 'User'),
  ('ROLE_AGENT', 'Agent');

-- Admin User
INSERT INTO users (name, role_id, username, passwordhash, createddate)
VALUES (
  'Admin Name',
  (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'),
  'admin',
  '$2a$10$cREgtNqjDkg7O571ExXgwOO4cMuTGFlYZwDEmJjeW4gNi6dqHcDr6',
  NOW()
);

-- Agent User
INSERT INTO users (name, role_id, username, passwordhash, createddate)
VALUES (
  'Agent Smith',
  (SELECT id FROM roles WHERE name = 'ROLE_AGENT'),
  'agent',
  '$2a$10$cREgtNqjDkg7O571ExXgwOO4cMuTGFlYZwDEmJjeW4gNi6dqHcDr6',
  NOW()
);

-- Regular User
INSERT INTO users (name, role_id, username, passwordhash, createddate)
VALUES (
  'John Doe',
  (SELECT id FROM roles WHERE name = 'ROLE_USER'),
  'johndoe',
  '$2a$10$cREgtNqjDkg7O571ExXgwOO4cMuTGFlYZwDEmJjeW4gNi6dqHcDr6',
  NOW()
);

-- initial tickets
INSERT INTO tickets (
  title, description, status, priority, assigned_to_id, requester, createddate
)
VALUES (
  'Cannot login',
  'User reports that they cannot login to the system.',
  'OPEN',
  'HIGH',
  (SELECT id FROM users WHERE username = 'agent'),
  'johndoe',
  NOW()
);

INSERT INTO tickets (
  title, description, status, priority, assigned_to_id, requester, createddate
)
VALUES (
  'Feature request: Dark mode',
  'User requested dark mode for the dashboard UI.',
  'OPEN',
  'LOW',
  (SELECT id FROM users WHERE username = 'agent'),
  'johndoe',
  NOW()
);
