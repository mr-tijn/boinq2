SELECT `t_track`.* FROM `t_track`,`t_datasource` WHERE `t_track`.`datasource_id` = `t_datasource`.`id` AND `t_datasource`.`name` = 'DBNAME';
