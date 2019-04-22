-- 用户信息
create table if not exists `user_info` (
  `user_id` bigint,
  `name` varchar(20),
  `birthday` date,
  `sex` enum ('0001-1','0001-2','0001-3') not null default '0001-3',
  `phone` varchar(16) not null ,
  `points` int not null default 0,
  `all_points` int not null default 0,
  `balance` decimal(10,2) not null default 0,
  primary key (`user_id`)
);

create table if not exists `admin_user` (
  `user_id` bigint,
  `login_id` varchar(20),
  `name` varchar(20),
  `phone` varchar(16) not null ,
  `role_id` bigint ,
  `local_password` varchar(32),
  `password_suffix` varchar(3) ,
  `birthday` date,
  `sex` enum ('0001-1','0001-2','0001-3') not null default '0001-3',
  `token` varchar(32),
  `enable` boolean not null default true ,
  primary key (`user_id`),
  unique key (`login_id`)
);

create table if not exists `user_auth` (
  `user_id` bigint,
  `phone` varchar(16) not null ,
  `local_password` varchar(32),
  `password_suffix` varchar(6) ,
  `token` varchar(32),
  `we_chat_open_id` varchar(64) not null,
  `we_chat_session_key` varchar(32) not null,
  primary key (`user_id`),
  unique key (`phone`),
  unique index (`token`),
  unique key (`we_chat_open_id`)
);


create table if not exists `order`(
  `order_id` bigint,
  `user_id` bigint not null,
  `coupon_id` varchar(32),
  `status` enum('0002-1','0002-2','0002-3','0002-4') not null default '0002-1',
  `original_money` decimal(10,2) not null ,
  `need_pay_money` decimal(10,2) not null ,
  `final_pay_money` decimal(10,2) not null ,
  `prepay_money` decimal(10,2),
  `order_item` json,
  `order_remark` varchar(64),
  `customer_count` smallint unsigned not null default 1,
  `create_date` datetime not null default now(),
  `cancel_id` bigint ,
  primary key (`order_id`)
);

create table if not exists `order_cancel`(
  `cancel_id` bigint,
  `user_id` bigint not null,
  `cancel_date` datetime not null default now(),
  `cancel_reason` enum('0009-1','0009-2','0009-99') not null ,
  `cancel_remark` varchar(64),
  primary key (`cancel_id`)
);

create table if not exists `coupon`(
  `coupon_id` bigint,
  -- 优惠券主人
  `author_user_id` bigint,
  -- 优惠券类型（折扣/满减）
  `type` enum('0004-1','0004-2') not null ,
  -- 使用状态
  `status` enum('0005-1','0005-2','0005-3','0005-4') not null default '0005-1',
  -- 折扣值
  `discount` decimal(3,1),
  -- 满 M 减 N -> N
  `offset_money` decimal(10,2),
  -- 满 M 减 N -> M
  `require_money` decimal(10,2),
  -- 创建人
  `create_user_id` bigint not null ,
  -- 发放人
  `distribute_user_id` bigint not null ,
  -- 开始时间
  `start_date` date not null ,
  -- 到期时间
  `expire_date` date not null ,
  primary key (`coupon_id`)
);

create table if not exists `pay_record`(
  `pay_record_id` bigint,
  `user_id` bigint not null ,
  `order_id` bigint,
  `pay_money` decimal(10,2),
  `type` varchar(6),
  primary key (`pay_record_id`)
);

create table if not exists `my_menu`(
  `user_id` bigint,
  `index` int,
  `menu_name` varchar(16) not null,
  `create_date` datetime not null default now(),
  `last_update_date` datetime not null default now(),
  `goods_info` json,
  primary key (`user_id`,`index`)
);


create table if not exists `goods`(
  `goods_id` int auto_increment ,
  `name` varchar(32) not null ,
  `description` varchar(32) ,
  `img` text not null ,
  `price` decimal(8,2) not null ,
  `category_id` int not null ,
  `enableAmount` boolean not null default false ,
  `amount` int unsigned not null default 0,
  `enableDiscount` boolean not null default false ,
  `discount` decimal(3,2),
  `enable` boolean not null default true ,
  `version` bigint not null default 0,
  primary key (`goods_id`)
) auto_increment = 1;


create table if not exists `goods_category` (
  `category_id` int auto_increment ,
  `name` varchar(32) not null ,
  `description` varchar(128),
  primary key (`category_id`)
) auto_increment = 1;



create table if not exists `code_config`(
  `code` varchar(10),
  `name` varchar(64),
  `description` varchar(512),
  primary key (`code`)
);

