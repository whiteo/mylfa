INSERT INTO mylfa."user" (id, email, verified, password, modify_date, creation_date)
VALUES (gen_random_uuid(), 'demo@demo.demo', TRUE, 'demodemo', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO mylfa.currency_type (id, name, hide, creation_date, modify_date, user_id)
VALUES (gen_random_uuid(), 'dollar', FALSE, now(), now(),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo')),
       (gen_random_uuid(), 'euro', FALSE, now(), now(),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo')),
       (gen_random_uuid(), 'btc', TRUE, now(), now(),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'))
ON CONFLICT DO NOTHING;

INSERT INTO mylfa.income_category (id, name, hide, creation_date, modify_date, user_id)
VALUES (gen_random_uuid(), 'salary', FALSE, now(), now(),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'))
ON CONFLICT DO NOTHING;

INSERT INTO mylfa.income_category (id, name, hide, creation_date, modify_date, parent_id, user_id)
VALUES (gen_random_uuid(), 'bonus', FALSE, now(), now(),
        (SELECT id FROM mylfa.income_category WHERE name = 'salary'),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo')),
       (gen_random_uuid(), 'hide income category', TRUE, now(), now(),
        (SELECT id FROM mylfa.income_category WHERE name = 'salary'),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'))
ON CONFLICT DO NOTHING;

INSERT INTO mylfa.income (id, user_id, category_id, currency_type_id, amount, modify_date, creation_date, description)
VALUES (gen_random_uuid(), (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'),
        (SELECT id FROM mylfa.income_category WHERE name = 'salary'),
        (SELECT id FROM mylfa.currency_type WHERE name = 'dollar'),
        10, now(), now(), 'first salary'),
       (gen_random_uuid(), (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'),
        (SELECT id FROM mylfa.income_category WHERE name = 'bonus'),
        (SELECT id FROM mylfa.currency_type WHERE name = 'euro'),
        11, now(), now(), 'first bonus'),
       (gen_random_uuid(), (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'),
        (SELECT id FROM mylfa.income_category WHERE name = 'hide income category'),
        (SELECT id FROM mylfa.currency_type WHERE name = 'btc'),
        12, now(), now(), 'first hide income')
ON CONFLICT DO NOTHING;

INSERT INTO mylfa.expense_category (id, name, hide, creation_date, modify_date, user_id)
VALUES (gen_random_uuid(), 'market', FALSE, now(), now(),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'))
ON CONFLICT DO NOTHING;

INSERT INTO mylfa.expense_category (id, name, hide, creation_date, modify_date, parent_id, user_id)
VALUES (gen_random_uuid(), 'food', FALSE, now(), now(),
        (SELECT id FROM mylfa.expense_category WHERE name = 'market'),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo')),
       (gen_random_uuid(), 'household chemistry', FALSE, now(), now(),
        (SELECT id FROM mylfa.expense_category WHERE name = 'market'),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo')),
       (gen_random_uuid(), 'hide expense category', TRUE, now(), now(),
        (SELECT id FROM mylfa.expense_category WHERE name = 'market'),
        (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'))
ON CONFLICT DO NOTHING;

INSERT INTO mylfa.expense (id, user_id, category_id, currency_type_id, amount, description, modify_date, creation_date)
VALUES (gen_random_uuid(), (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'),
        (SELECT id FROM mylfa.expense_category WHERE name = 'food'),
        (SELECT id FROM mylfa.currency_type WHERE name = 'dollar'),
        20, 'first food', now(), now()),
       (gen_random_uuid(), (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'),
        (SELECT id FROM mylfa.expense_category WHERE name = 'household chemistry'),
        (SELECT id FROM mylfa.currency_type WHERE name = 'euro'),
        21, 'first chemistry', now(), now()),
       (gen_random_uuid(), (SELECT id FROM mylfa."user" WHERE email = 'demo@demo.demo'),
        (SELECT id FROM mylfa.expense_category WHERE name = 'hide expense category'),
        (SELECT id FROM mylfa.currency_type WHERE name = 'btc'),
        22, 'first hide expense', now(), now())
ON CONFLICT DO NOTHING;