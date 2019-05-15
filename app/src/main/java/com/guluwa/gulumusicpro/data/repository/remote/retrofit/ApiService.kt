package com.guluwa.gulumusicpro.data.repository.remote.retrofit

import com.guluwa.gulumusicpro.data.bean.remote.old.PlayListBean
import com.guluwa.gulumusicpro.data.bean.remote.old.SearchResultSongBean
import com.guluwa.gulumusicpro.data.bean.remote.old.SongPathBean
import com.guluwa.gulumusicpro.data.bean.remote.old.SongWordBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by guluwa on 2018/1/11.
 */

interface ApiService {

    /**
     * 网易云热门
     *
     * @param map
     * @return
     */
    @POST("api.php/{callback}")
    @Headers("Host: www.gequdaquan.net", "Referer:http://www.gequdaquan.net/gqss/", "Cookie:UM_distinctid=16a68167f871e4-05efe46a53827f-3b7a516b-384000-16a68167f8863; CNZZDATA1275011118=1759831749-1556523110-%7C1556523110; Hm_lvt_e941dfc465779f2553a65876b7d920fe=1556524466; Hm_lpvt_e941dfc465779f2553a65876b7d920fe=1556527192")
    @FormUrlEncoded
    fun obtainNetCloudHot(@Path("callback") callback: String, @FieldMap map: Map<String, String>): Observable<PlayListBean>

    /**
     * 网易云歌曲歌词
     *
     * @param callback
     * @param map
     * @return
     */
    @POST("api.php/{callback}")
    @Headers("Host: www.gequdaquan.net", "Referer:http://www.gequdaquan.net/gqss/", "Cookie:UM_distinctid=16a68167f871e4-05efe46a53827f-3b7a516b-384000-16a68167f8863; CNZZDATA1275011118=1759831749-1556523110-%7C1556523110; Hm_lvt_e941dfc465779f2553a65876b7d920fe=1556524466; Hm_lpvt_e941dfc465779f2553a65876b7d920fe=1556527192")
    @FormUrlEncoded
    fun obtainSongWord(@Path("callback") callback: String, @FieldMap map: Map<String, String>): Observable<SongWordBean>

    /**
     * 网易云歌曲下载地址
     *
     * @param callback
     * @param map
     * @return
     */
    @POST("api.php/{callback}")
    @Headers("Host: www.gequdaquan.net", "Referer:http://www.gequdaquan.net/gqss/", "Cookie:UM_distinctid=16a68167f871e4-05efe46a53827f-3b7a516b-384000-16a68167f8863; CNZZDATA1275011118=1759831749-1556523110-%7C1556523110; Hm_lvt_e941dfc465779f2553a65876b7d920fe=1556524466; Hm_lpvt_e941dfc465779f2553a65876b7d920fe=1556527192")
    @FormUrlEncoded
    fun obtainSongPath(@Path("callback") callback: String, @FieldMap map: Map<String, String>): Observable<SongPathBean>

    /**
     * 歌曲封面图地址
     *
     * @param callback
     * @param map
     * @return
     */
    @POST("api.php/{callback}")
    @Headers("Host: www.gequdaquan.net", "Referer:http://www.gequdaquan.net/gqss/", "Cookie:UM_distinctid=16a68167f871e4-05efe46a53827f-3b7a516b-384000-16a68167f8863; CNZZDATA1275011118=1759831749-1556523110-%7C1556523110; Hm_lvt_e941dfc465779f2553a65876b7d920fe=1556524466; Hm_lpvt_e941dfc465779f2553a65876b7d920fe=1556527192")
    @FormUrlEncoded
    fun obtainSongPic(@Path("callback") callback: String, @FieldMap map: Map<String, String>): Observable<List<SearchResultSongBean>>

    /**
     * 歌曲搜索
     *
     * @param callback
     * @param map
     * @return
     */
    @POST("api.php/{callback}")
    @Headers("Host: www.gequdaquan.net", "Referer:http://www.gequdaquan.net/gqss/", "Cookie:UM_distinctid=16a68167f871e4-05efe46a53827f-3b7a516b-384000-16a68167f8863; CNZZDATA1275011118=1759831749-1556523110-%7C1556523110; Hm_lvt_e941dfc465779f2553a65876b7d920fe=1556524466; Hm_lpvt_e941dfc465779f2553a65876b7d920fe=1556527192")
    @FormUrlEncoded
    fun searchSongByKeyWord(@Path("callback") callback: String, @FieldMap map: Map<String, String>): Observable<List<SearchResultSongBean>>


    /**
     * 歌曲文件下载
     *
     * @param fileUrl
     * @return
     */
    @Streaming //大文件时要加不然会OOM
    @GET
    fun downloadSongFile(@Url fileUrl: String): Observable<ResponseBody>
}
