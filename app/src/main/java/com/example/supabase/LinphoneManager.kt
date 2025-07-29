package com.example.supabase

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import org.linphone.core.*

object LinphoneManager {
    private var core: Core? = null
    private var handler: Handler? = null
    private var initialized = false

    private const val DEFAULT_DOMAIN = "192.168.7.120"

    fun start(context: Context) {
        if (initialized) return

        val factory = Factory.instance()
        factory.setDebugMode(true, "Linphone")

        try {
            core = factory.createCore(null, null, context)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        handler = Handler(Looper.getMainLooper())
        handler?.post(object : Runnable {
            override fun run() {
                core?.iterate()
                handler?.postDelayed(this, 20)
            }
        })

        core?.start()
        initialized = true
    }

    fun registerSipAccount(
        username: String,
        password: String,
        domain: String = DEFAULT_DOMAIN,
        application: Application
    ) {
        if (core == null) return

        val factory = Factory.instance()

        try {
            val authInfo = factory.createAuthInfo(username, null, password, null, null, domain)
            core?.addAuthInfo(authInfo)

            val identity = factory.createAddress("sip:$username@$domain")
            val serverAddr = factory.createAddress("sip:$domain")

            if (identity != null && serverAddr != null) {
                val params = core?.createAccountParams()
                params?.apply {
                    identityAddress = identity
                    serverAddress = serverAddr
                    isRegisterEnabled = true
                    transport = TransportType.Udp
                }

                val account = params?.let { core?.createAccount(it) }
                account?.let {
                    core?.defaultAccount = it
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startOutgoingCall(sipUri: String) {
        if (core == null || core?.defaultAccount == null) {
            println("Core not initialized or not registered")
            return
        }

        val address = core?.interpretUrl(sipUri)
        if (address != null) {
            core?.inviteAddress(address)
        } else {
            println("Invalid SIP address: $sipUri")
        }
    }

    fun endCall() {
        core?.currentCall?.terminate()
    }

    fun stop() {
        handler?.removeCallbacksAndMessages(null)
        core?.stop()
        core = null
        initialized = false
    }

    fun getCore(): Core? = core
}
