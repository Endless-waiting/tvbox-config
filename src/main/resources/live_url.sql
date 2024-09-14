create table live_url
(
    id      int auto_increment
        primary key,
    url     varchar(511)  null,
    `group` int           null comment '归属于哪个源',
    count   int default 0 null comment '统计不能ping通的次数，超过一定次数即认为不可用，删除',
    constraint live_url_live_id_fk
        foreign key (`group`) references live (id)
            on update cascade
);

INSERT INTO tvbox.live_url (id, url, `group`, count) VALUES (1, 'http://121.18.43.254:10000/hls/1/index.m3u8', 1, 0);
