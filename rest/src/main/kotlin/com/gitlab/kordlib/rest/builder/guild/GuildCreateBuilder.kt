package com.gitlab.kordlib.rest.builder.guild

import com.gitlab.kordlib.common.entity.ChannelType
import com.gitlab.kordlib.common.entity.DefaultMessageNotificationLevel
import com.gitlab.kordlib.common.entity.ExplicitContentFilter
import com.gitlab.kordlib.common.entity.VerificationLevel
import com.gitlab.kordlib.rest.builder.KordDsl
import com.gitlab.kordlib.rest.builder.RequestBuilder
import com.gitlab.kordlib.rest.builder.role.RoleCreateBuilder
import com.gitlab.kordlib.rest.json.request.GuildCreateChannelRequest
import com.gitlab.kordlib.rest.json.request.GuildCreateRequest
import com.gitlab.kordlib.rest.json.request.GuildRoleCreateRequest

@KordDsl
class GuildCreateBuilder : RequestBuilder<GuildCreateRequest> {
    lateinit var name: String
    var region: String? = null
    var icon: String? = null
    var verificationLevel: VerificationLevel? = null
    var defaultMessageNotificationLevel: DefaultMessageNotificationLevel? = null
    var explicitContentFilter: ExplicitContentFilter? = null
    var everyoneRole: RoleCreateBuilder? = null
    val roles: MutableList<GuildRoleCreateRequest> = mutableListOf()
    val channels: MutableList<GuildCreateChannelRequest> = mutableListOf()

    fun addChannel(name: String, type: ChannelType) {
        channels.add(GuildCreateChannelRequest(name, type))
    }

    inline fun addRole(builder: RoleCreateBuilder.() -> Unit) {
        roles += RoleCreateBuilder().apply(builder).toRequest()
    }

    inline fun addEveryoneRole(builder: RoleCreateBuilder.() -> Unit) {
        everyoneRole = RoleCreateBuilder().apply(builder)
    }

    override fun toRequest(): GuildCreateRequest = GuildCreateRequest(
            name,
            region,
            icon,
            verificationLevel,
            defaultMessageNotificationLevel,
            explicitContentFilter,
            if (roles.isEmpty()) null else everyoneRole?.let { roles + it.toRequest() } ?: roles,
            if (channels.isEmpty()) null else channels
    )
}