// package tech.minesoft.minetv.dao;
//
// import java.util.List;
//
// import tech.minesoft.minetv.MineApplication;
// import tech.minesoft.minetv.bean.MovieInfo;
//
// public class MovieInfoDao {
//     /**
//      * 添加数据，如果有重复则覆盖
//      *
//      * @param MovieInfo
//      */
//     public static void insertData(MovieInfo MovieInfo) {
//         MineApplication.getDaoInstant().getMovieInfoDao().insertOrReplace(MovieInfo);
//     }
//
//     /**
//      * 删除数据 根据id删除
//      *
//      * @param id
//      */
//     public static void deleteData(long id) {
//         MineApplication.getDaoInstant().getMovieInfoDao().deleteByKey(id);
//     }
//
//     /**
//      * 删除所有数据
//      *
//      * @param id
//      */
//     public static void deleteTokenAll() {
//         MineApplication.getDaoInstant().getMovieInfoDao().deleteAll();
//     }
//
//     /**
//      * 更新数据
//      *
//      * @param MovieInfo
//      */
//     public static void updateData(MovieInfo MovieInfo) {
//         MineApplication.getDaoInstant().getMovieInfoDao().update(MovieInfo);
//     }
//
//     /**
//      * 查询全部数据
//      */
//     public static List<MovieInfo> queryAll() {
//         return MineApplication.getDaoInstant().getMovieInfoDao().loadAll();
//     }
//
//     /**
//      * 查询表中是否含某条数据(比如我们现在要看看有没存在一个条件为title的数据)
//      */
//     public static boolean queryTokenid(String title) {
//         List<MovieInfo> list = MineApplication.getDaoInstant().getMovieInfoDao().queryBuilder()
//                 .where(MovieInfoDao.Properties.Title.eq(title)).list();
//         if (list != null && list.size() != 0) {
//             return true;
//         }
//         return false;
//     }
// }
