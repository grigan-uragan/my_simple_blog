delete from message;

insert into message (id, text, tag, user_id) values
(1, 'first message', 'my-tag', 1),
(2, 'second message', 'my', 1),
(3, 'third message', 'my-tag', 1),
(4, 'fourth message', 'tag', 1);

alter sequence hibernate_sequence restart with 10;