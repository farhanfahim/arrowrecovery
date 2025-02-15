package com.tekrevol.arrowrecovery.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants.*
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.zopim.android.sdk.api.ZopimChat
import com.zopim.android.sdk.prechat.ZopimChatActivity
import kotlinx.android.synthetic.main.fragment_support_customer.*
import zendesk.core.AnonymousIdentity
import zendesk.core.Zendesk
import zendesk.support.Support


class CustomerSupportFragment : BaseFragment() {

    companion object {

        fun newInstance(): CustomerSupportFragment {

            val args = Bundle()

            val fragment = CustomerSupportFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Zendesk.INSTANCE.init(context!!, CHAT_URL, APP_ID, CLIENT_ID)

        val identity = AnonymousIdentity()
        Zendesk.INSTANCE.setIdentity(identity)

        Support.INSTANCE.init(Zendesk.INSTANCE)

        ZopimChat.init(ACCOUNT_KEY)

    }


    override fun getDrawerLockMode(): Int {

        return 0
    }

    override fun getFragmentLayout(): Int {

        return R.layout.fragment_support_customer
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {


        btnLiveChat.setOnClickListener {

            startActivity(Intent(context, ZopimChatActivity::class.java))
        }

        btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:800-222-6832")
            startActivity(intent)
        }

        btnLocation.setOnClickListener{
            baseActivity.addDockableFragment(CollectionCentersFragment.newInstance(), true)
        }


    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }


}