WITH t1 AS
(
       SELECT c_uid,
              c_sendmsg_time
       FROM   mfw_dwd.fact_flow_server_im_c_sendmsg_increment
       WHERE  dt = '20190114'
       AND    c_uid = 17721808 ),
     t2 AS
(
       SELECT c_uid,
              dialogue_start_time,
              dialogue_resolve_time,
              is_click
       FROM   mfw_dwd.fact_flow_server_im_all_resolve_increment
       WHERE  dt = '20190114'
       AND    c_uid = 17721808 )
SELECT     t1.c_uid,
           t1.c_sendmsg_time,
           d.dialogue_start_time,
           d.dialogue_resolve_time,
           d.is_click
FROM       (
                  SELECT t1.c_uid,
                         t1.c_sendmsg_time,
                         t2.dialogue_start_time,
                         t2.dialogue_resolve_time,
                         t2.is_click
                  FROM   t1
                  JOIN   t2
                  where  t1.c_uid = t2.c_uid
                  AND    t1.c_sendmsg_time > t2.dialogue_start_time
                  AND    t1.c_sendmsg_time <= t2.dialogue_resolve_time ) d
RIGHT JOIN t1
ON         d.c_uid = t1.c_uid
AND        d.c_sendmsg_time = t1.c_sendmsg_time