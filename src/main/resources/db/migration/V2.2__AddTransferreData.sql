
INSERT INTO wallet (id, user_id, balance, currency, last_updated) VALUES
  (1,  1, 250.00, 'EUR', '2018-09-17 18:47:52.69'),
  (2,  1, 3000.00, 'EUR', '2012-09-17 18:47:52.69'),
  (3,  2, 300.50, 'EUR', '2019-09-17 18:47:52.69');

INSERT INTO transaction (id, global_id, transaction_type, amount, wallet_id, description, last_updated) VALUES
  (1,  'fcbc3c23-e0ce-4a69-befe-3d711bd691bf', 'C', 50.0  , 1, 'entertainment', '2020-02-28 18:46:52.69'),
  (2,  '342eef66-41ec-42a9-bbf3-2fe66102d029', 'C', 100.0 , 1, 'entertainment', '2020-02-28 18:47:52.69'),
  (3,  '5f4bd715-e3af-430f-b272-7647973d3feb', 'C', 100.0 , 1, 'entertainment', '2020-02-28 18:48:52.69'),
  (4,  'fcbc3c23-e0ce-4a69-befe-3d711bd691b3', 'C', 1500.0, 2, 'entertainment', '2020-02-28 18:46:52.69'),
  (5,  '342eef66-41ec-42a9-bbf3-2fe66102d024', 'C', 1000.0, 2, 'entertainment', '2020-02-28 18:47:52.69'),
  (6,  '5f4bd715-e3af-430f-b272-7647973d3f34', 'C', 500.00, 2, 'entertainment', '2020-02-28 18:48:52.69'),
  (7,  '45bc3c23-e0ce-4a69-befe-3d711bd691x5', 'C', 110.00, 3, 'entertainment', '2020-02-28 18:46:52.69'),
  (8,  '772eef66-41ec-42a9-bbf3-2fe66102d024', 'C', 100.00, 3, 'entertainment', '2020-02-28 18:47:52.69'),
  (9,  '994bd715-e3af-430f-b272-7647973d3f34', 'C', 100.00, 3, 'entertainment', '2020-02-28 18:48:52.69');