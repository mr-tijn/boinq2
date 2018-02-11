SELECT `t_nodetemplate`.* FROM `t_nodetemplate`, `t_edgetemplate`, `t_graphtemplate`, `t_track`, `t_datasource` WHERE `t_track`.`datasource_id` = `t_datasource`.`id` AND `t_datasource`.`name` = 'DBNAME' AND `t_track`.`graphtemplate_id` = `t_graphtemplate`.`id` AND `t_edgetemplate`.`graphtemplate_id` = `t_graphtemplate`.`id` AND (`t_nodetemplate`.`id` = `t_edgetemplate`.`from_nodetemplate_id` OR `t_nodetemplate`.`id` = `t_edgetemplate`.`to_nodetemplate_id`);
