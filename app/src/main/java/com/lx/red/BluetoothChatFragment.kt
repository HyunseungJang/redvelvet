package com.lx.red

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import com.lx.red.common.logger.Log


class BluetoothChatFragment : Fragment() {
    private val TAG = "BluetoothChatFragment"

    // Intent request codes
    private val REQUEST_CONNECT_DEVICE_SECURE = 1
    private val REQUEST_CONNECT_DEVICE_INSECURE = 2
    private val REQUEST_ENABLE_BT = 3

    // Layout Views
    private var mConversationView: ListView? = null
    private var mOutEditText: EditText? = null
    private var mSendButton: Button? = null
    private var mhelp: Button? = null
    private var mhelp2: Button? = null
    private var mhelp3: Button? = null
    private var mConnectedDeviceName: String? = null
    private var mConversationArrayAdapter: ArrayAdapter<String>? = null
    private var mOutStringBuffer: StringBuffer? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mChatService: BluetoothChatService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val activity = getActivity()
        if (mBluetoothAdapter == null && activity != null) {
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show()
            activity.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mBluetoothAdapter == null) {
            return
        }
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        } else if (mChatService == null) {
            setupChat()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mChatService != null) {
            mChatService!!.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mChatService != null) {
            if (mChatService!!.mState == BluetoothChatService.STATE_NONE) {
                mChatService!!.start()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mConversationView = view.findViewById(R.id.`in`)
        mOutEditText = view.findViewById(R.id.editTextOut)
        mSendButton = view.findViewById(R.id.buttonSend)
        mhelp = view.findViewById(R.id.help)
        mhelp2 = view.findViewById(R.id.help2)
        mhelp3 = view.findViewById(R.id.help3)
    }

    private fun setupChat() {
        Log.d(TAG, "setupChat()")

        val activity = getActivity() ?: return
        mConversationArrayAdapter = ArrayAdapter(activity, R.layout.message)
        mConversationView!!.adapter = mConversationArrayAdapter
        mOutEditText!!.setOnEditorActionListener(mWriteListener)
        mSendButton!!.setOnClickListener {
            val view = view
            if (null != view) {
                val textView = view.findViewById<TextView>(R.id.editTextOut)
                val message = textView.text.toString()
                sendMessage(message)
            }
        }
        mhelp?.setOnClickListener {
            val view = view
            if (null != view) {
                val message = view.findViewById<Button>(R.id.help).text.toString()
                sendMessage(message)
            }
        }
        mhelp2?.setOnClickListener {
            val view = view
            if (null != view) {
                val message = view.findViewById<Button>(R.id.help2).text.toString()
                sendMessage(message)
            }
        }
        mhelp3?.setOnClickListener {
            val view = view
            if (null != view) {
                val message = view.findViewById<Button>(R.id.help3).text.toString()
                sendMessage(message)
            }
        }
        mChatService = BluetoothChatService(activity, mHandler)

        mOutStringBuffer = StringBuffer()
    }

    @SuppressLint("MissingPermission")
    private fun ensureDiscoverable() {
        if (mBluetoothAdapter!!.scanMode !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE
        ) {
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            startActivity(discoverableIntent)
        }
    }

    private fun sendMessage(message: String) {
        if (mChatService!!.mState != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(activity, R.string.not_connected, Toast.LENGTH_SHORT).show()
            return
        }
        if (message.length > 0) {
            val send = message.toByteArray()
            mChatService!!.write(send)
            mOutStringBuffer!!.setLength(0)
            mOutEditText!!.setText(mOutStringBuffer)
        }
    }

    private val mWriteListener = OnEditorActionListener { view, actionId, event ->
        if (actionId == EditorInfo.IME_NULL && event.action == KeyEvent.ACTION_UP) {
            val message = view.text.toString()
            sendMessage(message)
        }
        true
    }

    private fun setStatus(resId: Int) {
        val activity = activity ?: return
        val actionBar = activity.actionBar ?: return
        actionBar.setSubtitle(resId)
    }

    private fun setStatus(subTitle: CharSequence) {
        val activity = getActivity() ?: return
        val actionBar = activity.actionBar ?: return
        actionBar.subtitle = subTitle
    }

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            val activity = getActivity()
            when (msg.what) {
                Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
                    BluetoothChatService.STATE_CONNECTED -> {
                        setStatus(getString(R.string.title_connected_to, mConnectedDeviceName))
                        mConversationArrayAdapter!!.clear()
                    }
                    BluetoothChatService.STATE_CONNECTING -> setStatus(R.string.title_connecting)
                    BluetoothChatService.STATE_LISTEN, BluetoothChatService.STATE_NONE -> setStatus(
                        R.string.title_not_connected
                    )
                }
                Constants.MESSAGE_WRITE -> {
                    val writeBuf = msg.obj as ByteArray
                    // construct a string from the buffer
                    val writeMessage = String(writeBuf)
                    mConversationArrayAdapter!!.add("${MemberData.memberId} : $writeMessage")
                }
                Constants.MESSAGE_READ -> {
                    val readBuf = msg.obj as ByteArray
                    val readMessage = String(readBuf, 0, msg.arg1)
                    mConversationArrayAdapter!!.add("$mConnectedDeviceName:  $readMessage")
                }
                Constants.MESSAGE_DEVICE_NAME -> {
                    mConnectedDeviceName = msg.data.getString(Constants.DEVICE_NAME)
                    if (null != activity) {
                        Toast.makeText(
                            activity, "Connected to "
                                    + mConnectedDeviceName, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                Constants.MESSAGE_TOAST -> if (null != activity) {
                    Toast.makeText(
                        activity, msg.data.getString(Constants.TOAST),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CONNECT_DEVICE_SECURE ->
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true)
                }
            REQUEST_CONNECT_DEVICE_INSECURE ->
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false)
                }
            REQUEST_ENABLE_BT ->
                if (resultCode == Activity.RESULT_OK) {
                    setupChat()
                } else {
                    Log.d(TAG, "BT not enabled")
                    val activity = activity
                    if (activity != null) {
                        Toast.makeText(
                            activity, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT
                        ).show()
                        activity.finish()
                    }
                }
        }
    }

    private fun connectDevice(data: Intent?, secure: Boolean) {
        val extras = data!!.extras ?: return
        val address = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS)
        val device = mBluetoothAdapter!!.getRemoteDevice(address)
        mChatService!!.connect(device, secure)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.secure_connect_scan -> {
                val serverIntent = Intent(getActivity(), DeviceListActivity::class.java)
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE)
                return true
            }
            R.id.insecure_connect_scan -> {
                val serverIntent = Intent(getActivity(), DeviceListActivity::class.java)
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE)
                return true
            }
            R.id.discoverable -> {
                ensureDiscoverable()
                return true
            }
        }
        return false
    }

    companion object {
        private const val TAG = "BluetoothChatFragment"
        private const val REQUEST_CONNECT_DEVICE_SECURE = 1
        private const val REQUEST_CONNECT_DEVICE_INSECURE = 2
        private const val REQUEST_ENABLE_BT = 3
    }
}