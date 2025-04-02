insert into
    users (email, password, nick, activ, activ_key)
values
    ('admin@admin.pl', '{noop}admin', 'admin', true, 'sdf435345jn'),   -- 1
    ('user@user.pl', '{noop}user', 'user', true, 'sfedfg87'); -- 2


insert into
    user_role (name, description)
values
    ('ADMIN', 'pełne uprawnienia'),   -- 1
    ('USER', 'podstawowe uprawnienia, możliwość oddawania głosów, komentowania, polubienia'),  -- 2
    ('BLOCKED', 'zablokowany użytkownik');


insert into
    user_roles (user_id, role_id)
values
    (1, 1),
    (2, 2);