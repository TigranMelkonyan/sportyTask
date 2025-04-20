INSERT INTO sporty_db.user (
    id, created_on, updated_on, active, status, email, password, role, loyalty_points, username
) VALUES
      (
          '5afd2943-5553-4900-abe8-e330e37fcfb2',
          '2021-09-11 00:00:00',
          '2021-09-11 00:00:00',
          1,
          'ACTIVE',
          'testuser1@gmail.com',
          '$2a$10$skqx48jk88e2M8h31a.BT.Yq/wik7eYuEJ5rZJ.3yFJSRpDf1Gzwm',
          'ADMIN',
          0,
          'admin'
      ),
      (
          'b1e87b57-2152-4f13-88f9-2ab3e8360da9',
          '2021-09-12 00:00:00',
          '2021-09-12 00:00:00',
          1,
          'ACTIVE',
          'testuser2@gmail.com',
          '$2a$10$EpdungYs8JrD0xCUORwmw.bOwyDH6TzhPto.DuEzNoYvsYkhpfMj6',
          'USER',
          0,
          'testUser'
      );
