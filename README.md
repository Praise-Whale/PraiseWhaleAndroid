### 🐋   서비스 이름 : 칭찬할고래 🐋 

 ### 💙 서비스 소개 : 고래와 함께하는 칭찬 중독 강화 서비스 💙 

<br>

 🌀 최윤소 - `칭찬 미션`  ->First page , Dialog View

> **칭찬을 어떻게 할지 고민할 필요 없어요! 하루에 한 번씩 누구에게, 어떤 칭찬을 해줘야할지 칭찬 미션이 주어집니다**

 🌀 김희빈- `나의 칭찬 카드`  -> Second page, Recycler View

> **칭찬 미션 수행 시 나의 칭찬 카드가 차곡차곡 쌓여요! 언제, 누구에게, 어떤 칭찬을 했는지 한눈에 퀵하게 볼 수 있어요!**

 🌀 안나영- `나의 칭찬 고래` -> Third page, Splash View

> **나의 칭찬 고래의 상태를 통해 나의 칭찬 중독 지수를 확인할 수 있어요!**


<br>

> retrofit interface

```kotlin
interface RequestInterface {
    //최근 칭찬 유저 조회
    @Headers("Content-Type:application/json")
    @GET("users-praise")
    fun getUsersPraise(

    ) : Call<ResponseCollectionData>

    // 홈 화면 조회
    @GET("/home")
    fun getPraise(

    )  : Call<ResponseCollectionData>

    // 칭찬한 사람 추가
    @Headers("Content-Type:application/json")
    @POST("users/target")
    fun postUsers(
        @Body body : RequestCollectionData
    )  : Call<ResponseCollectionData>

    // 칭찬 컬렉션 조회
    @Headers("Content-Type:application/json")
    @GET("praise/collection")
    fun getCollection(

    ): Call<ResponseCardData>
    
    // 레벨 조회
    @Headers("Content-Type:application/json")
    @GET("level/praise/{userIdx}")
    fun getuserIdx(
        @Path("userIdx") userIdx : Int
    )  : Call<ResponseUserData>
}
```
