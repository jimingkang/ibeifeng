

--统计每个sid 的信息
drop table tmp_pc_session_info_1 ;

--unix_timestamp('2015-11-11 12:11:01')  转成unix毫秒数 ，10位
create table tmp_pc_session_info_1 as
select sessionId,
       max(guid) guid,
       max(endUserId) user_id,
       count(url) pv,
       unix_timestamp(max(trackTime)) - unix_timestamp(min(trackTime)) stay_time,
       min(trackTime) min_trackTime  ,
       max(ip) ip,
       max(provinceId) provinceId
 from track_log where date='2015-08-28'
 group by sessionId ;



insert overwrite table dw.fct_session_info partition(ds='2015-08-28')
select a.sessionId,
       max(a.guid),
       max(b.trackerU),
       max(b.url) landing_url,
       max(b.referer) landing_url_ref,
       max(a.user_id),
       max(a.pv),
       max(a.stay_time)/1000,
       max(a.min_trackTime),
       max(a.ip),
       max(a.provinceId)
 from tmp_pc_session_info_1  a
 join
 (select distinct date,url , referer , trackTime,sessionId,trackerU
  from track_log where date='2015-08-28' ) b
  on a.sessionId = b.sessionId and a.min_trackTime=b.trackTime
  group by a.sessionId;


