package org.enkasacco.mobile.models.register

/**
 * Created by dilpreet on 31/7/17.
 */

data class ClientUserRegisterPayload(

    var username: String? = null,

    var firstname: String? = null,

    var lastname: String? = null,

    var email: String? = null,

    var mobileNo: String? = null,

    var isSelfServiceUser: Boolean? = true,
    var sendPasswordToEmail: Boolean? = false,
    var roles: IntArray = intArrayOf(2),

    var password: String? = null,
    var officeId: Int =1,
    var legalFormId:Int =1,
    var locale:String = "en",
    var repeatPassword:String? =null,

   // {"roles":[2],"             ":"Test","lastname":"Account","email":"cliffmmasi@gmail.com","officeId":1,"username":"testaccount","legalFormId":1,"locale":"en"}
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClientUserRegisterPayload

        if (!roles.contentEquals(other.roles)) return false

        return true
    }

    override fun hashCode(): Int {
        return roles.contentHashCode()
    }
}
