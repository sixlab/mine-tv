alter table mine_movie_info add vod_hide int;
update mine_movie_info set vod_hide = 0 where vod_hide is null;