insert into `code_config` (`code`,`name`,`description`) values ('0001-1','男','性别');
insert into `code_config` (`code`,`name`,`description`) values ('0001-2','女','性别');
insert into `code_config` (`code`,`name`,`description`) values ('0001-9','未知','性别');
insert into `code_config` (`code`,`name`,`description`) values ('0002-1','已提交','已经提交到后台，但是未支付');
insert into `code_config` (`code`,`name`,`description`) values ('0002-2','已支付','已经支付');
insert into `code_config` (`code`,`name`,`description`) values ('0002-3','已完成','已完成');
insert into `code_config` (`code`,`name`,`description`) values ('0002-4','取消中','订单正在取消中');
insert into `code_config` (`code`,`name`,`description`) values ('0002-5','已取消','订单已被取消');
insert into `code_config` (`code`,`name`,`description`) values ('0002-6','已关闭','订单超时自动关闭');
insert into `code_config` (`code`,`name`,`description`) values ('0003-1','支付宝','支付方式');
insert into `code_config` (`code`,`name`,`description`) values ('0003-2','微信支付','支付方式');
insert into `code_config` (`code`,`name`,`description`) values ('0003-3','现金','支付方式');
insert into `code_config` (`code`,`name`,`description`) values ('0004-1','抵用券','优惠类型');
insert into `code_config` (`code`,`name`,`description`) values ('0004-2','折扣券','优惠类型');
insert into `code_config` (`code`,`name`,`description`) values ('0005-1','未分配','优惠券状态');
insert into `code_config` (`code`,`name`,`description`) values ('0005-2','未使用','优惠券状态');
insert into `code_config` (`code`,`name`,`description`) values ('0005-3','已使用','优惠券状态');
insert into `code_config` (`code`,`name`,`description`) values ('0005-4','已禁用','优惠券状态');
insert into `code_config` (`code`,`name`,`description`) values ('0006-1','顾客','用户类型');
insert into `code_config` (`code`,`name`,`description`) values ('0006-2','管理人员','用户类型');
insert into `code_config` (`code`,`name`,`description`) values ('0007-1','微信登录','登录类型');
insert into `code_config` (`code`,`name`,`description`) values ('0007-2','web后台登录','登录类型');
insert into `code_config` (`code`,`name`,`description`) values ('0008-1','订单支付','登录类型');
insert into `code_config` (`code`,`name`,`description`) values ('0008-2','充值支付','登录类型');
insert into `code_config` (`code`,`name`,`description`) values ('0009-1','临时有事','取消原因');
insert into `code_config` (`code`,`name`,`description`) values ('0009-2','排队时间过长','取消原因');
insert into `code_config` (`code`,`name`,`description`) values ('0009-99','其他','取消原因');

create table if not exists `table_info`(
  `table_id` int auto_increment,
  `count` smallint unsigned not null ,
  `enable` boolean not null default true ,
  primary key (`table_id`)
);


create table if not exists `menu_bar`(
  `menu_bar_id` bigint,
  `name` varchar(8) not null ,
  `description` varchar(128),
  `url` varchar(128),
  `order` int ,
  `parent` bigint,
  `enable` boolean not null default true ,
  primary key (`menu_bar_id`)
);

create table if not exists `role`(
  `role_id` int,
  `name` varchar(8) not null ,
  `description` varchar(128),
  `order` int ,
  `enable` boolean not null default true ,
  primary key (`role_id`)
);

create table if not exists `menu_bar_for_role`(
  `role_id` bigint ,
  `menu_bar_id` bigint ,
  primary key (`role_id`,`menu_bar_id`)
);

create table if not exists `short_message`(
  `message_id` varchar(10),
  `received_user_id` varchar(64),
  `content` varchar(256),
  `send_date` datetime,
  primary key (`message_id`)
);

create table if not exists `short_message_wait`(
  `message_id` varchar(10),
  `received_user_id` varchar(64),
  `content` varchar(256),
  `send_date` datetime,
  `join_date` datetime,
  primary key (`message_id`)
);

# create table if not exists `operate_log`(
#   `user_id` bigint,
#   `type` varchar(10),
#   `content` varchar(256),
#   `operate_date` datetime
# );
create table if not exists `tables`(
  `table_id` int,
  `max_customer` int,
  primary key (`table_id`)
);

-- goods_id 取模分表
create table if not exists `sale_comment_0`(
  `goods_id` bigint,
  `order_id` bigint,
  `user_id` bigint not null ,
  `comment` text,
  `comment_date` date,
  `review` text,
  `review_date` date,
  `score` int not null,
  primary key (`goods_id`,`order_id`)
);

create table if not exists `sale_situation_detail`(
  `goods_id` int,
  `date` date,
  `amount` int default 0 not null ,
  `score_5` int default 0 not null ,
  `score_4` int default 0 not null ,
  `score_3` int default 0 not null ,
  `score_2` int default 0 not null ,
  `score_1` int default 0 not null ,
  primary key (`goods_id`,`date`)
);

create table if not exists `sale_situation_summary`(
  `goods_id` int,
  `amount` int,
  `score_5` int,
  `score_4` int,
  `score_3` int,
  `score_2` int,
  `score_1` int,
  primary key (`goods_id`)
);

create table if not exists `activity`(
  `group_id` int not null ,
  `xh` int  not null ,
  `description` text not null ,
  `start_date` datetime,
  `end_date` datetime,
  `text` text,
  `type` varchar(7),
  `status` boolean not null default true ,
  `update_date` datetime not null default now(),
  primary key (group_id,xh)
);

insert activity (group_id, xh, description, text, type, status)
values (1,1,'下单完成送积分','#MONEY# * 10','',true);

insert activity (group_id, xh, description, text, type, status)
values (1,2,'在线支付送积分','#MONEY# * 5','',true);

-- 消费满50元，产生满100或满200 减10-50的随机优惠券 或 9折券，5天后可以使用，有效期60天
insert activity (group_id, xh, description, text, type, status)
values (2,1,'在线支付送优惠券','(50)#(10-50|9)#(100|200)#(5,60)','',true);

-- 消费满50元，产生1-20元的随机返现
insert activity (group_id, xh, description, text, type, status)
values (3,1,'在线支付送红包','(50)#(1-20)','',true);

-- 消费满200元，赠送goods_id=23的商品1份
insert activity (group_id, xh, description, text, type, status)
values (3,1,'在线支付送红包','(200)#(23)#(1)','',true);
