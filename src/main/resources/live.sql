create table live
(
    id            int auto_increment
        primary key,
    EXTINF        smallint     default -1        not null,
    `group-title` varchar(255) default 'Default' null comment '直播源分组名称',
    `tvg-id`      varchar(255)                   not null comment '直播源id',
    `tvg-name`    varchar(255)                   null comment '名称',
    `tvg-logo`    varchar(255)                   not null comment '直播源logo',
    title         varchar(255)                   not null comment '直播源名',
    constraint live_unique
        unique (`tvg-id`)
)
    comment '直播源表';

INSERT INTO tvbox.live (id, EXTINF, `group-title`, `tvg-id`, `tvg-name`, `tvg-logo`, title) VALUES (1, -1, '央视台', 'CCTV-1综合', 'CCTV-1综合', 'https://live.fanmingming.com/tv/CCTV1.png', 'CCTV-1综合');
