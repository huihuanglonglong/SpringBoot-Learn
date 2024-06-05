# Mysql中根据多个字段匹配去重数据
delete from ioc_requirment_declare a
inner join ioc_requirment_declare b on a.id = b.id and a.dec_type = b.dec_type
where a.create_time < b.create_time;



# PostgreSql中根据多个字段匹配去重数据
delete from ioc_requirment_declare a
 using (
  select id, dec_type, max(create_time) maxCreateTime
    from ioc_requirment_declare
  group by id, dec_type having (count(id)) > 1) b on a.id = b.id and a.dec_type = b.dec_type
where a.create_time < b.maxCreateTime
